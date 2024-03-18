package wms.rest.wms.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoginBody {

    private String email;
    private String password;

    @NotBlank
    @NotBlank
    @Email
    public String getEmail() {
        return email;
    }

    @NotBlank
    @NotNull
    public String getPassword() {
        return password;
    }
}


