package wms.rest.wms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Autowired
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrders(User user){
        return this.orderRepository.findByUser(user);
    }
}
