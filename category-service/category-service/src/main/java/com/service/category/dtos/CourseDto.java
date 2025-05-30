package com.service.category.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CourseDto {
    private String courseId;
    private String title;
    private String shortDesc;
    private String longDesc;
    private double price;
    private boolean live = false;
    private double discount;
    private String banner;
    private Date createdDate;
    @JsonProperty("long_description")
    private String bannerContentType;
    private String categoryId;


}
