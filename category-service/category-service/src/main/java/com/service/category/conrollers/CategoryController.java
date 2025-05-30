package com.service.category.conrollers;

import com.service.category.config.AppConstants;
import com.service.category.dtos.CategoryDto;
import com.service.category.dtos.CourseDto;
import com.service.category.dtos.CustomPageResponse;
import com.service.category.services.CategoryService;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    ResponseEntity<List<CategoryDto>> getAllCategories(){
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        System.out.println("title : "+categoryDto.getTitle());
        return  new ResponseEntity<>(categoryService.createCategory(categoryDto),HttpStatus.CREATED);
    }
    @GetMapping("/{categoryId}")
    ResponseEntity<CategoryDto> getCategoryId(@PathVariable String categoryId){
        return  new ResponseEntity<>(categoryService.getCategoryById(categoryId),HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    ResponseEntity<String> deleteCategory(@PathVariable String categoryId){
        return  new ResponseEntity<>(categoryService.deleteCategory(categoryId),HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,@PathVariable String categoryId){
        return new ResponseEntity<>(categoryService.updateCategory(categoryDto,categoryId),HttpStatus.OK);
    }
    @GetMapping("/search/{keword}")
    ResponseEntity<List<CategoryDto>> searchById(@RequestParam String keyword){
        return  new ResponseEntity<>(categoryService.searchCategory(keyword),HttpStatus.OK);
    }
    @GetMapping("/all")
    ResponseEntity<CustomPageResponse<CategoryDto>> getAll(
            @RequestParam(value="pageNumber",required = false,defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value="pageSize",required = false,defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(value="sortDirection",required = false,defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ){

        return new  ResponseEntity<>(this.categoryService.getAll(pageNumber,pageSize,sortBy,sortDirection),HttpStatus.OK);
    }

//    @GetMapping("/course/{categoryId}")
//    ResponseEntity<CourseDto> getCourseOfCategory(@PathVariable String categoryId){
//        return new  ResponseEntity<>(categoryService.getCourseOfCategory(categoryId),HttpStatus.OK);
//    }
}