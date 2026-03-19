package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payLoad.CategoryDTO;
import com.ecommerce.project.payLoad.CategoryResponse;
import com.ecommerce.project.service.CategoryServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Categorycontroller {

    @Autowired
    private CategoryServiceImplementation categories;


    //Create
    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO category) {

        CategoryDTO Created = categories.createCategories(category);
        return new ResponseEntity<>(Created, HttpStatus.CREATED);
    }


    //GetAll
    @GetMapping("/public/categories")
//    @RequestMapping(value ="\"/public/categories" , method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getCategory(@RequestParam(name = """
                                                                pageNumber""", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber, @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy, @RequestParam(name = "sortOrder", defaultValue = AppConstants.DIR, required = false) String sortOrder) {

        CategoryResponse allCategories = categories.getAllCategories(pageNumber, pageSize , sortBy , sortOrder);
        return new ResponseEntity<>(allCategories, HttpStatus.CREATED);

    }


    //GetById
    @GetMapping("/public/categories/{id}")
//    @RequestMapping(value ="\"/public/categories" , method = RequestMethod.GET)
    public ResponseEntity<CategoryDTO> getCategoryByid(@PathVariable Long id) {


        CategoryDTO IdData = categories.getCategoriesById(id);

        return new ResponseEntity<>(IdData, HttpStatus.CREATED);

    }


    //Update
    @PutMapping("/adimin/categories/{cateoryId}")
    public ResponseEntity<CategoryDTO> UpdateCategory(@RequestBody CategoryDTO category
            , @PathVariable long cateoryId) {
        CategoryDTO category1 = categories.UpdateCategory(category, cateoryId);
        return new ResponseEntity<>(category1, HttpStatus.OK);

    }


    //    DeleteById
    @DeleteMapping("/adimin/categories/{id}")
    public ResponseEntity<CategoryDTO> DeleteCategory(@PathVariable Long id) {
        CategoryDTO deleteCategory = categories.deleteCategory(id);

        return new ResponseEntity<>(deleteCategory, HttpStatus.OK);

    }
}
