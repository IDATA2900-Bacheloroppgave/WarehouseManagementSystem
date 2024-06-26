package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import wms.rest.wms.model.Customer;

import java.util.Optional;

/**
 * Repository interface for managing Customer entities in the database.
 * Provides CRUD operations and custom queries for retrieving Customer entities.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Repository
public interface CustomerRepository extends ListCrudRepository<Customer, Integer> {

    /**
     * Retrieves a Customer by their email address, ignoring case.
     *
     * @param email the email address of the Customer to retrieve.
     * @return an Optional containing the Customer, if found; otherwise, an empty Optional.
     */
    Optional<Customer> findByEmailIgnoreCase(String email);

    /**
     * Retrieves a Customer by their exact email address.
     *
     * @param email the exact email address of the Customer to retrieve.
     * @return an Optional containing the Customer, if found; otherwise, an empty Optional.
     */
    Optional<Customer> findByEmail(String email);
}

