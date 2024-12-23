package com.example.BE_Heatmap.repository;
import com.example.BE_Heatmap.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
}

