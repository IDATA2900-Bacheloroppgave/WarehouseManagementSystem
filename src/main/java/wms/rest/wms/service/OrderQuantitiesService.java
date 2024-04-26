package wms.rest.wms.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.OrderQuantities;
import wms.rest.wms.repository.OrderQuantitiesRepository;

import java.util.List;

/**
 * Service class for OrderQuantity API controller.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Service
@AllArgsConstructor
public class OrderQuantitiesService {

    /** Repository for handling OrderQuantities persistence operations */
    private OrderQuantitiesRepository orderQuantitiesRepository;

    /**
     * Returns a list of all OrderQuantities.
     *
     * @return a list of all OrderQuantities.
     */
    public List<OrderQuantities> getOrderQuantities(){
        return this.orderQuantitiesRepository.findAll();
    }
}