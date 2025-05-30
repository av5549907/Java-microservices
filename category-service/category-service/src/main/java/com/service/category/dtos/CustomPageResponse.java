package com.service.category.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPageResponse<T>{

    private  int pageSize;
    private int pageNumber;
    private  long totalElements;
    private  int totalPages;
    private boolean isLast;
    private List<T> content;
}
