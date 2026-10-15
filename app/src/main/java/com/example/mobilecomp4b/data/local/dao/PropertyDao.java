package com.example.mobilecomp4b.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mobilecomp4b.data.local.entity.PropertyEntity;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM properties")
    LiveData<List<PropertyEntity>> getAllProperties();

    @Query("SELECT * FROM properties WHERE isAvailable = 1 ORDER BY id DESC LIMIT 5")
    LiveData<List<PropertyEntity>> getFeaturedProperties();

    @Query("SELECT * FROM properties WHERE id = :id")
    LiveData<PropertyEntity> getPropertyById(int id);

    @Query("SELECT * FROM properties WHERE id = :id")
    PropertyEntity getPropertyByIdDirect(int id);

    @Query("SELECT COUNT(*) FROM properties")
    int getPropertyCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProperties(List<PropertyEntity> properties);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProperty(PropertyEntity property);

    @Query("SELECT * FROM properties WHERE " +
            "(:query IS NULL OR :query = '' OR title LIKE '%' || :query || '%' OR locality LIKE '%' || :query || '%' OR city LIKE '%' || :query || '%') AND " +
            "(:tenantType = 'Both' OR tenantType = :tenantType OR tenantType = 'Both') AND " +
            "(:minRent IS NULL OR monthlyRent >= :minRent) AND " +
            "(:maxRent IS NULL OR monthlyRent <= :maxRent) AND " +
            "(:propertyType IS NULL OR :propertyType = 'All' OR propertyType = :propertyType) AND " +
            "(:bedrooms IS NULL OR bedrooms >= :bedrooms) AND " +
            "(:furnishing IS NULL OR :furnishing = 'All' OR furnishing = :furnishing) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'RENT_ASC' THEN monthlyRent END ASC, " +
            "CASE WHEN :sortBy = 'RENT_DESC' THEN monthlyRent END DESC, " +
            "id DESC")
    LiveData<List<PropertyEntity>> filterProperties(
            String query,
            String tenantType,
            Long minRent,
            Long maxRent,
            String propertyType,
            Integer bedrooms,
            String furnishing,
            String sortBy
    );
}
