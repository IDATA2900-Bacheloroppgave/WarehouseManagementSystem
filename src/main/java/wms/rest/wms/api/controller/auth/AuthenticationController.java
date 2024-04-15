package wms.rest.wms.api.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import wms.rest.wms.model.Product;
import wms.rest.wms.service.CustomerService;

@Tag(name = "Authentication", description = "All endpoint operations related to Authentication")
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {

    private CustomerService customerService;

    public AuthenticationController(CustomerService customerService){
        this.customerService = customerService;
    }

    @Operation(summary = "Registers a Customer", description = "Register a customer with registrationbody", responses = {
            @ApiResponse(responseCode = "200", description = "Successful customer registration", content = @Content(schema = @Schema(implementation = RegistrationBody.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = RegistrationBody.class))),})
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegistrationBody registrationBody){
        try{
            customerService.registerCustomer(registrationBody);
            return ResponseEntity.ok().build();
        } catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Login a Customer", description = "Login a Customer with loginbody", responses = {
            @ApiResponse(responseCode = "200", description = "Successful customer login", content = @Content(schema = @Schema(implementation = LoginBody.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = LoginBody.class))),})
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

    @Operation(summary = "Get customer", description = "Get customer by json web token in http request header", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of customer profile",
                            content = @Content(schema = @Schema(implementation = Customer.class)),
                            headers = @Header(name = "Authorization", description = "Bearer [JWT token here]", required = true))})
    @GetMapping("/me")
    public Customer getLoggedInUserProfile(@AuthenticationPrincipal Customer user) {
        return user;
    }
}
