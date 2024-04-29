package wms.rest.wms.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Represents the body of a login request containing user email address and password.
 *
 * @author Mikkel Stavelie.
 */
@AllArgsConstructor
@NoArgsConstructor
public class LoginBody {

    /** The email address in the login body */
    private String email;

    /** The password in the login body */
    private String password;

    /**
     * Return the email address associated with the login request.
     *
     * @return the email address provided in the login request.
     */
    @NotBlank
    @NotBlank
    @Email
    public String getEmail() {
        return email;
    }

    /**
     * Return the password associated with the login request.
     *
     * @return the password provided in the login request.
     */
    @NotBlank
    @NotNull
    public String getPassword() {
        return password;
    }
}


