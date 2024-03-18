package wms.rest.wms.service;

import org.springframework.stereotype.Service;
import wms.rest.wms.repository.OrderRepository;

/**
 * Service class for order api controller.
 */
@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }
}
