package com.vendor.dao;

import com.vendor.model.Vendor;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SensorDAO {
    private JdbcTemplate jdbcTemplate;

    public void createTable(Vendor vendor) {
        String sql = "CREATE TABLE %s (" +
                "id int primary key auto_increment," +
                "data varchar(256)," +
                "file LONGBLOB);";
        String finalSql = String.format(sql, getTableName(vendor));
        jdbcTemplate.execute(finalSql);
    }

    public void insert(String sensorData, Vendor vendor) {
        String sql = "INSERT INTO %s (data) VALUES (?)";
        String finalSql = String.format(sql, getTableName(vendor));
        jdbcTemplate.update(finalSql, sensorData);
    }

    public void insertBytes(byte[] data, Vendor vendor) {
        String sql = "INSERT INTO %s (file) VALUES (?)";
        String finalSql = String.format(sql, getTableName(vendor));
        jdbcTemplate.update(finalSql, data);
    }

    private String getTableName(Vendor vendor) {
        return vendor.getVendorName() + vendor.getSensorType();
    }
}
