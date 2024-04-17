package wms.rest.wms.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import wms.rest.wms.exception.NotEnoughStockException;
import wms.rest.wms.model.*;
import wms.rest.wms.repository.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for order api controller.
 */
@Service
public class OrderService {

    private OrderRepository orderRepository;

    private ProductRepository productRepository;

    private InventoryRepository inventoryRepository;

    public OrderService(OrderRepository orderRepository
            , ProductRepository productRepository
            , InventoryRepository inventoryRepository){
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public List<Order> getOrders(Customer customer){
        return this.orderRepository.findByCustomer(customer);
    }

    /**
     * Get an Optional object of Order
     *
     * @param orderId the ID of the order to get
     * @return a Optional object of Order. Can contain and Order or not.
     */
    public Optional<Order> getOrderById(int orderId){
        return this.orderRepository.findById(orderId);
    }

    /**
     * Utility method for cancelOrderbyId
     * Checks if an order has the orderStatus as REGISTERED, which is the only
     * status an Order can have for the customer to cancel the order
     *
     * @param order the order to check if has orderStatus as REGISTERED
     * @return returns true if orderStatus is REGISTERED, false otherwise
     */
    public boolean cancelOrder(Order order){
        if(order.getOrderStatus() == OrderStatus.REGISTERED){
            order.setOrderStatus(OrderStatus.CANCELLED);
            return true;
        }
        return false;
    }

    /**
     * Cancels an Order by ID
     *
     * @param orderId the ID of the Order to cancel
     * @return returns true if Order is cancelled, false otherwise
     */
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
     * Creates a new order associated to a customer. Reserves the quantity of each product
     * in inventory under reserved stock.
     *
     * @param order JSON data payload of order.
     * @param customer Authenticated customer.
     * @return saves the order with the orderRepository.
     */
    @Transactional
    public Order createOrder(Order order, Customer customer) throws NotEnoughStockException {
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatus.REGISTERED);
        order.setOrderDate(LocalDate.now());
        order.setStore(customer.getStore());

        for (OrderQuantities quantity : order.getQuantities()) {

            Product product = productRepository.findById(quantity.getProduct().getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + quantity.getProduct().getProductId()));

            // Need to reserve product quantity in stock
            Inventory inventory = product.getInventory();
            int orderedQuantity = quantity.getProductQuantity();
            int availableStock = inventory.getAvailableStock();

            if(orderedQuantity <= availableStock){
                // Reserve stock
                inventory.setReservedStock(inventory.getReservedStock() + orderedQuantity);
                // Subtract reserved from available
                inventory.setAvailableStock(inventory.getAvailableStock() - orderedQuantity);
                inventoryRepository.save(inventory);
            } else {
                throw new NotEnoughStockException("There is not enough stock of product with ID: " + product.getProductId());
            }
            quantity.setProduct(product);
            quantity.setOrder(order);
        }
        return this.orderRepository.save(order);
    }

    /**
     * Fetch all orders that has the OrderStatus as REGISTERED
     *
     * @return a List of Order with orderStatus REGISTERED
     */
    public List<Order> getRegisteredOrders() {
        return this.orderRepository.findAll().stream()
                .filter((order -> order.getOrderStatus() == OrderStatus.REGISTERED))
                .toList();
    }

    /**
     * Fetch all registered orders from getRegisteredOrders and sorts them into a Map of key-value pairs
     * grouped by Store
     *
     * @return a Map of Store and Order. All Orders associated to each Store
     * Output example:
     * [Store 1] -> [Order 1, Order 2]
     * [Store 2] -> [Order 3, Order 4, Order 5]
     * [Store n] -> [Order n, Order n]
     */
    public Map<Store, List<Order>> groupByStore() {
        List<Order> registeredOrders = this.getRegisteredOrders();
        return registeredOrders.stream()
                .filter(order -> order.getCustomer() != null && order.getCustomer().getStore() != null)
                .collect(Collectors.groupingBy(order -> order.getCustomer().getStore()));
    }

    /**
     * Fetch all registered orders from getRegisteredOrders and sorts them into a Map of key-value pairs
     * grouped by both Store and wishedDeliveryDate.
     *
     * @return a Map where each key is a Pair of Store and wishedDeliveryDate, and each value is a list of Orders
     * Output example:
     * [Store 1, 2024-04-15] -> [Order 1, Order 2]
     * [Store 1, 2024-04-16] -> [Order 3, Order 4]
     * [Store 2, 2024-04-16] -> [Order 5, Order 6, Order 7]
     */
    public Map<AbstractMap.SimpleEntry<Store, LocalDate>, List<Order>> groupByStoreAndDeliveryDate() {
        List<Order> registeredOrders = this.getRegisteredOrders();
        return registeredOrders.stream()
                .filter(order -> order.getCustomer() != null && order.getCustomer().getStore() != null)
                .collect(Collectors.groupingBy(order ->
                        new AbstractMap.SimpleEntry<>
                                (order.getCustomer().getStore(), order.getWishedDeliveryDate())));
    }

    /**
     * Updates an Order from orderStatus REGISTERED to PICKING
     * Also set progressInPercent to 10
     *
     * @param order the order to update the orderStatus and progressInPercent
     */
    @Transactional
    public void updateFromRegisteredToPicking(Order order) {
        order.setProgressInPercent(10);
        order.setOrderStatus(OrderStatus.PICKING);
        this.orderRepository.save(order);
    }

    /**
     * Updates an Order from orderStatus PICKING to PICKED
     * Also set progressInPercent to 20
     *
     * @param order the order to update the orderStatus
     */
    @Transactional
    public void updateFromPickingToPicked(Order order) {
        order.setProgressInPercent(20);
        order.setOrderStatus(OrderStatus.PICKED);
        this.orderRepository.save(order);
    }
}