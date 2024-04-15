package wms.rest.wms.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wms.rest.wms.model.Store;
import wms.rest.wms.service.StoreService;

import java.util.List;
import java.util.Optional;

@Tag(name = "Shipments", description = "All endpoint operations related to Shipments")
@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    public ResponseEntity<?> getStores() {
        ResponseEntity response;
        List<Store> stores = this.storeService.getStores();
        if(!stores.isEmpty()){
            response = new ResponseEntity(stores, HttpStatus.OK);
        } else {
            response = new ResponseEntity("There is no stores available", HttpStatus.NO_CONTENT);
        }
        return response;
    }

    public ResponseEntity<?> getStoreById(@PathVariable("id") int id) {
        ResponseEntity response;
        Optional<Store> store = this.storeService.getStoreById(id);
        if(!store.isPresent()){
            response = new ResponseEntity(store, HttpStatus.OK);
        } else {
            response = new ResponseEntity("Store with ID: " + id + " does not exist", HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
