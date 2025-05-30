package com.service.video.dtos;

import lombok.Data;

@Data
public class VideoDto {
    private  String videoId;
    private  String videoTitle;
    private  String videoDesc;
    private String filePath;
    private  String contentType;
    private String courseId;
}