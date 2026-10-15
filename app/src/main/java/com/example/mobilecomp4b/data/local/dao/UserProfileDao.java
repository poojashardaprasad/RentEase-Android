package com.example.mobilecomp4b.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mobilecomp4b.data.local.entity.UserProfileEntity;

@Dao
public interface UserProfileDao {

    @Query("SELECT * FROM user_profile WHERE id = 1")
    LiveData<UserProfileEntity> getUserProfile();

    @Query("SELECT * FROM user_profile WHERE id = 1")
    UserProfileEntity getUserProfileDirect();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateProfile(UserProfileEntity profile);

    @Query("DELETE FROM user_profile")
    void clearProfile();
}
