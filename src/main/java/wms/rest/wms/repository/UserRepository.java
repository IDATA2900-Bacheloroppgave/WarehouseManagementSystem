package wms.rest.wms.repository;

import org.springframework.data.repository.CrudRepository;
import wms.rest.wms.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmailIgnoreCase(String email);


}
