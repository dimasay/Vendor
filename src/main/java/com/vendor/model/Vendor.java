package com.vendor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vendors")
@Entity
public class Vendor {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "vendor_name")
    private String vendorName;
    @Column(name = "storage_type")
    private String storageType;
    @Column(name = "sensor_type")
    private String sensorType;
    @Column(name = "auth_token")
    private String token;

    public enum StorageTypes {
        DB, S3
    }
}


