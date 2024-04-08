package wms.rest.wms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.Order;
import wms.rest.wms.model.OrderQuantities;
import wms.rest.wms.model.Product;
import wms.rest.wms.repository.OrderQuantitiesRepository;

import java.util.List;

@Service
public class OrderQuantitiesService {

    @Autowired
    private OrderQuantitiesRepository orderQuantitiesRepository;

    @Autowired
    private OrderService orderService;

    public OrderQuantitiesService(OrderQuantitiesRepository orderQuantitiesRepository, OrderService orderService){
        this.orderQuantitiesRepository = orderQuantitiesRepository;
        this.orderService = orderService;
    }
    public List<OrderQuantities> getOrderQuantities(){
        return this.orderQuantitiesRepository.findAll();
    }
}