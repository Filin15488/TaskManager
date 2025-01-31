package by.Filin.TaskManager.config;

import by.Filin.TaskManager.service.UserDetailsServiceImpl;
import by.Filin.TaskManager.token.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwtToken;
            if (request.getRequestURI().equals("/refresh")) {
                jwtToken = getRefreshJwtFromRequest(request); // Только refresh-токен
            } else {
                jwtToken = getAccessJwtFromRequest(request); // Остальные запросы
            }

            if (jwtToken != null) {
                processJwtToken(jwtToken, request);
            }
        } catch (Exception ex) {
            logger.error("Error during JWT authentication: ", ex);
        }


//        try {
//            // Извлечение токена
//            String jwtToken = getAccessJwtFromRequest(request);
//            logger.info("Extracted JWT token: " + jwtToken);
//
//            if (jwtToken != null) {
//                // Извлекаем имя пользователя из токена
//                String username = jwtUtil.extractUsername(jwtToken);
//                logger.info("Extracted username: " + username);
//
//                // Если имя пользователя не пустое и контекст безопасности пуст
//                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    // Загружаем данные пользователя из базы данных
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                    logger.debug("Loaded user details: " + userDetails);
//
//                    // Проверяем валидность токена
//                    if (jwtUtil.validateToken(jwtToken, userDetails)) {
//                        // Устанавливаем аутентификацию в контексте
//                        setAuthentication(userDetails, request);
//                        logger.info("JWT token validated successfully for user: " + username);
//                    } else {
////                        log.info("JWT token is invalid for user {}", username);
//                        logger.warn("Invalid JWT token for user: " + username);
//                    }
//                }
//            }
//        } catch (Exception ex) {
//
//            logger.error("Error during JWT authentication: ", ex);
//        }

        // Передача запроса дальше
        filterChain.doFilter(request, response);
    }

    private void processJwtToken(String jwtToken, HttpServletRequest request) {
        String username = jwtUtil.extractUsername(jwtToken);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                setAuthentication(userDetails, request);
                logger.info("JWT token validated successfully for user: " + username);
            } else {
                logger.warn("Invalid JWT token for user: " + username);
            }
        }
    }


    private String getAccessJwtFromRequest(HttpServletRequest request) {
//        return getTokenFromRequest(request, "Authorization", "access_token");
        String tokenFromRequest = getTokenFromRequest(request, "Authorization", "access_token");
        if (tokenFromRequest != null && tokenFromRequest.startsWith("Bearer ")) {
            return tokenFromRequest.substring(7);
        }
        return tokenFromRequest;
    }

    private String getRefreshJwtFromRequest(HttpServletRequest request) {
        return getTokenFromRequest(request, "Refresh-Token", "refresh_token");
    }


    private String getTokenFromRequest(HttpServletRequest request, String headerName, String cookieName) {

        // Проверка в пользовательском заголовке
        String tokenHeader = request.getHeader(headerName);
        if (tokenHeader != null && !tokenHeader.isEmpty()) {
            return tokenHeader;
        }

        // Проверка в куках
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null; // Если токен не найден
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        List<GrantedAuthority> authorities = userDetails.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority("ROLE_" + auth.getAuthority())) // Префикс ROLE_
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("Authentication set for user: " + userDetails.getUsername() + " with roles: " + authorities);
    }

}
