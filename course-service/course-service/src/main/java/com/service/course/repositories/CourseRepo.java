package com.service.course.repositories;

import com.service.course.dtos.CategoryDto;
import com.service.course.dtos.CourseDto;
import com.service.course.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<Course,String> {
//    List<Course> findBannerById(String courseId);
    List<Course> findByCategoryId(String categoryId);

}