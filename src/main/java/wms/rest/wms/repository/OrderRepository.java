package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.OrderStatus;
import wms.rest.wms.model.Customer;

import java.util.List;

public interface OrderRepository extends ListCrudRepository<Order, Integer> {
    List<Order> findByUser(Customer user);

    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
