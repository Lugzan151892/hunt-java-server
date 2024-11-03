package lugzan.co.huntjavaserver.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lugzan.co.huntjavaserver.models.refresh_token.RefreshToken;
import lugzan.co.huntjavaserver.models.user.UserModel;
import lugzan.co.huntjavaserver.repository.RefreshTokenRepository;
import lugzan.co.huntjavaserver.repository.UserRepository;
import lugzan.co.huntjavaserver.services.jwtservice.JwtService;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        String accessJwtToken = null;
        String refreshJwtToken = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessJwtToken = authHeader.substring(7);
        }
        refreshJwtToken = extractJwtFromCookies(request);

        if (StringUtils.hasText(accessJwtToken) && !JwtService.isTokenExpired(accessJwtToken)) {
            Claims claims = JwtService.getTokenData(accessJwtToken);

            String userName = claims.getSubject();

            UserModel user = userRepository.findByUsername(userName);

            if (user != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    
                SecurityContextHolder.getContext().setAuthentication(authentication);
    
                String newToken = JwtService.createAccessJwtToken(user.getId(), userName);
                response.setHeader("Authorization", newToken);

                Optional<RefreshToken> userRefreshToken = refreshTokenRepository.findByUser(user);
                if (userRefreshToken.isPresent()) {
                    response.setHeader("Set-Cookie", "auth-token=" + userRefreshToken.get().getToken() + "; HttpOnly; Path=/; Max-Age=86400; SameSite=Lax");
                }

                chain.doFilter(request, response);
                return;
            }
        }

        if (StringUtils.hasText(refreshJwtToken) && !JwtService.isTokenExpired(refreshJwtToken)) {
            Claims claims = JwtService.getTokenData(refreshJwtToken);

            String userName = claims.getSubject();
            UserModel user = userRepository.findByUsername(userName);

            if (user != null) {              
                Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUser(user);

                if (refreshToken.isPresent() && refreshJwtToken.equals(refreshToken.get().getToken()) && !JwtService.isTokenExpired(refreshToken.get().getToken())) {
                    RefreshToken currentToken = refreshToken.get();

                    String newRefreshToken = JwtService.createRefreshJwtToken(user.getId(), userName);
                    String newAccessToken = JwtService.createAccessJwtToken(user.getId(), userName);
                    currentToken.setToken(newRefreshToken);
                    refreshTokenRepository.save(currentToken);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    response.setHeader("Authorization", newAccessToken);
                    response.setHeader("Set-Cookie", "auth-token=" + newRefreshToken + "; HttpOnly; Path=/; Max-Age=86400; SameSite=Lax");

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    chain.doFilter(request, response);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }


    private String extractJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
