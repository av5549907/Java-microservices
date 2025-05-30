package com.service.video.document;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Data
public class Video {

    @Id
    private  String videoId;
    private  String videoTitle;
    private  String videoDesc;
    private String filePath;
    private  String contentType;
    private String courseId;

}