package wms.rest.wms.service;

import org.springframework.stereotype.Service;
import wms.rest.wms.api.model.RegistrationBody;
import wms.rest.wms.exception.UserAlreadyExistsException;
import wms.rest.wms.model.User;
import wms.rest.wms.repository.UserRepository;

/**
 * Service class for user api controller.
 */
@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {
        if(userRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()){
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setPassword(registrationBody.getPassword());
        return userRepository.save(user);
    }


}
