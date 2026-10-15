package com.example.mobilecomp4b.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.mobilecomp4b.RentEaseApplication;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.data.repository.FavoritesRepository;
import com.example.mobilecomp4b.data.repository.PropertyRepository;

import java.util.List;

public class BrowseViewModel extends AndroidViewModel {

    public static class FilterState {
        public String query = "";
        public String tenantType = "Both";
        public Long minRent = null;
        public Long maxRent = null;
        public String propertyType = "All";
        public Integer bedrooms = null;
        public String furnishing = "All";
        public String sortBy = "DEFAULT";
    }

    private final PropertyRepository propertyRepository;
    private final FavoritesRepository favoritesRepository;
    private final MutableLiveData<FilterState> filterStateLiveData = new MutableLiveData<>(new FilterState());
    private final LiveData<List<PropertyEntity>> filteredProperties;

    public BrowseViewModel(@NonNull Application application) {
        super(application);
        RentEaseApplication app = (RentEaseApplication) application;
        this.propertyRepository = app.getPropertyRepository();
        this.favoritesRepository = app.getFavoritesRepository();

        this.filteredProperties = Transformations.switchMap(filterStateLiveData, state ->
                propertyRepository.filterProperties(
                        state.query,
                        state.tenantType,
                        state.minRent,
                        state.maxRent,
                        state.propertyType,
                        state.bedrooms,
                        state.furnishing,
                        state.sortBy
                )
        );
    }

    public LiveData<List<PropertyEntity>> getFilteredProperties() {
        return filteredProperties;
    }

    public LiveData<FilterState> getFilterState() {
        return filterStateLiveData;
    }

    public void updateSearchQuery(String query) {
        FilterState current = filterStateLiveData.getValue();
        if (current != null) {
            current.query = query;
            filterStateLiveData.setValue(current);
        }
    }

    public void updateTenantType(String tenantType) {
        FilterState current = filterStateLiveData.getValue();
        if (current != null) {
            current.tenantType = tenantType;
            filterStateLiveData.setValue(current);
        }
    }

    public void applyFilters(Long minRent, Long maxRent, String propertyType, Integer bedrooms, String furnishing, String sortBy) {
        FilterState current = filterStateLiveData.getValue();
        if (current != null) {
            current.minRent = minRent;
            current.maxRent = maxRent;
            current.propertyType = propertyType;
            current.bedrooms = bedrooms;
            current.furnishing = furnishing;
            current.sortBy = sortBy;
            filterStateLiveData.setValue(current);
        }
    }

    public void clearFilters() {
        FilterState fresh = new FilterState();
        FilterState current = filterStateLiveData.getValue();
        if (current != null) {
            fresh.query = current.query;
        }
        filterStateLiveData.setValue(fresh);
    }

    public void toggleFavorite(int propertyId) {
        favoritesRepository.toggleFavorite(propertyId);
    }
}
