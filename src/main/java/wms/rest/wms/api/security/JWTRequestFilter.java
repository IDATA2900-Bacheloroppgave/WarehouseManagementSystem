package wms.rest.wms.api.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import wms.rest.wms.model.Customer;
import wms.rest.wms.repository.CustomerRepository;
import wms.rest.wms.service.security.JwtService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Filter class for processing JWT authentication.
 *
 * @Author Mikkel Stavelie.
 * @version 1.0.
 */
@Component
@AllArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    /** Service for handling JWT operations */
    private JwtService jwtService;

    /** Repository for handling Customer persistence operations */
    private CustomerRepository customerRepository;

    /**
     * Filters incoming HTTP requests to authenticate JWT tokens.
     *
     * @param request the HTTP request to be filtered.
     * @param response the HTTP response.
     * @param filterChain the filter chain for passing the request and response to the next filter.
     * @throws ServletException if a servlet exception occurs.
     * @throws IOException if an I/O exception occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            try {
                String email = jwtService.getEmail(token);
                Optional<Customer> opUser = customerRepository.findByEmail(email);
                if(opUser.isPresent()){
                    Customer user = opUser.get();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTDecodeException ex) {
            }
        }
        filterChain.doFilter(request, response);
    }
}
