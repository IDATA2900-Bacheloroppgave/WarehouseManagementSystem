package wms.rest.wms.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wms.rest.wms.model.Customer;
import wms.rest.wms.service.CustomerService;

import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable("customerId") int customerId) {
        ResponseEntity response;
        Optional<Customer> customer = this.customerService.getCustomerById(customerId);
        if(customer.isPresent()) {
            response = new ResponseEntity(customer, HttpStatus.OK);
        } else {
            response = new ResponseEntity("No such Customer with customerId: " + customer, HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
