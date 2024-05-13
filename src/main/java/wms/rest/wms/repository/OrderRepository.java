package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Order entities in the database.
 * Provides CRUD operations and custom queries for retrieving Order entities.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Repository
public interface OrderRepository extends ListCrudRepository<Order, Integer> {

    /**
     * Retrieves a List of Orders associated to a specific Customer.
     *
     * @param customer the Customer whose Orders are to be retrieved.
     * @return a List of Orders associated with the specific Customer.
     */
    List<Order> findByCustomer(Customer customer);
}
