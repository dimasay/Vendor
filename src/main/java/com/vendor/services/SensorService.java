package com.vendor.services;

import com.amazonaws.services.s3.AmazonS3;
import com.vendor.aws.S3Utils;
import com.vendor.dao.SensorDAO;
import com.vendor.model.Vendor;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@AllArgsConstructor
public class SensorService {
    private final SensorDAO sensorDAO;
    private final AmazonS3 amazonS3;

    public void postSensorDataFromJson(String sensorDataJson, Vendor vendor) {
        switch (Vendor.StorageTypes.valueOf(vendor.getStorageType())) {
            case DB:
                postSensorDataFromJsonToDb(sensorDataJson, vendor);
                break;
            case S3:
                postSensorDataFromJsonToS3(sensorDataJson, vendor);
        }
    }

    private void postSensorDataFromJsonToDb(String sensorDataJson, Vendor vendor) {
        sensorDAO.insert(sensorDataJson, vendor);
    }

    private void postSensorDataFromJsonToS3(String sensorDataJson, Vendor vendor) {
        try {
            File file = File.createTempFile(getBucketName(vendor), ".json");
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                byte[] content = S3Utils.convert(sensorDataJson);
                if (content != null) {
                    fileOutputStream.write(content);
                    amazonS3.putObject(getBucketName(vendor), file.getAbsolutePath(), file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void postSensorDataFromFile(MultipartFile multipartFile, Vendor vendor) {
        switch (Vendor.StorageTypes.valueOf(vendor.getStorageType())) {
            case DB:
                postSensorDataFromFileToDb(multipartFile, vendor);
                break;
            case S3:
                postSensorDataFromFileToS3(multipartFile, vendor);
                break;
        }
    }

    @SneakyThrows
    private void postSensorDataFromFileToDb(MultipartFile multipartFile, Vendor vendor) {
        sensorDAO.insertBytes(multipartFile.getBytes(), vendor);
    }

    private void postSensorDataFromFileToS3(MultipartFile multipartFile, Vendor vendor) {
        uploadFileFromMultipart(multipartFile, getBucketName(vendor));
    }

    public void uploadFileFromMultipart(MultipartFile multipartFile, String bucketName) {
        try {
            File file = File.createTempFile(multipartFile.getName(), ".csv");
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                byte[] content = S3Utils.convert(multipartFile);
                if (content != null) {
                    fileOutputStream.write(content);
                    amazonS3.putObject(bucketName, file.getAbsolutePath(), file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getBucketName(Vendor vendor) {
        return vendor.getVendorName() + vendor.getSensorType();
    }
}
