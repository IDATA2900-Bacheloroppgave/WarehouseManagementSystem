package wms.rest.wms.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wms.rest.wms.model.Store;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegistrationBody {

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 3)
    @Size(max = 20)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(min = 3)
    @Size(max = 20)
    private String lastName;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$") //Minimum eight characters, atleast one letter and one number
    private String password;

    @Min(0)
    private int storeId;
}
