package com.service.category.serviceimpl;

import com.service.category.config.AppConstants;
import com.service.category.dtos.CategoryDto;
import com.service.category.dtos.CourseDto;
import com.service.category.dtos.CustomPageResponse;
import com.service.category.dtos.VideoDto;
import com.service.category.entities.Category;
import com.service.category.exceptions.ResourceNotFoundException;
import com.service.category.repositories.CategoryRepo;
import com.service.category.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    CategoryRepo categoryRepo;
    ModelMapper modelMapper;
    RestTemplate restTemplate;

    public CategoryServiceImpl(CategoryRepo categoryRepo, ModelMapper modelMapper, RestTemplate restTemplate) {
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> category = categoryRepo.findAll();
        List<CategoryDto> categoryDtos= category.stream().map(cat -> modelMapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
//        categoryDtos.stream()
//                .map(categoryDto -> {
//                    if(categoryDto.getCourseId()!=null) {
//                        System.out.println(""+categoryDto.getCourseId());
//                        CourseDto courseDto = restTemplate.getForObject(AppConstants.COURSE_SERVICE_URL + categoryDto.getCourseId(), CourseDto.class);
//                        categoryDto.setBannerImageUrl(courseDto.getBanner());
//                        categoryDto.setCourseDto(courseDto);
//                    }
//                    return categoryDto;
//                })
//                .collect(Collectors.toList());
//        categoryDtos.stream()
//                .map(categoryDto -> {
//                    if(categoryDto.getVideoId()!=null) {
//                        System.out.println(""+categoryDto.getCourseId());
//                        VideoDto videoDto = restTemplate.getForObject(AppConstants.VIDEO_SERVICE_URL + categoryDto.getVideoId(), VideoDto.class);
////                        categoryDto.setBannerImageUrl(courseDto.getBanner());
//                        categoryDto.setVideoDto(videoDto);
//                    }
//                    return categoryDto;
//                })
//                .collect(Collectors.toList());
        return categoryDtos;
    }

    @Override
    public CategoryDto getCategoryById(String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found"));
        CategoryDto categoryDto= modelMapper.map(category, CategoryDto.class);
//            CourseDto courseDto = restTemplate.getForObject(AppConstants.COURSE_SERVICE_URL + categoryDto.getCourseId(), CourseDto.class);
//            categoryDto.setCourseDto(courseDto);
//        CourseDto courseDto=getCourseOfCategory(categoryId);
//        categoryDto.setCourseDto(courseDto);
        return categoryDto;
    }

    @Override
    public String deleteCategory(String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found"));
        categoryRepo.delete(category);
        return "Category with " + categoryId + " is deleted";
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found"));
        category.setId(categoryDto.getId());
        category.setDesc(categoryDto.getDesc());
        category.setTitle(categoryDto.getTitle());
        category.setAddedDate(categoryDto.getAddedDate());
        category.setBannerImageUrl(categoryDto.getBannerImageUrl());
        Category savedCategory=categoryRepo.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        List<Category> categories=categoryRepo.findByTitleContainingIgnoreCaseOrDescContainingIgnoreCase(keyword,keyword);
        return categories.stream().map(cat->modelMapper.map(cat,CategoryDto.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        String Id=UUID.randomUUID().toString();
        categoryDto.setId(Id);
        categoryDto.setAddedDate(new Date());
        Category category=modelMapper.map(categoryDto,Category.class);
        categoryRepo.save(category);
        return modelMapper.map(category,CategoryDto.class);
    }


    @Override
    public CustomPageResponse getAll(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        //  Sort sort= Sort.by(sortBy);
        Sort sort=Sort.by(sortBy);
        PageRequest pageRequest=PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> categoryPage=categoryRepo.findAll(pageRequest);
        List<Category> all=categoryPage.getContent();
        List<CategoryDto> categoryDtoList=all.stream().map(cat->modelMapper.map(cat,CategoryDto.class)).collect(Collectors.toList());
        CustomPageResponse<CategoryDto> customPageResponse=new CustomPageResponse<CategoryDto>();
        customPageResponse.setContent(categoryDtoList);
        customPageResponse.setLast(categoryPage.isLast());
        customPageResponse.setTotalElements(categoryPage.getTotalElements());
        customPageResponse.setTotalPages(categoryPage.getTotalPages());
        customPageResponse.setPageSize(categoryPage.getSize());
        customPageResponse.setPageNumber(pageNumber);
        return customPageResponse;
    }
//    @Override
//    public CourseDto getCourseOfCategory(String categoryId) {
//        Category category=categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category Not found"));
//        CategoryDto courseDto=modelMapper.map(category,CategoryDto.class);
//        ResponseEntity<CourseDto> exchange=  restTemplate.exchange(
//                AppConstants.COURSE_SERVICE_URL+category.getCourseId(),
//                HttpMethod.GET,
//                null,
//                CourseDto.class
//        );
//        System.out.println(exchange.getStatusCode());
//        System.out.println(exchange.getBody());
//        if(category.getCourseId()!=null)
//            return restTemplate.getForObject(AppConstants.COURSE_SERVICE_URL+category.getCourseId(),CourseDto.class);
//        return null;
//    }

}
