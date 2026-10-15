package com.example.mobilecomp4b.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "visit_requests",
    foreignKeys = @ForeignKey(
        entity = PropertyEntity.class,
        parentColumns = "id",
        childColumns = "propertyId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index(value = "propertyId")}
)
public class VisitRequestEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int propertyId;
    private String name;
    private String phone;
    private String visitDate;
    private String visitTime;
    private String message;
    private String status;
    private long createdAt;

    public VisitRequestEntity(int id, int propertyId, String name, String phone,
                              String visitDate, String visitTime, String message,
                              String status, long createdAt) {
        this.id = id;
        this.propertyId = propertyId;
        this.name = name;
        this.phone = phone;
        this.visitDate = visitDate;
        this.visitTime = visitTime;
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

    public String getVisitDate() { return visitDate; }
    public void setVisitDate(String visitDate) { this.visitDate = visitDate; }

    public String getVisitTime() { return visitTime; }
    public void setVisitTime(String visitTime) { this.visitTime = visitTime; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
