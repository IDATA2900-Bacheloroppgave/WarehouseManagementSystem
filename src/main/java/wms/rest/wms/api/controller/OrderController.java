package wms.rest.wms.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.Customer;
import wms.rest.wms.model.Product;
import wms.rest.wms.service.OrderService;

import java.util.List;
import java.util.Optional;

@Tag(name = "Orders", description = "All endpoint operations related to Orders.")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Get a list of all orders associated to authenticated customer", description = "Returns a list of all orders associated to authenticated customer", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Product.class))),})
    @GetMapping
    public List<Order> getOrders(@AuthenticationPrincipal Customer user){
        return this.orderService.getOrders(user);
    }

    @Operation(summary = "Get a specific order associated to authenticated customer by id", description = "Returns a specific order based on the path variable id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = Product.class))),})
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@AuthenticationPrincipal Customer customer, @PathVariable("id") int orderId){
        if(customer == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Customer needs to be authenticated to access order");
        }
        Optional<Order> order = this.orderService.getOrderById(orderId);
        if(order.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body("Order with id: " + orderId);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no such order associated with the authenticated customer");
        }
    }

    @Operation(summary = "Cancel a specific order associated to authenticated customer by id", description = "Cancels a specific order based on the path variable id." +
            " The order that is going to be cancelled can only be cancelled if the order status is REGISTERED", responses = {
            @ApiResponse(responseCode = "200", description = "Successful order cancel", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = Product.class))),})
    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal Customer customer, @PathVariable("id") int orderId) {
        if(customer == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Customer needs to be authenticated to access order");
        }
        boolean success = orderService.cancelOrderById(orderId);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).body("Order with orderId: " + orderId + " was cancelled");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order was not cancelled");
        }
    }
}
