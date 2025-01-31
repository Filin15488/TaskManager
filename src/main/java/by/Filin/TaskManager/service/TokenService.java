package by.Filin.TaskManager.service;

import by.Filin.TaskManager.entity.AccessToken;
import by.Filin.TaskManager.entity.RefreshToken;
import by.Filin.TaskManager.entity.User;
import by.Filin.TaskManager.repository.AccessTokenRepository;
import by.Filin.TaskManager.repository.RefreshTokenRepository;
import by.Filin.TaskManager.repository.UserRepository;
import by.Filin.TaskManager.security.CustomUserDetails;
import by.Filin.TaskManager.token.JwtUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtUtil jwtUtil;

    private final UserRepository userRepository;


    public AccessToken createAccessToken(User user) {
        String token = jwtUtil.generateAccessToken(new CustomUserDetails(user));
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(token);
        accessToken.setUser(user);
        accessToken.setExpiresAt(new Date(System.currentTimeMillis() + jwtUtil.getAccessTokenDuration().toMillis()));

        return accessTokenRepository.save(accessToken);
    }

    public RefreshToken createRefreshToken(User user) {
        String token = jwtUtil.generateRefreshToken(new CustomUserDetails(user));
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(new Date(System.currentTimeMillis() + jwtUtil.getRefreshTokenDuration().toMillis()));
        return refreshTokenRepository.save(refreshToken);
    }

    public List<AccessToken> getAccessTokenFromDB(User user) {
        List<AccessToken> tokens = accessTokenRepository.findAllByUserId(user.getId());
        if (tokens.isEmpty()) {
            throw new RuntimeException("Access tokens not found for user: " + user.getId());
        }
        return tokens;
    }

    public List<RefreshToken> getRefreshTokenFromDB(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserId(user.getId());
        if (tokens.isEmpty()) {
            throw new RuntimeException("Refresh tokens not found for user: " + user.getId());
        }
        return tokens;
    }


//    public List<AccessToken> getAccessTokenFromDB(User user) {
//        return accessTokenRepository.findAllByUserId(user.getId()).orElseThrow(
//                () -> new RuntimeException("User not found")
//        );
//    }
//
//    public List<RefreshToken> getRefreshTokenFromDB(User user) {
//        return refreshTokenRepository.findAllByUserId(user.getId()).orElseThrow(
//                () -> new RuntimeException("User not found")
//        );
//    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(refreshToken -> refreshToken.isValid() && refreshToken.getExpiresAt().after(new Date()))
                .orElse(false);
    }

    public void invalidateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshToken -> {
            refreshToken.setValid(false);
            refreshTokenRepository.save(refreshToken);
        });
    }
    public boolean validateAccessToken(String token) {
        return accessTokenRepository.findByToken(token)
                .map(accessToken -> accessToken.isValid() && accessToken.getExpiresAt().after(new Date()))
                .orElse(false);
    }
    public void deactivateAllAccessTokenByUserId(Long userId) {
        List<AccessToken> accessTokens = accessTokenRepository.findAllByUserId(userId);

        if (accessTokens.isEmpty()) {
            throw new RuntimeException("Access tokens not found for user: " + userId);
        }

        for (AccessToken accessToken : accessTokens) {
            accessToken.setValid(false);
        }

        accessTokenRepository.saveAll(accessTokens);
    }



//    public void deactivateAllAccessTokenByUserId(Long userId) {
//       List<AccessToken> accessTokens = accessTokenRepository.findAllByUserId(userId).orElseThrow(
//               () -> new RuntimeException("User not found")
//       );
//       for (AccessToken accessToken : accessTokens) {
//           accessToken.setValid(false);
//       }
//       accessTokenRepository.saveAll(accessTokens);
//    }

}
