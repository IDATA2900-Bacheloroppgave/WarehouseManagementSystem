package wms.rest.wms.service.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * Service for handling password encryption and verification using BCrypt hashing algorithm.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Service
public class EncryptionService {

    /** Number of rounds used for generating the salt value in password encryption */
    @Value("${encryption.salt.rounds}")
    private int saltRounds;

    /** Salt value used for password encryption */
    private String salt;

    /**
     * Initializes the salt value for password encryption.
     *
     * This method is called after the service bean is fully initialized and properties injected. It generates
     * a salt using the configured number of salt rounds, which is then used for all password encryptions.
     */
    @PostConstruct
    public void postConstruct(){
        salt = BCrypt.gensalt(saltRounds);
    }

    /**
     * Encrypts a plain text password using BCrypt hashing algorithm.
     *
     * @param password the plain text password to be encrypted.
     * @return the hashed password as a String.
     */
    public String encryptPassword(String password){
        return BCrypt.hashpw(password, salt);
    }

    /**
     * Verifies a plain text password against an encrypted hash.
     *
     * This method checks if the specified plain text password corresponds to the provided hash.
     *
     * @param password the plain text password to verify.
     * @param hash the BCrypt hash against which the password is checked.
     * @return true if the password matches the hash; false otherwise.
     */
    public boolean verifyPassword(String password, String hash){
        return BCrypt.checkpw(password, hash);
    }
}