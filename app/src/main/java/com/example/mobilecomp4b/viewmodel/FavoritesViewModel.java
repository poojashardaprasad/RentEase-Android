package com.example.mobilecomp4b.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mobilecomp4b.RentEaseApplication;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.data.repository.FavoritesRepository;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private final FavoritesRepository favoritesRepository;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        RentEaseApplication app = (RentEaseApplication) application;
        this.favoritesRepository = app.getFavoritesRepository();
    }

    public LiveData<List<PropertyEntity>> getFavoriteProperties() {
        return favoritesRepository.getFavoriteProperties();
    }

    public void removeFavorite(int propertyId) {
        favoritesRepository.removeFavorite(propertyId);
    }
}
