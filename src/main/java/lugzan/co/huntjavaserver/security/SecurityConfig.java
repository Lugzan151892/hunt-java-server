package lugzan.co.huntjavaserver.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import lugzan.co.huntjavaserver.repository.RefreshTokenRepository;
import lugzan.co.huntjavaserver.repository.UserRepository;
import lugzan.co.huntjavaserver.services.enviromentvariables.EnviromentVariables;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public SecurityConfig(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @SuppressWarnings("unused")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/api/user/login", "/api/user/registration", "/api/user/logout").permitAll()
            .anyRequest().authenticated()
        )
        .cors(cors -> cors.configurationSource(request -> {
            var corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(Arrays.asList(EnviromentVariables.getOriginsList().split(",")));
            corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
            corsConfiguration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization", "Content-Type"));
            corsConfiguration.setAllowCredentials(true);
            return corsConfiguration;
        }))
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint((request, response, authException) -> 
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
            )
        )
        .addFilterBefore(new JwtAuthenticationFilter(userRepository, refreshTokenRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
