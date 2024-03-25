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

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())           //TODO: MIGHT NEED TO LOOK INTO THIS MORE
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/api/products", "/auth/register", "/auth/login").permitAll() // Allow unauthorized access
//                        .anyRequest().authenticated()) // Any other
//                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Apply the JWTRequestFilter
//
//        return http.build();
//    }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtRequestFilter, AuthorizationFilter.class) // Run authentication filter before http request filter.
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/products/**", "/auth/register", "/auth/login", "/api/orders/**",
                                "/api/trips/**", "/api/shipments/**").permitAll() // Exclusion rules
                        .anyRequest().authenticated()); // Everything else needs authorization
        return http.build();
    }
}
