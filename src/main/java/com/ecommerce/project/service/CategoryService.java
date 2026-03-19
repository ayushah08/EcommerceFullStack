package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payLoad.CategoryDTO;
import com.ecommerce.project.payLoad.CategoryResponse;


public interface CategoryService {

    CategoryDTO getCategoriesById(Long id);

    CategoryResponse getAllCategories(Integer  pageNumber , Integer pageSize ,  String sortBy , String order);

    CategoryDTO createCategories(CategoryDTO category);

    CategoryDTO deleteCategory(Long id);


    CategoryDTO UpdateCategory(CategoryDTO category, Long id);
}
