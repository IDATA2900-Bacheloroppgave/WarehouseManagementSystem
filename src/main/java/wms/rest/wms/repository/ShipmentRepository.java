package wms.rest.wms.repository;

import org.springframework.data.repository.ListCrudRepository;
import wms.rest.wms.model.Shipment;

public interface ShipmentRepository extends ListCrudRepository<Shipment, Integer> {



}
