package com.service.course.services;

import com.service.course.dtos.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface CourseService {
    CourseDto createCourse(CourseDto courseDto);

    CourseDto getCourseById(String courseId);

    List<CourseDto> getAllCourses();

    CourseDto updateCourse(CourseDto courseDto,String courseId);

    String deleteCourse(String courseId);

    CustomPageResponse getAll(int pageNumber, int pageSize, String sortBy, String sortDirection);
    CourseDto saveBanner(MultipartFile file, String courseId) throws IOException;


    ResourceContentType getCourseBannerById(String courseId);

    CategoryDto getCategoryOfCourse(String categoryId);

    List<CourseDto> getCoursesOfCategory(String categoryId);
    List<VideoDto> getVideosOfCourse(String courseId);

}