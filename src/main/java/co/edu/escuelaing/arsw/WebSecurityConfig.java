package co.edu.escuelaing.arsw;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

        /**
         * Configures web security for the application.
         *
         * @param http HttpSecurity instance to configure.
         * @return SecurityFilterChain configured with authentication and authorization
         *         settings.
         * @throws Exception If an error occurs while configuring security.
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests((requests) -> requests
                                                .requestMatchers("/", "/index", "/image/**", "/css/**","/register").permitAll()
                                                .anyRequest().authenticated())
                                .formLogin((form) -> form
                                                .loginPage("/login")
                                                .permitAll())
                                .logout((logout) -> logout.permitAll());

                return http.build();
        }

        /**
         * Provides user details for authentication.
         *
         * @return InMemoryUserDetailsManager instance populated with user details.
         */
        @Bean
        public UserDetailsService userDetailsService() {
                UserDetails user1 = User.withDefaultPasswordEncoder()
                                .username("user")
                                .password("password")
                                .roles("USER")
                                .build();

                UserDetails user2 = User.withDefaultPasswordEncoder()
                                .username("admin")
                                .password("adminpass")
                                .roles("ADMIN")
                                .build();
                return new InMemoryUserDetailsManager(user1, user2);
        }
}
