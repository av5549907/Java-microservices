package com.service.video.controller;


import com.service.video.dtos.VideoDto;
import com.service.video.dtos.VideoUploadResponse;
import com.service.video.services.VideoService;
import com.service.video.services.VideoUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    VideoService videoService;
    @Autowired
    private VideoUploadService videoUploadService;

    private Logger logger= LoggerFactory.getLogger(VideoController.class);

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    ResponseEntity<VideoDto> createVideo(@RequestBody VideoDto videoDto){
        System.out.println("title : "+videoDto.getVideoTitle());
        return new ResponseEntity<>(videoService.createVideo(videoDto), HttpStatus.CREATED);
    }

    @GetMapping("/{videoId}")
    ResponseEntity< VideoDto> getVideoById(@PathVariable String videoId){
        return  new ResponseEntity<>(videoService.getVideoById(videoId),HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Page<VideoDto>> getAllVideos(Pageable pageable) {
        return ResponseEntity.ok(videoService.getAllVideos(pageable));
    }
    @PutMapping("/{videoId}")
    ResponseEntity< VideoDto> updateVideo(@RequestBody VideoDto videoDto,@PathVariable String videoId){
        return  new ResponseEntity<>(videoService.updateVideo(videoDto,videoId),HttpStatus.OK);
    }
    @GetMapping("/course/{courseId}")
    ResponseEntity<List<VideoDto>> getVideoOfCourse(@PathVariable String courseId){
        return  new ResponseEntity<>(videoService.getVideoOfCourse(courseId),HttpStatus.OK);
    }

    @DeleteMapping("/{videoId}")
    ResponseEntity<String> deleteVideo(@PathVariable String videoId){
        return new ResponseEntity<>(videoService.deleteVideo(videoId),HttpStatus.OK);
    }

    @GetMapping("/search")
    ResponseEntity<List<VideoDto>> searchVideo(
            @RequestParam(value = "keyWord",required = false) String keyWord
    ){
        return new ResponseEntity<>(videoService.searchVideoByKeyword(keyWord),HttpStatus.OK);
    }
    //upload video:
    @PostMapping("/upload")
    public VideoUploadResponse uploadVideo(@RequestParam("courseId") String courseId, @RequestParam("videoId") String videoId, @RequestParam("videoFile") MultipartFile videoFile) {
        VideoUploadResponse videoUploadResponse = videoUploadService.uploadVideo(courseId, videoId, videoFile);
        return videoUploadResponse;
    }

    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable String videoId,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {
        try {

            logger.info(rangeHeader);

            // Get the video file details
            VideoDto videoDto = videoService.getVideoById(videoId);
            Path filePath = Paths.get(videoDto.getFilePath());

            Resource videoResource = new UrlResource(filePath.toUri());

            if (!videoResource.exists() || !videoResource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            long fileSize = videoResource.contentLength();

            long start = 0, end = fileSize - 1;

            // Parse Range Header if present
            if (rangeHeader != null && rangeHeader.startsWith("bytes="))
            {
                String[] range = rangeHeader.replace("bytes=", "").split("-");


                try {
                    start = Long.parseLong(range[0]);

                    if (range.length > 1 && !range[1].isEmpty()) {
                        end = Long.parseLong(range[1]);
                    }

                    logger.info("{} - {}",start,end);
                } catch (NumberFormatException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            }

            // Validate the range
            if (start > end || end >= fileSize) {

                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                        .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                        .build();
            }



            // Create Partial Content Response
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, videoDto.getContentType());
            //bytes 0-999/10000
            headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
            headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");


            InputStream inputStream = Files.newInputStream(filePath);
            //this is very important:Moves the pointer to the start byte for the requested range.
            inputStream.skip(start); // Skip to the start of the range

            InputStreamResource resource = new InputStreamResource(inputStream);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .contentLength(end - start + 1)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}