package com.example.BE_Heatmap.service;

import com.example.BE_Heatmap.dto.LocationDto;
import com.example.BE_Heatmap.model.Location;
import com.example.BE_Heatmap.repository.LocationRepository;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
        this.modelMapper = new ModelMapper();
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

    private LocationDto convertToDto(Location location) {
        return modelMapper.map(location, LocationDto.class);
    }

    private Location convertToEntity(LocationDto locationDto, String userId) throws ParseException {
        Location location = modelMapper.map(locationDto, Location.class);
        location.setUserId(userId);
        return location;
    }
}
