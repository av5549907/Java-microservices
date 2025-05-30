package com.service.category.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.util.Date;


@Getter
@Setter
@Entity
@Data
@Table(name="categories")
public class Category {
    @Id
    private String Id;
    @Size(min=5, max=50, message = "title can not be blank")
    private String title;
    @NotNull(message = "description can not blank")
    @Column(name = "description")
    private String desc;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd/mm/yyyy hh:mm:ss a", timezone = "IST")
    private Date addedDate;
    @Column(name="banner_url")
    private String bannerImageUrl;
//    private String courseId;
//    private  String videoId;

}
