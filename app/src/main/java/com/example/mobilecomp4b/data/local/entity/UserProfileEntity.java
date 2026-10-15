package com.example.mobilecomp4b.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_profile")
public class UserProfileEntity {

    @PrimaryKey
    private int id = 1;
    private String name;
    private String email;
    private String phone;
    private String preferredTenantType;

    public UserProfileEntity(int id, String name, String email, String phone, String preferredTenantType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.preferredTenantType = preferredTenantType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPreferredTenantType() { return preferredTenantType; }
    public void setPreferredTenantType(String preferredTenantType) { this.preferredTenantType = preferredTenantType; }
}
