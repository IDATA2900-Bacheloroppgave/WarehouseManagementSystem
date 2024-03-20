package wms.rest.wms.repository;

import org.springframework.data.repository.CrudRepository;
import wms.rest.wms.model.Customer;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Customer, Integer> {

    Optional<Customer> findByEmailIgnoreCase(String email);

    Optional<Customer> findByEmail(String email);


}
