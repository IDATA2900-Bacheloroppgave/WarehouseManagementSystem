package wms.rest.wms.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.OrderStatus;
import wms.rest.wms.model.Trip;
import wms.rest.wms.model.Customer;
import wms.rest.wms.repository.OrderRepository;
import wms.rest.wms.repository.TripRepository;

import java.util.List;

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
