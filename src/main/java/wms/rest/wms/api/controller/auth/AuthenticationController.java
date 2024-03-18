package wms.rest.wms.api.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wms.rest.wms.api.model.RegistrationBody;
import wms.rest.wms.exception.UserAlreadyExistsException;
import wms.rest.wms.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private UserService userService;

    public AuthenticationController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegistrationBody registrationBody){
        try{
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch(UserAlreadyExistsException e){ //TODO: FIX EXCEPTION HANDLING
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); //User with registered email already exists
        }
    }

}
