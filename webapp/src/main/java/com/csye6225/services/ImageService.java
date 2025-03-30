package com.csye6225.services;

import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.csye6225.models.BookImage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Profile("default")
public class ImageService implements FileHandler{


    private final static Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Override
    public String uploadFile(MultipartFile multipartFile,String filePath) throws Exception {

        try {
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
        } catch (Exception e) {
            throw e;
        }


        return filePath;

    }



    @Override
    public String deleteFile(BookImage bookImage) throws Exception {
        File f = new File(bookImage.getUrl().toString());
        logger.info("Local ");
        if (f.exists()){
            f.delete();
        }
        return null;
    }

    @Override
    public String getFile(BookImage bookImage) throws Exception {
        return bookImage.getUrl();
    }
}

