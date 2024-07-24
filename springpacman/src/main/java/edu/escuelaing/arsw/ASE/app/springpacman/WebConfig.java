package edu.escuelaing.arsw.ASE.app.springpacman;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up CORS (Cross-Origin Resource Sharing) in the application.
 * <p>
 * This class implements {@link WebMvcConfigurer} and provides a custom configuration for handling CORS
 * requests. It allows specific origins and methods for API endpoints.
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures CORS settings for the application.
     * <p>
     * This method sets up the allowed origins and methods for API endpoints matching the pattern "/api/**".
     * </p>
     * 
     * @param registry The {@link CorsRegistry} object used to configure CORS mappings.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST");
    }
}
