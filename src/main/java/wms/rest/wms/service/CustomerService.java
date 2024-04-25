package wms.rest.wms.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import wms.rest.wms.api.model.LoginBody;
import wms.rest.wms.api.model.RegistrationBody;
import wms.rest.wms.exception.CustomerAlreadyExistsException;
import wms.rest.wms.model.Customer;
import wms.rest.wms.model.Store;
import wms.rest.wms.repository.CustomerRepository;
import wms.rest.wms.repository.StoreRepository;
import wms.rest.wms.service.security.EncryptionService;
import wms.rest.wms.service.security.JwtService;

import java.util.Optional;

/**
 * Service class for Customer API controller.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Service
@AllArgsConstructor
public class CustomerService {

    /** Repository for handling Customer persistence operations */
    private CustomerRepository customerRepository;

    /** Repository for handling Store persistence operations */
    private StoreRepository storeRepository;

    /** Service for handling password encryption and verification  */
    private EncryptionService encryptionService;

    /** Service for handling JSON web token creation */
    private JwtService jwtService;

    /**
     * Register a Customer service method for endpoint /auth/register.
     *
     * @param registrationBody the registration body required in HTTP request JSON payload.
     * @throws CustomerAlreadyExistsException if Customer tries to register with an
     *         email that is already registered to another Customer.
     */
    public void registerCustomer(RegistrationBody registrationBody) throws CustomerAlreadyExistsException {
        if (customerRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new CustomerAlreadyExistsException("User already exists with this email.");
        }

        // Use the storeId directly to fetch the store
        Store store = storeRepository.findById(registrationBody.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store with ID: " + registrationBody.getStoreId() + " was not found"));

        Customer customer = new Customer();
        customer.setEmail(registrationBody.getEmail());
        customer.setFirstName(registrationBody.getFirstName());
        customer.setLastName(registrationBody.getLastName());
        customer.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        customer.setStore(store);
        customerRepository.save(customer);
    }

    /**
     * Login a Customer service method for endpoint /auth/login.
     *
     * @param loginBody the login body required in HTTP request JSON payload.
     * @return a JSON web token (JWT) if the Customer login is successful. Otherwise, return null.
     */
    public String loginCustomer(LoginBody loginBody){
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(loginBody.getEmail());
        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            if(encryptionService.verifyPassword(loginBody.getPassword(), customer.getPassword())){
                return jwtService.generateJWT(customer);
            }
        }
        return null;
    }
}