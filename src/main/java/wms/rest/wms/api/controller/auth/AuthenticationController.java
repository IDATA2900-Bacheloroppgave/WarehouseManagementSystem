package wms.rest.wms.api.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
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
import wms.rest.wms.service.UserService;

@Tag(name = "Customers", description = "All endpoint operations related to Customers")
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {

    private UserService userService;

    public AuthenticationController(UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "Register a customer", description = "Register a new customer with registrationbody {email, firstname, lastname, password}", responses = {
            @ApiResponse(responseCode = "200", description = "Successful registation", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content(schema = @Schema(implementation = Product.class)))})
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegistrationBody registrationBody){
        try{
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Customer login. Receieves JWT", description = "Login customer with loginbody {email, password}", responses = {
            @ApiResponse(responseCode = "200", description = "Successful login", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Product.class)))})
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody){
        String jwt = userService.loginUser(loginBody);
        if(jwt == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Profile", description = "Shows user", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Product.class)))})
    @GetMapping("/me")
    public Customer getLoggedInUserProfile(@AuthenticationPrincipal Customer customer){
        return customer;
    }
}