package com.service.category;

import com.service.category.dtos.CategoryDto;
import com.service.category.entities.Category;
import com.service.category.repositories.CategoryRepo;
import com.service.category.services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CategoryServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Mock
	ModelMapper modelMapper;
	@Mock
	CategoryRepo categoryRepo;
	@Mock
	Category category;
	//@Mock
	@InjectMocks
	CategoryService categoryService;
	@Test
	public void createCategorySuccess(){
		CategoryDto categoryDto=new CategoryDto();
		categoryDto.setId(UUID.randomUUID().toString());
		Category mockCate= (Category) Mockito.when(categoryRepo.save(category)).thenReturn(category);
        CategoryDto addedCat= categoryService.createCategory(categoryDto);
		Assertions.assertEquals(mockCate.getId(),addedCat.getId());
	}
}
