package wms.rest.pms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wms.rest.pms.repository.CategoryRepository;
import wms.rest.pms.model.Category;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public boolean addCategory(Category category) {
        if (category != null) {
            this.categoryRepository.save(category);
            return true;
        } else {
            return false;
        }
    }

    public Iterable<Category> getAll() {
        return this.categoryRepository.findAll();
    }

    public Optional<Category> findById(int id) {
        return this.categoryRepository.findById(id);
    }

    public boolean deleteCategory(int id) {
        if (this.categoryRepository.findById(id).isPresent()) {
            this.categoryRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
