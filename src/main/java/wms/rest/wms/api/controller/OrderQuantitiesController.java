package wms.rest.wms.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wms.rest.wms.model.OrderQuantities;
import wms.rest.wms.service.OrderQuantitiesService;

@RestController
@RequestMapping("/api/orderquantities")
public class OrderQuantitiesController {

    private OrderQuantitiesService orderQuantitiesService;

}
