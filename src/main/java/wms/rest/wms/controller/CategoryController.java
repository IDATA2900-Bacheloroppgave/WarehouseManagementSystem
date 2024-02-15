package wms.rest.wms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.rest.wms.model.Category;
import wms.rest.wms.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    /**
     * Constructor.
     *
     * @param categoryService categoryservice.
     */
    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    /**
     * Post mapping for /categories endpoint. Adds a category to the database.
     *
     * @param category the category to add to the database. Requires JSON body in the HTTP POST request.
     * @return responseentity based on the result of the post request.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Category category){
        try{
            boolean added = this.categoryService.addCategory(category);
            if(added){
                return new ResponseEntity<>("Category was added", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Category was not added", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete mapping for /categories endpoint. Deletes a category from the database.
     *
     * @param id the id of the category to delete.
     * @return responsentity based on the result of the delete request.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id){
        boolean deleted = this.categoryService.deleteCategory(id);
        if(deleted){
            return new ResponseEntity<>("Category was deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Category was not deleted", HttpStatus.BAD_REQUEST);
        }
    }
}
