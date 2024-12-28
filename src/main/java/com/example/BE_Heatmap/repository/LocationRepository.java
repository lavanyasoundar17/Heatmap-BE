package com.example.BE_Heatmap.repository;

import com.example.BE_Heatmap.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LocationRepository extends MongoRepository<Location, String> {
    List<Location> findByUserId(String userId);
}
