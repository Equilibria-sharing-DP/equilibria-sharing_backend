package api.equilibria_sharing.config;

import api.equilibria_sharing.services.CustomUserDetailsService;
import api.equilibria_sharing.services.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static api.equilibria_sharing.config.CorsConfig.corsConfigurationSource;

/**
 * SecurityConfig Klasse - Hier wird alles security-technische, was global für die Applikation gilt, konfiguriert
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                //activate CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        //permit all requests to login and register endpoints
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/auth/register").permitAll()
                        //only allow POST requests to bookings endpoint (booking creation by user)
                        .requestMatchers(HttpMethod.POST, "/api/v1/bookings").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(AbstractHttpConfigurer::disable);
        // add the JWT Authentication Filter ao all requests
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
