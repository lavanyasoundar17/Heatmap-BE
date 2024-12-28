package com.example.BE_Heatmap.service;

import com.example.BE_Heatmap.dto.FilterObj;
import com.example.BE_Heatmap.dto.LocationDto;
import com.example.BE_Heatmap.model.Location;
import com.example.BE_Heatmap.repository.LocationRepository;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public LocationService(LocationRepository locationRepository, MongoTemplate mongoTemplate) {
        this.locationRepository = locationRepository;
        this.modelMapper = new ModelMapper();
        this.mongoTemplate = mongoTemplate;
    }

    @SneakyThrows
    public List<LocationDto> saveAll(List<LocationDto> locations, String userId) {
        List<Location> locationList = locations.stream().map(l -> {
            try {
                return convertToEntity(l, userId);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        return locationRepository.saveAll(locationList).stream().map(this::convertToDto).toList();
    }

    public List<LocationDto> getAllLocation(String userId) {
        try {
            return locationRepository.findByUserId(userId).stream().map(this::convertToDto).toList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<LocationDto> getLocationWithinStartNEndDate(String userId, FilterObj filter) {
        try {
            String startDate = filter.getStartDate();
            String endDate = filter.getEndDate();
            return findWithInStartAndEndDateWithUserId(userId, startDate, endDate).stream().map(this::convertToDto).toList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private LocationDto convertToDto(Location location) {
        return modelMapper.map(location, LocationDto.class);
    }

    private Location convertToEntity(LocationDto locationDto, String userId) throws ParseException {
        Location location = modelMapper.map(locationDto, Location.class);
        location.setUserId(userId);
        return location;
    }

    private List<Location> findWithInStartAndEndDateWithUserId(String userId, String startDate, String endDate) {
        String queryString = "{date: {$gte : ISODate('" + startDate + "'), $lte : ISODate('" + endDate + "')}, userId: {$eq: '"+ userId +"'}}";
        BasicQuery query = new BasicQuery(queryString);
        return mongoTemplate.find(query, Location.class);
    }
}
