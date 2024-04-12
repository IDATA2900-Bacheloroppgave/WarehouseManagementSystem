package wms.rest.wms.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JWTRequestFilter jwtRequestFilter;

    // All permitted endpoints
    private static final String[] WHITELIST = {
            "/api/products/**", "/api/auth/register"
            , "/api/customers/**", "/api/address/**"
            , "/api/auth/login", "/api/orders/**"
            , "/api/trips/**", "/api/shipments/**"
            , "/swagger-ui/**", "/v3/api-docs/**"
            , "/swagger-resources/**", "/swagger-resources"
    };

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
