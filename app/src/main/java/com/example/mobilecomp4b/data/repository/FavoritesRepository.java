package com.example.mobilecomp4b.data.repository;

import androidx.lifecycle.LiveData;

import com.example.mobilecomp4b.data.local.dao.FavoriteDao;
import com.example.mobilecomp4b.data.local.entity.FavoriteEntity;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.utils.AppExecutors;

import java.util.List;

public class FavoritesRepository {

    private final FavoriteDao favoriteDao;
    private final AppExecutors executors;

    public FavoritesRepository(FavoriteDao favoriteDao, AppExecutors executors) {
        this.favoriteDao = favoriteDao;
        this.executors = executors;
    }

    public LiveData<Boolean> isFavorite(int propertyId) {
        return favoriteDao.isFavorite(propertyId);
    }

    public void toggleFavorite(int propertyId) {
        executors.diskIO().execute(() -> {
            boolean currentlyFavorite = favoriteDao.isFavoriteDirect(propertyId);
            if (currentlyFavorite) {
                favoriteDao.removeFavorite(propertyId);
            } else {
                favoriteDao.addFavorite(new FavoriteEntity(0, propertyId));
            }
        });
    }

    public void removeFavorite(int propertyId) {
        executors.diskIO().execute(() -> favoriteDao.removeFavorite(propertyId));
    }

    public LiveData<List<PropertyEntity>> getFavoriteProperties() {
        return favoriteDao.getFavoriteProperties();
    }

    public LiveData<Integer> getFavoriteCount() {
        return favoriteDao.getFavoriteCount();
    }

    public LiveData<List<Integer>> getAllFavoriteIds() {
        return favoriteDao.getAllFavoriteIds();
    }
}
