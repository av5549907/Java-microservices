package com.service.video.services;

import com.service.video.dtos.VideoDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface VideoService {

    public VideoDto createVideo(VideoDto videoDto);
    List<VideoDto> getAllVideo();
    Page<VideoDto> getAllVideos(Pageable pageable);
    VideoDto getVideoById(String videoId);
    VideoDto updateVideo(VideoDto videoDto,String videoId);
    String deleteVideo(String videoId);
    List<VideoDto> searchVideo(String keyword);

    List<VideoDto> getVideoOfCourse(String courseId);
    List<VideoDto> searchVideoByKeyword(String keyword);
    VideoDto saveVideoFile(MultipartFile file, String videoId) throws IOException;

}