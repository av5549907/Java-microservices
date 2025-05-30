package com.service.course.dtos;

import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CourseDto {
    private  String courseId;
    private  String title;
    private  String shortDesc;
    private String longDesc;
    private double price;
    private  boolean live=false;
    private double discount;
    private  String banner;
    private Date createdDate;
    private String categoryId;
    public  String getBannerUrl(){
        return "http://localhost:9092/api/v1/courses/"+courseId+"/banners";
    }
    public  CategoryDto categoryDto;

    public List<VideoDto> videoDto;

}