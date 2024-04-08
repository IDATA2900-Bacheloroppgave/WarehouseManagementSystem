package wms.rest.wms.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.*;
import wms.rest.wms.repository.OrderRepository;
import wms.rest.wms.repository.ProductRepository;
import wms.rest.wms.repository.ShipmentRepository;
import wms.rest.wms.repository.TripRepository;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class for order api controller.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ShipmentService shipmentService, ProductRepository productRepository){
        this.orderRepository = orderRepository;
        this.shipmentService = shipmentService;
        this.productRepository = productRepository;
    }

    public List<Order> getOrders(Customer customer){
        return this.orderRepository.findByCustomer(customer);
    }

    public Optional<Order> getOrderById(int orderId){
        return this.orderRepository.findById(orderId);
    }

    /**
     * Utility method for cancelOrderbyId.
     * Checks if an order has the orderStatus as REGISTERED, which is the only
     * status a Order can have for the customer to cancel the order.
     *
     * @param order the order to check if has orderStatus as REGISTERED
     * @return true if orderStatus is REGISTERED, false otherwise
     */
    public boolean cancelOrder(Order order){
        if(order.getOrderStatus() == OrderStatus.REGISTERED){
            order.setOrderStatus(OrderStatus.CANCELLED);
            return true;
        }
        return false;
    }
    public boolean cancelOrderById(int orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (cancelOrder(order)) {
                orderRepository.save(order);
                return true;
            }
        }
        return false;
    }

    public void updateOrderStatus(int orderId, OrderStatus orderStatus){
        Optional<Order> orderOptional = this.orderRepository.findById(orderId);
        Order order = orderOptional.get();

        order.setOrderStatus(orderStatus);
        this.orderRepository.save(order);

        this.shipmentService.updateTripStatus(order.getShipment().getShipmentId());
    }

    /**
     * Creates a new order associated to a customer.
     *
     * @param order JSON data payload of order.
     * @param customer Authenticated customer.
     * @return saves the order with the orderrepository.
     */
    @Transactional
    public Order createOrder(Order order, Customer customer) {
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatus.REGISTERED);
        order.setOrderDate(new Date(System.currentTimeMillis()));
        order.setAddress(customer.getAddress());

        for (OrderQuantities quantity : order.getQuantities()) {

            Product product = productRepository.findById(quantity.getProduct().getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + quantity.getProduct().getProductId()));

            quantity.setProduct(product);
            quantity.setOrder(order);
        }
        return this.orderRepository.save(order);
    }
}