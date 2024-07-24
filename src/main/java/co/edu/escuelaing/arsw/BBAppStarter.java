package co.edu.escuelaing.arsw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Main application class for the Spring Boot application.
 * The @SpringBootApplication annotation denotes this class as the main entry point for the Spring Boot application.
 */
@SpringBootApplication
public class BBAppStarter {

    public static void main(String[] args) {
        SpringApplication.run(BBAppStarter.class, args);
    }
}
