package com.example.BE_Heatmap.dto;

import java.util.List;

public class LocationResponse {
    private List<LocationDto> locationList;
    private String status;
    private String error;
    private String errorCode;

    public LocationResponse(List<LocationDto> locationList, String status) {
        this.locationList = locationList;
        this.status = status;
    }

    public LocationResponse(List<LocationDto> locationList, String status, String error, String errorCode) {
        this.locationList = locationList;
        this.status = status;
        this.error = error;
        this.errorCode = errorCode;
    }

    public List<LocationDto> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<LocationDto> locationList) {
        this.locationList = locationList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
