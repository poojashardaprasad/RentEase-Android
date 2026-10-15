package com.example.mobilecomp4b.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "inquiries",
    foreignKeys = @ForeignKey(
        entity = PropertyEntity.class,
        parentColumns = "id",
        childColumns = "propertyId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index(value = "propertyId")}
)
public class InquiryEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int propertyId;
    private String name;
    private String phone;
    private String email;
    private String message;
    private String status;
    private long createdAt;

    public InquiryEntity(int id, int propertyId, String name, String phone,
                         String email, String message, String status, long createdAt) {
        this.id = id;
        this.propertyId = propertyId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPropertyId() { return propertyId; }
    public void setPropertyId(int propertyId) { this.propertyId = propertyId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
