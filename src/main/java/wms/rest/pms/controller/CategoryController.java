package wms.rest.pms.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.rest.pms.model.Category;
import wms.rest.pms.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Category>> getCategories(){
        Iterable<Category> categories = this.categoryService.getAll();
        return ResponseEntity.ok(categories);
    }

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id){
        boolean deleted = this.categoryService.deleteCategory(id);
        if(deleted){
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not deleted", HttpStatus.BAD_REQUEST);
        }
    }
}
