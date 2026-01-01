package com.webstore.implementation;

import com.webstore.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImplementation implements FileService {

    @Value("${project.image}")
    private String imagePath;

    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        String fileName = image.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        String newFileName = randomId.concat(fileName.substring(fileName.lastIndexOf('.')));
        String filePath = imagePath + File.separator + newFileName;

        File folder = new File(imagePath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        Files.copy(image.getInputStream(), Paths.get(filePath));

        return newFileName;
    }
}
