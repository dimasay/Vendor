package com.vendor.controllers;

import com.vendor.model.Vendor;
import com.vendor.services.SensorService;
import com.vendor.services.VendorService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
public class SensorController {
    private SensorService sensorService;
    private VendorService vendorService;

    public SensorController(SensorService sensorService, VendorService vendorService) {
        this.sensorService = sensorService;
        this.vendorService = vendorService;
    }

    @Async("threadPoolTaskExecutor")
    @PostMapping(value = "/sensor")
    public void postSensorData(@RequestBody String sensorDataJson,
                               @RequestHeader(value = "token") String token,
                               HttpServletResponse response) {
        Vendor vendor = vendorService.getVendorByToken(token);
        if (vendor != null) {
            sensorService.postSensorDataFromJson(sensorDataJson, vendor);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Async("threadPoolTaskExecutor")
    @PostMapping(value = "/sensor", consumes = "multipart/form-data")
    public void postSensorData(@RequestParam("file") MultipartFile multipartFile,
                               @RequestHeader(value = "token") String token,
                               HttpServletResponse response) {
        Vendor vendor = vendorService.getVendorByToken(token);
        if (vendor != null) {
            sensorService.postSensorDataFromFile(multipartFile, vendor);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
