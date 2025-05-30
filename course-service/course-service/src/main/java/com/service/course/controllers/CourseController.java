package com.service.course.controllers;

import com.service.course.config.AppConstants;
import com.service.course.dtos.*;
import com.service.course.services.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @Transactional
    ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto){
        System.out.println("Created Date : "+courseDto.getCreatedDate());
        return  new ResponseEntity<>(courseService.createCourse(courseDto), HttpStatus.CREATED);
    }
    @GetMapping("/{courseId}")
    ResponseEntity<CourseDto> getCourseById(@PathVariable String courseId){
        System.out.println("Get Course By Id called");
        return  new ResponseEntity<>(courseService.getCourseById(courseId),HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<CourseDto>> getAllCourses(){
        return  new ResponseEntity<>(courseService.getAllCourses(),HttpStatus.OK);
    }

    @PutMapping("/{courseId}")
    ResponseEntity<CourseDto> updateCourse(@RequestBody CourseDto courseDto,@PathVariable String courseId){
        return new ResponseEntity<>(courseService.updateCourse(courseDto,courseId),HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}")
    ResponseEntity<String> deleteCourse(@PathVariable String courseId){
        return new ResponseEntity<>(courseService.deleteCourse(courseId),HttpStatus.OK);
    }

    @GetMapping("/all")
    ResponseEntity<CustomPageResponse> getAll(
            @RequestParam(value="pageNumber",required = false,defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value="pageSize",required = false,defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(value="sortDirection",required = false,defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ){
        return new ResponseEntity<>(courseService.getAll(pageNumber,pageSize,sortBy,sortDirection),HttpStatus.OK);
    }

//    @GetMapping
//    ResponseEntity<Page<CourseDto>> getAllCourses(Pageable pageable) {
//
//        return new ResponseEntity<>(courseService.getAllCourses(pageable), HttpStatus.OK);
//    }
//
//    @Operation(summary = "Update Course",
//            description = "Update the course for the given course id",
//            tags = {"course operation"}
//    )
//    @ApiResponse(responseCode = "201",description = "Course Update Success")
//    @ApiResponse(responseCode = "501",description = "Internal Server Error")
//    @PutMapping("/{courseId}")
//    ResponseEntity<CourseDto> updateCourseDetails(@Valid @RequestBody CourseDto courseDto, @PathVariable String courseId) {
//        return new ResponseEntity<>(courseService.updateCourseDetails(courseDto, courseId), HttpStatus.OK);
//    }


    @PostMapping("/{courseId}/banners")
    ResponseEntity<?> uploadBanner(
            @PathVariable String courseId,
            @RequestParam("banner") MultipartFile banner
    ) throws IOException {
        String bannerType = banner.getContentType();
        System.out.println("banner type "+bannerType);
        System.out.println(!bannerType.equalsIgnoreCase("image/jpeg"));
        System.out.println(!bannerType.equalsIgnoreCase("image/png"));
        System.out.println( bannerType != null);

//        boolean checktype=bannerType != null && (!bannerType.equalsIgnoreCase("image/png") || !bannerType.equalsIgnoreCase("image/jpeg"));
//         System.out.println("CheckType: "+checktype);
//        if (checktype) {
//            CustomMessage customMessage = new CustomMessage();
//            System.out.println("content error");
//            customMessage.setMessage("Invalid file !!");
//            customMessage.setSuccess(false);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customMessage);
//        }
        System.out.println(banner.getContentType());
        System.out.println(banner.getOriginalFilename());
        System.out.println(banner.getName());
        System.out.println(banner.getSize());

        return ResponseEntity.ok(courseService.saveBanner(banner, courseId));
    }

    @GetMapping("/{courseId}/banners")
    ResponseEntity<Resource> serveBanner(
            @PathVariable String courseId,
            HttpServletRequest request
    ) {
        System.out.println(request.getContextPath());
        Enumeration<String> head = request.getHeaderNames();
        while (head.hasMoreElements()) {
            String header = head.nextElement();
            System.out.println(header + " : " + request.getHeader(header));
        }
        ResourceContentType resourceContentType = courseService.getCourseBannerById(courseId);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(resourceContentType.getContentType())).body(resourceContentType.getResource());
    }

    @GetMapping("/category/{courseId}")
    ResponseEntity<CategoryDto> getCategoryOfCourse(@PathVariable String courseId){
        return new  ResponseEntity<>(courseService.getCategoryOfCourse(courseId),HttpStatus.OK);
    }
}