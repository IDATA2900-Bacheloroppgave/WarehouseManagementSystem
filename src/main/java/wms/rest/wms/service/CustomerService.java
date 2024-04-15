package wms.rest.wms.service;

import org.springframework.stereotype.Service;
import wms.rest.wms.api.model.LoginBody;
import wms.rest.wms.api.model.RegistrationBody;
import wms.rest.wms.exception.UserAlreadyExistsException;
import wms.rest.wms.model.Customer;
import wms.rest.wms.model.Store;
import wms.rest.wms.repository.CustomerRepository;
import wms.rest.wms.repository.StoreRepository;
import wms.rest.wms.service.security.EncryptionService;
import wms.rest.wms.service.security.JwtService;

import java.util.Optional;

/**
 * Service class for user api controller.
 */
@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    private StoreRepository storeRepository;

    private EncryptionService encryptionService;

    private JwtService jwtService;

    public CustomerService(CustomerRepository customerRepository
            , EncryptionService encryptionService
            , JwtService jwtService
            , StoreRepository storeRepository){
        this.customerRepository = customerRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.storeRepository = storeRepository;
    }

    public Customer registerCustomer(RegistrationBody registrationBody) throws UserAlreadyExistsException {
        if (customerRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with this email.");
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
        return customerRepository.save(customer);
    }


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
