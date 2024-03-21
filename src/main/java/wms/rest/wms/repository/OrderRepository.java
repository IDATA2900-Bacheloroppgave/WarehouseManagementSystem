package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.OrderStatus;
import wms.rest.wms.model.Customer;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends ListCrudRepository<Order, Integer> {
    List<Order> findByCustomer(Customer customer);

    Optional<Order> findByOrderStatus(OrderStatus orderStatus);

}
