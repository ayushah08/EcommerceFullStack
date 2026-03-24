package com.ecommerce.project.service;

import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payLoad.CategoryDTO;
import com.ecommerce.project.payLoad.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    //ToCreate
    @Override
    public CategoryDTO createCategories(CategoryDTO category) {
        Category category1 = modelMapper.map(category, Category.class);
        Category findCategory = categoryRepository.findByCategoryName(category.getCategoryName());

        if (findCategory != null) {
            throw new ApiException("Category with " + category.getCategoryName() + " already Exists !!!!");
        }

        categoryRepository.save(category1);


        return modelMapper.map(category1, CategoryDTO.class);
    }


    //ToGetAll

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String order
    ) {

        if (!List.of("categoryId", "categoryName").contains(sortBy)) {
            sortBy = "categoryId";
        }
        Sort sort = order.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);


        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty())
            throw new ApiException("No category created till now.");

        List<CategoryDTO> categoryDTO = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTO);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;

    }


    //GetById
    public CategoryDTO getCategoriesById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException("No Data Found With id " + id));
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        return categoryDTO;
    }


    //ToUpdate
    @Override
    public CategoryDTO UpdateCategory(CategoryDTO categorydto, Long id) {
        Category category = modelMapper.map(categorydto, Category.class);

        Category SavedCategory = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", id));
        category.setCategoryId(id);
        SavedCategory = categoryRepository.save(category);
        return modelMapper.map(SavedCategory, CategoryDTO.class);
    }


    //ToDelete
    @Override
    public CategoryDTO deleteCategory(Long id) {


        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", id));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }


}


