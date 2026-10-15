package com.example.mobilecomp4b.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mobilecomp4b.data.local.entity.FavoriteEntity;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long addFavorite(FavoriteEntity favorite);

    @Query("DELETE FROM favorites WHERE propertyId = :propertyId")
    void removeFavorite(int propertyId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE propertyId = :propertyId)")
    LiveData<Boolean> isFavorite(int propertyId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE propertyId = :propertyId)")
    boolean isFavoriteDirect(int propertyId);

    @Query("SELECT p.* FROM properties p INNER JOIN favorites f ON p.id = f.propertyId")
    LiveData<List<PropertyEntity>> getFavoriteProperties();

    @Query("SELECT COUNT(*) FROM favorites")
    LiveData<Integer> getFavoriteCount();
}
