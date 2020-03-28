package com.vendor.dtos;

import lombok.Data;

@Data
public class CreateVendorRequest {
    private String name;
    private String storageType;
    private String sensorType;
}
