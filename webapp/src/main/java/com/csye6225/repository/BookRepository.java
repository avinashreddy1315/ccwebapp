package com.csye6225.repository;

import com.csye6225.models.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {
    Book findById(String id);

}