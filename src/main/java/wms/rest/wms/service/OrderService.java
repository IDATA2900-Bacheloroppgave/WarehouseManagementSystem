package wms.rest.wms.service;

import org.springframework.stereotype.Service;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.User;
import wms.rest.wms.repository.OrderRepository;

import java.util.List;

/**
 * Service class for order api controller.
 */
@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrdersByUser(User user){
        return this.orderRepository.findByUser(user);
    }
}
