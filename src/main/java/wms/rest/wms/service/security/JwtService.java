package wms.rest.wms.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import wms.rest.wms.model.Customer;

import java.util.Date;

/**
 * Service class for JSON web token (JWT) creation
 */
@Service
public class JwtService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiryInSeconds}") // 1 Week
    private int expiryInSeconds;

    private Algorithm algorithm;

    private static final String EMAIL_KEY = "EMAIL";

    /**
     * Initializes the cryptographic algorithm used for JWT signing after all necessary dependencies are injected
     */
    @PostConstruct
    public void postConstruct() {
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    /**
     * Generates a JSON Web Token (JWT) for a given Customer
     *
     * This method creates a JWT with a claim based on the customers email, an expiry time, and a specified issuer.
     * The token is then signed with the predefined algorithm
     *
     * @param customer the Customer object containng the details needed for the JWT
     * @return a signed JWT as a String
     */
    public String generateJWT(Customer customer){
        return JWT.create()
                .withClaim(EMAIL_KEY, customer.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * expiryInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    /**
     * Extracts and returns the email address from a given JSON web token
     *
     * @param token the JWT String from which the email is to be extracted
     * @return the email address as a String if present, otherwise null
     */
    public String getEmail(String token){
        return JWT.decode(token).getClaim(EMAIL_KEY).asString();
    }
}
