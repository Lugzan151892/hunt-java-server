package lugzan.co.huntjavaserver.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import lugzan.co.huntjavaserver.repository.RefreshTokenRepository;
import lugzan.co.huntjavaserver.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public SecurityConfig(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/api/user/login", "/api/user/registration").permitAll()
            .anyRequest().authenticated()
        )
        .cors(cors -> cors.configurationSource(request -> {
            var corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
            corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
            corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
            corsConfiguration.setExposedHeaders(Arrays.asList("Set-Cookie", "Content-Type"));
            corsConfiguration.setAllowCredentials(true);
            return corsConfiguration;
        }))
        .addFilterBefore(new JwtAuthenticationFilter(userRepository, refreshTokenRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
