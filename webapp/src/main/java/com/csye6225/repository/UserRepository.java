package com.csye6225.repository;

import com.csye6225.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmailAddress(String emaidAddress);
}
