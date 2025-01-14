package by.Filin.TaskManager.token;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

import io.jsonwebtoken.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("${jwt.AccessLifetime}")
    private Duration accessTokenDuration;

    @Getter
    @Value("${jwt.RefreshLifetime}")
    private Duration refreshTokenDuration;

//    // Генерация токена с указанием времени жизни
//    private String generateToken(String username, Duration expiration) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expiration.toMillis()))
//                .signWith(SignatureAlgorithm.HS256, secret)
//                .compact();
//    }

    private String generateToken(UserDetails userDetails, int tokenDuration) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", roles);

        Date issuedDate = new Date();
        Date expirationDate = new Date(issuedDate.getTime() + tokenDuration * 1000L);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    // Генерация access токена
    public String generateAccessToken(UserDetails userDetails) {
//        return generateToken(username, accessTokenDuration);
//        System.out.println(Math.toIntExact(accessTokenDuration.toSeconds()));
        return generateToken(userDetails, Math.toIntExact(accessTokenDuration.toSeconds()));
    }

    // Генерация refresh токена
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, Math.toIntExact(refreshTokenDuration.toSeconds()));
    }

    // Универсальный метод для получения поля из токена
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Извлечение username из токена
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Проверка валидности токена
    public boolean isTokenValid(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    // Проверка срока действия токена
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

//    Извлечение всех данных из токена
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public List<String> getUserRoles(String jwtToken) {
        return extractAllClaims(jwtToken).get("roles", List.class);
    }
}
