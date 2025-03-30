package com.csye6225.repository;

import com.csye6225.models.Book;
import com.csye6225.models.BookImage;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<BookImage, Integer> {
    BookImage findById(String id);

}
