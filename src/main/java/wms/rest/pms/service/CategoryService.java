package wms.rest.pms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wms.rest.pms.repository.CategoryRepository;
import wms.rest.pms.model.Category;

import java.util.Optional;

/**
 * Service class for categoryrepository interface.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Constructor.
     *
     * @param categoryRepository categoryrepository.
     */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Adds a category to the repository.
     *
     * @param category the category to add to the repository.
     * @return true if added, false if not added.
     */
    public boolean addCategory(Category category) {
        if (category != null) {
            this.categoryRepository.save(category);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return all categories in a list.
     *
     * @return true if
     */
    public Iterable<Category> getAll() {
        return this.categoryRepository.findAll();
    }

    /**
     * Returns a category the with suggested ID.
     *
     * @param id the ID of the category to return.
     * @return the category with the suggested ID.
     */
    public Optional<Category> findById(int id) {
        return this.categoryRepository.findById(id);
    }

    /**
     * Deletes a category with the suggested ID.
     *
     * @param id the ID of the category to delete.
     * @return delete the category with the suggested ID.
     */
    public boolean deleteCategory(int id) {
        if (this.categoryRepository.findById(id).isPresent()) {
            this.categoryRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
