package wms.rest.wms.service;

import org.springframework.stereotype.Service;
import wms.rest.wms.model.Store;
import wms.rest.wms.repository.StoreRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service class for API Store controller
 */
@Service
public class StoreService {

    private StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * Return a List of all Stores
     *
     * @return a List of all Stores
     */
    public List<Store> getStores() {
        return this.storeRepository.findAll();
    }

    /**
     * Get an Optional object of Store
     *
     * @param storeId the ID of the Store to get
     * @return an Optional object of the Store. Can contain a Store or not.
     */
    public Optional<Store> getStoreById(int storeId) {
        return this.storeRepository.findById(storeId);
    }
}