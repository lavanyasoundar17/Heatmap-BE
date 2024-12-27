package com.example.BE_Heatmap.dto;

import java.util.List;

public class LocationRequest {
    private List<LocationDto> data;

    public List<LocationDto> getData() {
        return data;
    }

    public void setData(List<LocationDto> data) {
        this.data = data;
    }
}
