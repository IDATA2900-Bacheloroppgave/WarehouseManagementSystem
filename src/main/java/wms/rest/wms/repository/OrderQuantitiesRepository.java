package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.OrderQuantities;
import wms.rest.wms.service.OrderQuantitiesService;

public interface OrderQuantitiesRepository extends ListCrudRepository<OrderQuantities, Integer> {
}
