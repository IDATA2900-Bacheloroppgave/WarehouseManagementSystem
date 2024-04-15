package wms.rest.wms.service;

import org.springframework.stereotype.Service;
import wms.rest.wms.model.Store;
import wms.rest.wms.repository.StoreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {

    private StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<Store> getStores() {
        return this.storeRepository.findAll();
    }

    public Optional<Store> getStoreById(int id) {
        return this.storeRepository.findById(id);
    }
}
