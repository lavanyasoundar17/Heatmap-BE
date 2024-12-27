package com.example.BE_Heatmap.repository;

import com.example.BE_Heatmap.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepository extends MongoRepository<Location, String> {
}
