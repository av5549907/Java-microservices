package com.service.video.serviceimpl;

import com.service.video.document.AppConstants;
import com.service.video.document.Video;
import com.service.video.dtos.VideoDto;
import com.service.video.exceptions.ResourceNotFoundException;
import com.service.video.repositories.VideoRepo;
import com.service.video.services.FileService;
import com.service.video.services.VideoService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService {

    VideoRepo videoRepo;
    ModelMapper modelMapper;

    FileService fileService;

    public VideoServiceImpl(VideoRepo videoRepo, ModelMapper modelMapper, FileService fileService) {
        this.videoRepo = videoRepo;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }


    @Override
    public VideoDto createVideo(VideoDto videoDto) {
        String id = UUID.randomUUID().toString();
        videoDto.setVideoId(id);
        System.out.println(videoDto.getVideoTitle() + " " + videoDto.getVideoDesc());
        Video video = modelMapper.map(videoDto, Video.class);
        Video savedVideo = videoRepo.save(video);
        return modelMapper.map(savedVideo, VideoDto.class);
    }

    @Override
    public List<VideoDto> getAllVideo() {
        List<Video> videos = videoRepo.findAll();
        return videos.stream().map(x -> modelMapper.map(x, VideoDto.class)).collect(Collectors.toList());
    }

    @Override
    public Page<VideoDto> getAllVideos(Pageable pageable) {
        Page<Video> videos = videoRepo.findAll(pageable);
        List<VideoDto> dtos = videos.getContent()
                .stream()
                .map(video -> modelMapper.map(video, VideoDto.class))
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, videos.getTotalElements());
    }

    @Override
    public VideoDto getVideoById(String videoId) {
        Video video = videoRepo.findById(videoId).orElseThrow(() -> new ResourceNotFoundException("video not found"));
        return modelMapper.map(video, VideoDto.class);
    }

    @Override
    public VideoDto updateVideo(VideoDto videoDto, String videoId) {
        Video video = videoRepo.findById(videoId).orElseThrow(() -> new ResourceNotFoundException("video not found"));
        video.setVideoId(videoDto.getVideoId());
        video.setVideoDesc(videoDto.getVideoDesc());
        video.setVideoTitle(videoDto.getVideoTitle());
        video.setFilePath(videoDto.getFilePath());
        video.setContentType(videoDto.getContentType());
        Video saved = videoRepo.save(video);
        return modelMapper.map(saved, VideoDto.class);
    }

    @Override
    public String deleteVideo(String videoId) {
        Video video = videoRepo.findById(videoId).orElseThrow(() -> new ResourceNotFoundException("video not found"));
        videoRepo.delete(video);
        return "Video with " + videoId + " deleted";
    }

    @Override
    public List<VideoDto> searchVideo(String keyword) {
        List<Video> videos = videoRepo.findByVideoTitleContainingIgnoreCaseOrVideoDescContainingIgnoreCase(keyword, keyword);
        List<VideoDto> videoDtos = videos.stream().map(video -> modelMapper.map(video, VideoDto.class)).collect(Collectors.toList());
        return videoDtos;
    }

    @Override
    public List<VideoDto> getVideoOfCourse(String courseId) {
        return videoRepo.findByCourseId(courseId).stream().map(video -> modelMapper.map(video, VideoDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<VideoDto> searchVideoByKeyword(String keyword) {
        return videoRepo.findByVideoTitleContainingIgnoreCaseOrVideoDescContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(video -> modelMapper.map(video, VideoDto.class)).collect(Collectors.toList());
    }

    @Override
    public VideoDto saveVideoFile(MultipartFile file, String videoId) throws IOException {
        Video video = videoRepo.findById(videoId).orElseThrow(() -> new ResourceNotFoundException("Video not found !!"));
        String filePath = fileService.save(file, AppConstants.COURSE_VIDEO_UPLOAD_DIR, file.getOriginalFilename());
        video.setFilePath(filePath);
        video.setContentType(file.getContentType());
        Video savedVideo = videoRepo.save(video);
        return modelMapper.map(savedVideo, VideoDto.class);

    }


}