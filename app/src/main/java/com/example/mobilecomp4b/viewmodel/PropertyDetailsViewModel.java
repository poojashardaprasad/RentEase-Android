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

public class PropertyDetailsViewModel extends AndroidViewModel {

    private final PropertyRepository propertyRepository;
    private final FavoritesRepository favoritesRepository;

    private final MutableLiveData<Integer> propertyIdLiveData = new MutableLiveData<>();
    private final LiveData<PropertyEntity> propertyLiveData;
    private final LiveData<Boolean> isFavoriteLiveData;

    public PropertyDetailsViewModel(@NonNull Application application) {
        super(application);
        RentEaseApplication app = (RentEaseApplication) application;
        this.propertyRepository = app.getPropertyRepository();
        this.favoritesRepository = app.getFavoritesRepository();

        this.propertyLiveData = Transformations.switchMap(propertyIdLiveData, propertyRepository::getPropertyById);
        this.isFavoriteLiveData = Transformations.switchMap(propertyIdLiveData, favoritesRepository::isFavorite);
    }

    public void setPropertyId(int propertyId) {
        propertyIdLiveData.setValue(propertyId);
    }

    public LiveData<PropertyEntity> getProperty() {
        return propertyLiveData;
    }

    public LiveData<Boolean> isFavorite() {
        return isFavoriteLiveData;
    }

    public void toggleFavorite() {
        Integer id = propertyIdLiveData.getValue();
        if (id != null) {
            favoritesRepository.toggleFavorite(id);
        }
    }
}
