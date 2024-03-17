package wms.rest.wms.repository;

import org.springframework.data.repository.CrudRepository;
import wms.rest.wms.model.Order;

public interface OrderRepository extends CrudRepository<Order, Integer > {
}
