package com.example.mobilecomp4b.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "properties")
public class PropertyEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String locality;
    private String city;
    private long monthlyRent;
    private long deposit;
    private String propertyType;
    private int bedrooms;
    private String tenantType;
    private String furnishing;
    private String amenities;
    private String imageName;
    private boolean isAvailable;

    public PropertyEntity(int id, String title, String description, String locality, String city,
                          long monthlyRent, long deposit, String propertyType, int bedrooms,
                          String tenantType, String furnishing, String amenities, String imageName,
                          boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.locality = locality;
        this.city = city;
        this.monthlyRent = monthlyRent;
        this.deposit = deposit;
        this.propertyType = propertyType;
        this.bedrooms = bedrooms;
        this.tenantType = tenantType;
        this.furnishing = furnishing;
        this.amenities = amenities;
        this.imageName = imageName;
        this.isAvailable = isAvailable;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocality() { return locality; }
    public void setLocality(String locality) { this.locality = locality; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public long getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(long monthlyRent) { this.monthlyRent = monthlyRent; }

    public long getDeposit() { return deposit; }
    public void setDeposit(long deposit) { this.deposit = deposit; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public int getBedrooms() { return bedrooms; }
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }

    public String getTenantType() { return tenantType; }
    public void setTenantType(String tenantType) { this.tenantType = tenantType; }

    public String getFurnishing() { return furnishing; }
    public void setFurnishing(String furnishing) { this.furnishing = furnishing; }

    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
