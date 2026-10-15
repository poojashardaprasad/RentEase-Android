package com.example.mobilecomp4b.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mobilecomp4b.RentEaseApplication;
import com.example.mobilecomp4b.data.local.entity.UserProfileEntity;
import com.example.mobilecomp4b.data.repository.FavoritesRepository;
import com.example.mobilecomp4b.data.repository.ProfileRepository;
import com.example.mobilecomp4b.data.repository.VisitRepository;

public class ProfileViewModel extends AndroidViewModel {

    private final ProfileRepository profileRepository;
    private final FavoritesRepository favoritesRepository;
    private final VisitRepository visitRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        RentEaseApplication app = (RentEaseApplication) application;
        this.profileRepository = app.getProfileRepository();
        this.favoritesRepository = app.getFavoritesRepository();
        this.visitRepository = app.getVisitRepository();
    }

    public LiveData<UserProfileEntity> getUserProfile() {
        return profileRepository.getUserProfile();
    }

    public LiveData<Integer> getFavoriteCount() {
        return favoritesRepository.getFavoriteCount();
    }

    public LiveData<Integer> getPendingRequestsCount() {
        return visitRepository.getPendingRequestsCount();
    }

    public void saveProfile(String name, String email, String phone, String tenantType, Runnable onComplete) {
        profileRepository.saveProfile(name, email, phone, tenantType, onComplete);
    }

    public void resetProfile(Runnable onComplete) {
        profileRepository.resetProfile(onComplete);
    }
}
