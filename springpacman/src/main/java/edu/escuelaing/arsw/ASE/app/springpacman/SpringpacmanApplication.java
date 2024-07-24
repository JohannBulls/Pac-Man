package edu.escuelaing.arsw.ASE.app.springpacman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Spring Boot application.
 * <p>
 * This class serves as the main application class that initializes and runs the Spring Boot application.
 * It is annotated with {@link SpringBootApplication}, which enables auto-configuration, component scanning,
 * and configuration properties.
 * </p>
 */
@SpringBootApplication
public class SpringpacmanApplication {

    /**
     * Main method to start the Spring Boot application.
     * <p>
     * This method launches the application by invoking {@link SpringApplication#run(Class, String...)}.
     * </p>
     * 
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringpacmanApplication.class, args);
    }

}
