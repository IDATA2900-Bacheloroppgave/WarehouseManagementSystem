package wms.rest.wms.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.Customer;
import wms.rest.wms.service.OrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getOrders(@AuthenticationPrincipal Customer user){
        return this.orderService.getOrders(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") int orderId){
        ResponseEntity response;

        Optional<Order> order = this.orderService.getOrderById(orderId);
        if(order.isPresent()){

            response =  ResponseEntity.ok().body("Order with id: " + orderId);
        } else {
            response = ResponseEntity.badRequest().body("There are no orders associated with the customer");
        }
        return response;
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") int orderId) {
        try {
            boolean success = orderService.cancelOrderById(orderId);
            if (success) {
                return ResponseEntity.ok().body("Order successfully cancelled.");
            } else {
                return ResponseEntity.badRequest().body("Order cannot be cancelled at its current status.");
            }
        } catch (Exception e) {
            // Log the exception details here for debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while cancelling the order.");
        }
    }
}
