
package wms.rest.wms.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.OrderQuantities;
import wms.rest.wms.model.Product;
import wms.rest.wms.repository.OrderQuantitiesRepository;

import java.util.List;

/**
 * Service class for OrderQuantity API controller
 */
@Service
@AllArgsConstructor
public class OrderQuantitiesService {

    private OrderQuantitiesRepository orderQuantitiesRepository;

    /**
     * Returns a list of all OrderQuantities
     *
     * @return a list of all OrderQuantities
     */
    public List<OrderQuantities> getOrderQuantities(){
        return this.orderQuantitiesRepository.findAll();
    }
}