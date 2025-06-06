package com.service.category.repositories;

import com.service.category.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category,String> {
    List<Category> findByTitleContainingIgnoreCaseOrDescContainingIgnoreCase(String keyword, String keyword1);
}