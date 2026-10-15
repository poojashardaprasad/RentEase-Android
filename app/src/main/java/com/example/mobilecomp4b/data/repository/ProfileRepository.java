package com.example.mobilecomp4b.data.repository;

import androidx.lifecycle.LiveData;

import com.example.mobilecomp4b.data.local.dao.UserProfileDao;
import com.example.mobilecomp4b.data.local.entity.UserProfileEntity;
import com.example.mobilecomp4b.utils.AppExecutors;

public class ProfileRepository {

    private final UserProfileDao profileDao;
    private final AppExecutors executors;

    public ProfileRepository(UserProfileDao profileDao, AppExecutors executors) {
        this.profileDao = profileDao;
        this.executors = executors;
    }

    public LiveData<UserProfileEntity> getUserProfile() {
        return profileDao.getUserProfile();
    }

    public void saveProfile(String name, String email, String phone, String tenantType, Runnable onComplete) {
        executors.diskIO().execute(() -> {
            UserProfileEntity profile = new UserProfileEntity(1, name, email, phone, tenantType);
            profileDao.insertOrUpdateProfile(profile);
            if (onComplete != null) {
                executors.mainThread().execute(onComplete);
            }
        });
    }

    public void resetProfile(Runnable onComplete) {
        executors.diskIO().execute(() -> {
            UserProfileEntity defaultProfile = new UserProfileEntity(
                    1, "Alex Morgan", "alex.morgan@example.com", "9876543210", "Both"
            );
            profileDao.insertOrUpdateProfile(defaultProfile);
            if (onComplete != null) {
                executors.mainThread().execute(onComplete);
            }
        });
    }
}
