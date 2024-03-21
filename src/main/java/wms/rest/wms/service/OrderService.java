package wms.rest.wms.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.OrderStatus;
import wms.rest.wms.model.Trip;
import wms.rest.wms.model.Customer;
import wms.rest.wms.repository.OrderRepository;
import wms.rest.wms.repository.TripRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * Service class for order api controller.
 */
@Service
public class OrderService {

    private OrderRepository orderRepository;
    private TripRepository tripRepository;

    public OrderService(OrderRepository orderRepository, TripRepository tripRepository){
        this.orderRepository = orderRepository;
        this.tripRepository = tripRepository;
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

    /**
     * Runs every 3 minutes. Updates every OrderStatus for development purposes.
     */
    @Scheduled(fixedRate = 180000) //Runs method every 3 minutes
    public void updateOrderStatuses(){
        List<Order> orders = orderRepository.findAll();
        List<Trip> trips = tripRepository.findAll();

        for (Order order : orders) {
            if (order.getOrderStatus() == OrderStatus.REGISTERED) {
                order.setOrderStatus(OrderStatus.PICKING);
            } else if (order.getOrderStatus() == OrderStatus.PICKING) {
                order.setOrderStatus(OrderStatus.PICKED);
            } //TODO: CHANGE TRIPSTATUS TO READY_FOR_DEPARTURE
            orderRepository.save(order);
        }
    }
}
