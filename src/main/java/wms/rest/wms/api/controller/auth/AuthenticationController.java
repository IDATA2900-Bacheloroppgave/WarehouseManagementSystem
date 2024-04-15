package wms.rest.wms.api.controller.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wms.rest.wms.api.model.LoginBody;
import wms.rest.wms.api.model.LoginResponse;
import wms.rest.wms.api.model.RegistrationBody;
import wms.rest.wms.exception.UserAlreadyExistsException;
import wms.rest.wms.model.Customer;
import wms.rest.wms.service.CustomerService;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {

    private CustomerService customerService;

    public AuthenticationController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegistrationBody registrationBody){
        try{
            customerService.registerCustomer(registrationBody);
            return ResponseEntity.ok().build();
        } catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody){
        String jwt = customerService.loginCustomer(loginBody);
        if(jwt == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            System.out.printf("Customer with email: " + loginBody.getEmail() + " just logged in!");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/me")
    public Customer getLoggedInUserProfile(@AuthenticationPrincipal Customer user){
        return user;
    }
}
