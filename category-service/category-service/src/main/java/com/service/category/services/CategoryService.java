package com.service.category.services;

import com.service.category.dtos.CategoryDto;
import com.service.category.dtos.CourseDto;
import com.service.category.dtos.CustomPageResponse;
import com.service.category.entities.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(String categoryId);
    String deleteCategory(String categoryId);
    CategoryDto updateCategory(CategoryDto categoryDto,String categoryId);

    List<CategoryDto> searchCategory(String keyword);
    CustomPageResponse getAll(int pageNumber, int pageSize, String sortBy, String sortDirection);
    CategoryDto createCategory(CategoryDto categoryDto);
//    CourseDto getCourseOfCategory(String courseId);

}