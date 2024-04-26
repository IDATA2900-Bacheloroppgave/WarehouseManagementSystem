package wms.rest.wms.api.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

/**
 * Configuration class for security settings.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    /** JWT request filter for handling JWT authentication */
    private JWTRequestFilter jwtRequestFilter;

    /**
     * List of all endpoints permitted without authentication.
     */
    private static final String[] WHITELIST = {
            "/api/products/**", "/auth/register"
            , "/api/customers/**", "/api/address/**"
            , "/auth/login", "/api/orders/**"
            , "/api/stores/**"
            , "/api/trips/**", "/api/shipments/**"
            , "/swagger-ui/**", "/v3/api-docs/**"
            , "/swagger-resources/**", "/swagger-resources"
    };

    /**
     * Configures security filters and authorization rules.
     *
     * @param http the HttpSecurity object to configure security.
     * @return the configured security chain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtRequestFilter, AuthorizationFilter.class) // Run authentication filter before http request filter.
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(WHITELIST).permitAll() // Exclusion rules
                        .anyRequest().authenticated()); // Everything else needs authorization
        return http.build();
    }
}
