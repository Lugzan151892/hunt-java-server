package lugzan.co.huntjavaserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/rest/**")
                .allowedOrigins("https://hunter-service.fun/", "http://localhost:5173", "http://localhost:4173")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}