package wms.rest.wms.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.User;

import java.util.List;

public interface OrderRepository extends ListCrudRepository<Order, Integer > {
    List<Order> findByUser(User user);
}
