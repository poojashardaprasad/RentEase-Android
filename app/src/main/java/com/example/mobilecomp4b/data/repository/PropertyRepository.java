package com.example.mobilecomp4b.data.repository;

import androidx.lifecycle.LiveData;

import com.example.mobilecomp4b.data.local.dao.PropertyDao;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;

import java.util.List;

public class PropertyRepository {

    private final PropertyDao propertyDao;

    public PropertyRepository(PropertyDao propertyDao) {
        this.propertyDao = propertyDao;
    }

    public LiveData<List<PropertyEntity>> getAllProperties() {
        return propertyDao.getAllProperties();
    }

    public LiveData<List<PropertyEntity>> getFeaturedProperties() {
        return propertyDao.getFeaturedProperties();
    }

    public LiveData<PropertyEntity> getPropertyById(int id) {
        return propertyDao.getPropertyById(id);
    }

    public void deleteProperty(int id) {
        propertyDao.deletePropertyById(id);
    }

    public LiveData<List<PropertyEntity>> filterProperties(
            String query,
            String tenantType,
            Long minRent,
            Long maxRent,
            String propertyType,
            Integer bedrooms,
            String furnishing,
            String sortBy
    ) {
        String cleanQuery = (query == null || query.trim().isEmpty()) ? null : query.trim();
        return propertyDao.filterProperties(
                cleanQuery,
                tenantType,
                minRent,
                maxRent,
                propertyType,
                bedrooms,
                furnishing,
                sortBy
        );
    }
}
