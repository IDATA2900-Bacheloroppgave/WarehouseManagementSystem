package wms.rest.wms.service;

import org.springframework.stereotype.Service;
import wms.rest.wms.api.model.LoginBody;
import wms.rest.wms.api.model.RegistrationBody;
import wms.rest.wms.exception.UserAlreadyExistsException;
import wms.rest.wms.model.Customer;
import wms.rest.wms.repository.UserRepository;

import java.util.Optional;

/**
 * Service class for user api controller.
 */
@Service
public class UserService {

    private UserRepository userRepository;

    private EncryptionService encryptionService;

    private JwtService jwtService;

    public UserService(UserRepository userRepository, EncryptionService encryptionService, JwtService jwtService){
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public Customer registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {
        if(userRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()){
            throw new UserAlreadyExistsException();
        }
        Customer user = new Customer();
        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        return userRepository.save(user);
    }

    public String loginUser(LoginBody loginBody){
        Optional<Customer> opUser = userRepository.findByEmail(loginBody.getEmail());
        if(opUser.isPresent()){
            Customer user = opUser.get();
            if(encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())){
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }


}
