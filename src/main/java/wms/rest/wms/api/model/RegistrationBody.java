package wms.rest.wms.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wms.rest.wms.model.Store;

/**
 * Represents the body of a registration request, containing user registration details.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegistrationBody {

    /** The email address of the Customer. Must be xxx@xxx.xxx format */
    @NotNull
    @NotBlank
    @Email
    private String email;

    /** The first name of the Customer */
    @NotNull
    @NotBlank
    @Size(min = 3)
    @Size(max = 20)
    private String firstName;

    /** The last name of the Customer */
    @NotNull
    @NotBlank
    @Size(min = 3)
    @Size(max = 20)
    private String lastName;

    /** The password for Customer authentication. Must be minimum eight characters, and at least one letter and one number */
    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    private String password;

    /** The storeId of the Store associated to the Customer */
    @Min(0)
    private int storeId;
}
