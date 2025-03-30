package com.csye6225.services;


import com.amazonaws.auth.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.csye6225.models.BookImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Profile("dev")
public class AwsFileHandler implements FileHandler {

    private final static Logger logger = LoggerFactory.getLogger(AwsFileHandler.class);

    private AmazonS3 s3client;

    private String dir = "Images";

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

   @Value("${spring.bucket.name}")
    private String bucketName;


    @PostConstruct
    private void initializeAmazon() {
        this.s3client = AmazonS3ClientBuilder.standard().build();

    }

    @Override
    public String uploadFile(MultipartFile multipartFile, String fileName) throws Exception {
      //  String fileName = (new Date().toString() + "-" + multipartFile.getOriginalFilename()).replace(" ", "_");

        logger.info(multipartFile.getName());

        String name = this.dir + "/" + fileName;
        logger.info(name);

        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

       s3client.putObject(bucketName, name, multipartFile.getInputStream(), new ObjectMetadata());


        return name;

    }






    @Override
    public String deleteFile(BookImage bookImage) throws Exception {
        String fileName= bookImage.getUrl().toString();

        logger.info(fileName);
        s3client.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(fileName));
        return "Successfully deleted";
    }

    @Override
    public String getFile(BookImage bookImage) throws Exception {
        String name= bookImage.getUrl();
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 120;
        expiration.setTime(expTimeMillis);

        s3client.generatePresignedUrl(bucketName,name,expiration);
        logger.info(String.valueOf(s3client.generatePresignedUrl(bucketName,name,expiration)));


        return String.valueOf(s3client.generatePresignedUrl(bucketName,name,expiration));
    }
}