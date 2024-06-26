package wms.rest.wms.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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

/**
 * Controller class containing all endpoints related to Orders.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Tag(name = "Orders", description = "All endpoint operations related to Orders")
@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    /** Service for handling Order service operations */
    private final OrderService orderService;

    @Operation(summary = "Get a list of all orders associated to authenticated customer", description = "Returns a list of all orders associated to authenticated customer", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Order.class))),})
    @GetMapping
    public List<Order> getOrders(@AuthenticationPrincipal Customer user){
        return this.orderService.getOrders(user);
    }

    @Operation(summary = "Get a specific order associated to authenticated customer by id", description = "Returns a specific order based on the path variable id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = Order.class))),})
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
            @ApiResponse(responseCode = "200", description = "Successful order cancel", content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = Order.class))),})
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

    @Operation(summary = "Get the current location of an Order", description = "Get the current location of an Order by OrderId", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = Order.class))),})
    @GetMapping("/currentlocation/{id}")
    public ResponseEntity<?> getCurrentLocation(@PathVariable("id") int orderId) {
        ResponseEntity response;
        String currentLocation = this.orderService.getCurrentLocation(orderId);
        if(currentLocation != null) {
            response = new ResponseEntity(currentLocation, HttpStatus.OK);
        } else {
            response = new ResponseEntity("Could not find Order with ID: " + orderId, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Operation(summary = "Get the progress in percent of an Order", description = "Get the progress in percent of an Order by OrderId", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = Order.class))),})
    @GetMapping("/progressinpercent/{id}")
    public ResponseEntity<?> getProgressInPercent(@PathVariable("id") int orderId) {
        ResponseEntity response;
        try {
            int progress = orderService.getProgressInPercent(orderId);
            if (progress >= 0) {
                return ResponseEntity.ok(progress);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while retrieving the order progress.");
        }
    }

    @Operation(summary = "Create a order associated to authenticated customer", description = "Creates a new order associated to the authenticate customer" +
            "  that places the order", responses = {
            @ApiResponse(responseCode = "201", description = "Successful order creation", content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = Order.class))),})
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