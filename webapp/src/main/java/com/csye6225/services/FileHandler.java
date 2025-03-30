package com.csye6225.services;

import com.csye6225.models.BookImage;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

public interface FileHandler {

    public String uploadFile(MultipartFile multipartFile, String emailAddress) throws Exception;



    public String deleteFile(BookImage bookImage) throws Exception;
    public String getFile(BookImage bookImage) throws Exception;
}
