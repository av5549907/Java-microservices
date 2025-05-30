package com.service.course.serviceimpl;

import com.service.course.config.AppConstants;
import com.service.course.dtos.*;
import com.service.course.entities.Course;
import com.service.course.exceptions.ResourceNotFoundException;
import com.service.course.repositories.CourseRepo;
import com.service.course.services.CourseService;
import com.service.course.services.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    CourseRepo courseRepo;
    ModelMapper modelMapper;
    FileService fileService;

    RestTemplate restTemplate;

    WebClient.Builder webClient;

    public CourseServiceImpl(CourseRepo courseRepo, ModelMapper modelMapper, FileService fileService, RestTemplate restTemplate, WebClient.Builder webClient) {
        this.courseRepo = courseRepo;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.restTemplate = restTemplate;
        this.webClient = webClient;
    }



    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        String id = UUID.randomUUID().toString();
        courseDto.setCourseId(id);
        courseDto.setCreatedDate(new Date());
        Course course = modelMapper.map(courseDto, Course.class);
        Course savedCourse = courseRepo.save(course);
        return modelMapper.map(savedCourse, CourseDto.class);
    }

    @Override
    public CourseDto getCourseById(String courseId) {
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course Not found"));
        CourseDto courseDto=modelMapper.map(course, CourseDto.class);
        if (course.getCategoryId() != null) {
            CategoryDto categoryDto= restTemplate.getForObject(AppConstants.CATEGORY_SERVICE_URL+course.getCategoryId(), CategoryDto.class);
            courseDto.setCategoryId(course.getCategoryId());
            categoryDto.setBannerImageUrl(courseDto.getBannerUrl());
            courseDto.setCategoryDto(getCategoryOfCourse(courseDto.getCategoryId()));
        }

        courseDto.setVideoDto(getVideosOfCourse(course.getCourseId()));
        System.out.println("Video Service executed");
        return courseDto;
    }

    @Override
    public List<CourseDto> getAllCourses() {
        List<CourseDto> courseDtos= courseRepo.findAll().stream().map(course -> modelMapper.map(course, CourseDto.class)).collect(Collectors.toList());
        courseDtos.stream()
                .map(courseDto -> {
                    if(courseDto.getCategoryId()!=null) {
                        System.out.println(""+courseDto.getCategoryId());
                        CategoryDto categoryDto = restTemplate.getForObject(AppConstants.CATEGORY_SERVICE_URL + courseDto.getCategoryId(), CategoryDto.class);
                        categoryDto.setBannerImageUrl(courseDto.getBannerUrl());
                        courseDto.setCategoryDto(getCategoryOfCourse(courseDto.getCategoryId()));
                        courseDto.setCategoryDto(categoryDto);
                    }
                    return courseDto;
                })
                .collect(Collectors.toList());
        courseDtos.stream().forEach(courseDto -> courseDto.setVideoDto(getVideosOfCourse(courseDto.getCourseId())));
        return courseDtos;
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto, String courseId) {
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course Not found"));
        course.setCourseId(courseId);
        course.setCreatedDate(courseDto.getCreatedDate());
        course.setTitle(courseDto.getTitle());
        course.setLive(true);
        course.setBanner(courseDto.getBanner());
        course.setDiscount(courseDto.getDiscount());
//      course.setBannerContentType(courseDto.getBannerContentType());
        course.setLongDesc(courseDto.getLongDesc());
        course.setShortDesc(courseDto.getShortDesc());
        course.setPrice(courseDto.getPrice());
        course.setBannerContentType(getCourseBannerById(courseDto.getCourseId()).getContentType());
        Course savedCourse = courseRepo.save(course);
        return modelMapper.map(savedCourse, CourseDto.class);
    }

    @Override
    public String deleteCourse(String courseId) {
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course Not found"));
        courseRepo.delete(course);
        return "Course with " + courseId + " is deleted";
    }

    public CustomPageResponse getAll(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        //  Sort sort= Sort.by(sortBy);
        Sort sort = Sort.by(sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Course> categoryPage = courseRepo.findAll(pageRequest);
        List<Course> all = categoryPage.getContent();
        List<CourseDto> categoryDtoList = all.stream().map(cat -> modelMapper.map(cat, CourseDto.class)).collect(Collectors.toList());
        CustomPageResponse<CourseDto> customPageResponse = new CustomPageResponse<CourseDto>();
        customPageResponse.setContent(categoryDtoList);
        customPageResponse.setLast(categoryPage.isLast());
        customPageResponse.setTotalElements(categoryPage.getTotalElements());
        customPageResponse.setTotalPages(categoryPage.getTotalPages());
        customPageResponse.setPageSize(categoryPage.getSize());
        customPageResponse.setPageNumber(pageNumber);
        return customPageResponse;
    }
    @Override
    public CourseDto saveBanner(MultipartFile file, String courseId) throws IOException {
        Course course=courseRepo.findById(courseId).orElseThrow(()->new ResourceNotFoundException("Course not found!!"));
        String filePath=fileService.save(file, AppConstants.COURSE_BANNER_UPLOAD_DIR,file.getOriginalFilename());
        course.setBanner(filePath);
        course.setBannerContentType(file.getContentType());
        Course savedCourse= courseRepo.save(course);
        return modelMapper.map(savedCourse,CourseDto.class);
    }

    @Override
    public ResourceContentType getCourseBannerById(String courseId) {
        Course course=courseRepo.findById(courseId).orElseThrow(()->new ResourceNotFoundException("Course not found !!"));
        String bannerPath=course.getBanner();
        Path path= Paths.get(bannerPath);
        Resource resource=new FileSystemResource(path);
        ResourceContentType resourceContentType=new ResourceContentType();
        resourceContentType.setResource(resource);
        resourceContentType.setContentType(course.getBannerContentType());
        return resourceContentType;
    }

    @Override
    public CategoryDto getCategoryOfCourse(String categoryId) {
        try {
            ResponseEntity<CategoryDto> exchange = restTemplate.exchange(AppConstants.CATEGORY_SERVICE_URL + categoryId, HttpMethod.GET, null, CategoryDto.class);

            HttpEntity<CategoryDto> categoryDtoHttpEntity = new HttpEntity<>(
                    new CategoryDto()
            );


            return exchange.getBody();
        } catch (HttpClientErrorException ex) {
//            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CourseDto> getCoursesOfCategory(String categoryId){
        List<Course> courseDtos=courseRepo.findByCategoryId(categoryId);
       return courseDtos.stream().map(x->modelMapper.map(x,CourseDto.class)).collect(Collectors.toList());

    }

    @Override
    public List<VideoDto> getVideosOfCourse(String courseId) {
        return webClient.build().get()
                .uri(AppConstants.VIDEO_SERVICE_URL+"/course/{courseId}",courseId)
                .retrieve()
                .bodyToFlux(VideoDto.class)
                .collectList()
                .block();
    }

}