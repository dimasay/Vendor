package com.vendor.dtos;

import lombok.Data;

@Data
public class UpdateVendorRequest {
    private String vendorName;
    private String storageType;
}
