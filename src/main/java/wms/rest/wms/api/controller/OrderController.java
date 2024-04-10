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
import wms.rest.wms.exception.NotEnoughStockException;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.Customer;
import wms.rest.wms.model.Product;
import wms.rest.wms.service.OrderService;

import java.util.List;
import java.util.Optional;

@Tag(name = "Orders", description = "All endpoint operations related to Orders")
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
        ResponseEntity response;
        if(customer == null){
            return new ResponseEntity("Customer is unauthorized", HttpStatus.UNAUTHORIZED);
        }
        Optional<Order> order = this.orderService.getOrderById(orderId);
        if(order.isPresent()){
            response = new ResponseEntity(order, HttpStatus.OK);
        } else {
            response = new ResponseEntity("There is no such order associated with the authenticated customer", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Operation(summary = "Cancel a specific order associated to authenticated customer by id", description = "Cancels a specific order based on the path variable id." +
            " The order that is going to be cancelled can only be cancelled if the order status is REGISTERED", responses = {
            @ApiResponse(responseCode = "200", description = "Successful order cancel", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = Product.class))),})
    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal Customer customer, @PathVariable("id") int orderId) {
        ResponseEntity response;
        if(customer == null){
            return new ResponseEntity("Customer needs to be authenticated to access order", HttpStatus.UNAUTHORIZED);
        }
        boolean success = orderService.cancelOrderById(orderId);
        if (success) {
            response = new ResponseEntity("Order with orderId: " + orderId + " was cancelled", HttpStatus.OK);
        } else {
            response = new ResponseEntity("Order was not cancelled", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Operation(summary = "Create a order associated to authenticated customer", description = "Creates a new order associated to the authenticate customer" +
            "  that places the order", responses = {
            @ApiResponse(responseCode = "201", description = "Successful order creation", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = Product.class))),})
    @PostMapping("/createorder")
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal Customer customer, @RequestBody Order order) throws NotEnoughStockException {
        if (customer != null) {
            Order createdOrder = this.orderService.createOrder(order, customer);
            if (createdOrder != null) {
                return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Order was not created", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Customer is not authenticated", HttpStatus.FORBIDDEN);
    }
}