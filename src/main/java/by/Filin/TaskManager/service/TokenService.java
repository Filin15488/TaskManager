package by.Filin.TaskManager.service;

import by.Filin.TaskManager.entity.AccessToken;
import by.Filin.TaskManager.entity.RefreshToken;
import by.Filin.TaskManager.entity.User;
import by.Filin.TaskManager.repository.AccessTokenRepository;
import by.Filin.TaskManager.repository.RefreshTokenRepository;
import by.Filin.TaskManager.token.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtUtil jwtUtil;


    public AccessToken createAccessToken(User user) {
        String token = jwtUtil.generateAccessToken(user.getUsername());
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(token);
        accessToken.setUser(user);
        accessToken.setExpiresAt(new Date(System.currentTimeMillis() + jwtUtil.getAccessTokenDuration().toMillis()));

        return accessTokenRepository.save(accessToken);
    }

    public RefreshToken createRefreshToken(User user) {
        String token = jwtUtil.generateRefreshToken(user.getUsername());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(new Date(System.currentTimeMillis() + jwtUtil.getRefreshTokenDuration().toMillis()));
        return refreshTokenRepository.save(refreshToken);
    }

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

}
