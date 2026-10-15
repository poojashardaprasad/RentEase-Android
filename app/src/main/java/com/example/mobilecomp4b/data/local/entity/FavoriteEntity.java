package com.example.mobilecomp4b.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "favorites",
    foreignKeys = @ForeignKey(
        entity = PropertyEntity.class,
        parentColumns = "id",
        childColumns = "propertyId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index(value = "propertyId", unique = true)}
)
public class FavoriteEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int propertyId;

    public FavoriteEntity(int id, int propertyId) {
        this.id = id;
        this.propertyId = propertyId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPropertyId() { return propertyId; }
    public void setPropertyId(int propertyId) { this.propertyId = propertyId; }
}
