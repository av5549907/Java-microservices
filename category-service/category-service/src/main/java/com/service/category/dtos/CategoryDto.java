package com.service.category.dtos;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class CategoryDto {
    private String Id;
    private String title;
    @Column(name = "description")
    private String desc;
    private Date addedDate;
    @Column(name="banner_url")
    private String bannerImageUrl;
//    private String courseId;
//    private String videoId;
//    private CourseDto courseDto;
//    private  VideoDto videoDto;


}