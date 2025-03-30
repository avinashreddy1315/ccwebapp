package com.csye6225.models;


import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "BookCover")
public class BookImage {


    @Id
    private String id;

    @Column(name="path")
    private String url;

    public BookImage(){}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public Blob getUrl() {
//        return url;
//    }
//
//    public void setUrl(Blob url) {
//        this.url = url;
//    }
}
