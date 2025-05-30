package com.service.course.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
@Service
public interface FileService {
    String save(MultipartFile file, String outputPath, String filename) throws IOException;
}