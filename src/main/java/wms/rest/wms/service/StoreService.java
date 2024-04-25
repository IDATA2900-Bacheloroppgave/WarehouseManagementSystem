package wms.rest.wms.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import wms.rest.wms.model.Store;
import wms.rest.wms.repository.StoreRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service class for API Store controller.
 *
 * @author Mikkel Stavelie.
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class StoreService {

    /** Repository for handling Store persistence operations */
    private StoreRepository storeRepository;

    /**
     * Return a List of all Stores.
     *
     * @return a List of all Stores.
     */
    public List<Store> getStores() {
        return this.storeRepository.findAll();
    }

    /**
     * Return an Optional object of Store by storeId.
     *
     * @param storeId the storeId of the Store to return.
     * @return an Optional object of the Store, can be present or not.
     */
    public Optional<Store> getStoreById(int storeId) {
        return this.storeRepository.findById(storeId);
    }
}