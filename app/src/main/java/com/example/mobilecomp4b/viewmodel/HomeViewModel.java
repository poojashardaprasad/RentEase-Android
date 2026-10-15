package com.example.mobilecomp4b.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mobilecomp4b.RentEaseApplication;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.data.repository.FavoritesRepository;
import com.example.mobilecomp4b.data.repository.PropertyRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final PropertyRepository propertyRepository;
    private final FavoritesRepository favoritesRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        RentEaseApplication app = (RentEaseApplication) application;
        this.propertyRepository = app.getPropertyRepository();
        this.favoritesRepository = app.getFavoritesRepository();
    }

    public LiveData<List<PropertyEntity>> getFeaturedProperties() {
        return propertyRepository.getFeaturedProperties();
    }

    public LiveData<List<PropertyEntity>> getAllProperties() {
        return propertyRepository.getAllProperties();
    }

    public void toggleFavorite(int propertyId) {
        favoritesRepository.toggleFavorite(propertyId);
    }
}
