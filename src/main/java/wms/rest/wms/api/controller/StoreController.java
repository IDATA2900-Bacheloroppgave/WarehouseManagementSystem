package wms.rest.wms.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wms.rest.wms.model.Store;
import wms.rest.wms.service.StoreService;

import java.util.List;
import java.util.Optional;

/**
 * Controller class containing all endpoints related to Stores.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Tag(name = "Stores", description = "All endpoint operations related to Stores")
@RestController
@AllArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    /** Service for handling Store service operations */
    private final StoreService storeService;

    @Operation(summary = "Get a list of all stores", description = "Returns a list of all stores", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Store.class))),
            @ApiResponse(responseCode = "204", description = "No content"),})
    @GetMapping
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

    @Operation(summary = "Get a specific store by id", description = "Returns a specific store by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = Store.class))),
            @ApiResponse(responseCode = "400", description = "No content"),})
    @GetMapping("{id}")
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
