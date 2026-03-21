package com.ecommerce.project.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service

public class FileServiceImpl implements FileService{


    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        String orignalFile = file.getOriginalFilename();

        // Generate a random Id for the file
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(orignalFile.substring(orignalFile.lastIndexOf(".")));
        String filePath = path + File.separator + fileName;
        //Check if Path Exist

        File foler = new File(path);
        if(!foler.exists())
            foler.mkdir();

        Files.copy(file.getInputStream() , Paths.get(filePath));

        return fileName;

    }
}
