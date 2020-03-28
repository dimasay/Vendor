package com.vendor.services;

import com.amazonaws.services.s3.AmazonS3;
import com.vendor.dao.SensorDAO;
import com.vendor.dtos.CreateVendorRequest;
import com.vendor.dtos.UpdateVendorRequest;
import com.vendor.model.Vendor;
import com.vendor.repository.VendorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VendorService {
    private final VendorRepository vendorRepository;
    private final SensorDAO sensorDAO;
    private final AmazonS3 amazonS3;
    private final String STORAGE_TYPE_DOESNT_SUPPORT = "This storage type doesn't support";

    public String createVendor(CreateVendorRequest createVendorRequest) {
        if (!contains(createVendorRequest.getStorageType()))
            return STORAGE_TYPE_DOESNT_SUPPORT;
        Vendor vendor = new Vendor();
        vendor.setVendorName(createVendorRequest.getName());
        vendor.setSensorType(createVendorRequest.getSensorType());
        vendor.setStorageType(createVendorRequest.getStorageType());
        String token = vendor.getVendorName() + vendor.getSensorType() + Math.random();
        vendor.setToken(token);
        vendorRepository.save(vendor);
        sensorDAO.createTable(vendor);
        amazonS3.createBucket(vendor.getVendorName() + vendor.getSensorType());
        return token;
    }

    public void changeVendorStorageType(UpdateVendorRequest updateVendorRequest) {
        if (!contains(updateVendorRequest.getStorageType())) {
            return;
        }
        Optional<Vendor> optional = vendorRepository.findByVendorName(updateVendorRequest.getVendorName());
        if (optional.isPresent()) {
            Vendor vendor = optional.get();
            vendor.setStorageType(updateVendorRequest.getStorageType());
            vendorRepository.save(vendor);
        }

    }

    public String getVendorToken(String vendorName) {
        Optional<Vendor> optional = vendorRepository.findByVendorName(vendorName);
        return optional.map(Vendor::getToken).orElse("Vendor doesn't exists");
    }

    public Vendor getVendorByToken(String token) {
        Optional<Vendor> optional = vendorRepository.findByToken(token);
        return optional.orElse(null);
    }

    public boolean contains(String storageType) {
        for (Vendor.StorageTypes storageTypes : Vendor.StorageTypes.values()) {
            if (storageTypes.name().equals(storageType)) {
                return true;
            }
        }
        return false;
    }
}
