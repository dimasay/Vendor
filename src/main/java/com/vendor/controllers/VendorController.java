package com.vendor.controllers;

import com.vendor.dtos.CreateVendorRequest;
import com.vendor.dtos.UpdateVendorRequest;
import com.vendor.services.VendorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class VendorController {
    private final VendorService vendorService;

    @PostMapping(value = "/vendor")
    public String createVendor(@RequestBody CreateVendorRequest createVendorRequest) {
        return vendorService.createVendor(createVendorRequest);
    }

    @GetMapping(value = "/vendor")
    public String getVendorToken(@RequestParam("name") String vendorName) {
        return vendorService.getVendorToken(vendorName);
    }

    @PutMapping(value = "/vendor")
    public void updateVendor(@RequestBody UpdateVendorRequest updateVendorRequest) {
        vendorService.changeVendorStorageType(updateVendorRequest);
    }
}
