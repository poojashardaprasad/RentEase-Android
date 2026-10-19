package com.example.mobilecomp4b;

import android.app.Application;

import com.example.mobilecomp4b.data.local.RentEaseDatabase;
import com.example.mobilecomp4b.data.repository.FavoritesRepository;
import com.example.mobilecomp4b.data.repository.InquiryRepository;
import com.example.mobilecomp4b.data.repository.ProfileRepository;
import com.example.mobilecomp4b.data.repository.PropertyRepository;
import com.example.mobilecomp4b.data.repository.VisitRepository;
import com.example.mobilecomp4b.data.seed.DatabaseSeeder;
import com.example.mobilecomp4b.utils.AppExecutors;
import com.google.android.gms.maps.MapsInitializer;

public class RentEaseApplication extends Application {

    private RentEaseDatabase database;
    private AppExecutors executors;
    private PropertyRepository propertyRepository;
    private FavoritesRepository favoritesRepository;
    private VisitRepository visitRepository;
    private InquiryRepository inquiryRepository;
    private ProfileRepository profileRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        executors = AppExecutors.getInstance();
        database = RentEaseDatabase.getInstance(this);

        propertyRepository = new PropertyRepository(database.propertyDao());
        favoritesRepository = new FavoritesRepository(database.favoriteDao(), executors);
        visitRepository = new VisitRepository(database.visitRequestDao(), executors);
        inquiryRepository = new InquiryRepository(database.inquiryDao(), executors);
        profileRepository = new ProfileRepository(database.userProfileDao(), executors);

        // Initialize Google Maps SDK Latest Renderer
        try {
            MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, null);
        } catch (Exception ignored) {}

        // Seed initial data on background thread
        executors.diskIO().execute(() ->
            DatabaseSeeder.seedInitialData(database.propertyDao(), database.userProfileDao())
        );
    }

    public RentEaseDatabase getDatabase() { return database; }
    public AppExecutors getExecutors() { return executors; }
    public PropertyRepository getPropertyRepository() { return propertyRepository; }
    public FavoritesRepository getFavoritesRepository() { return favoritesRepository; }
    public VisitRepository getVisitRepository() { return visitRepository; }
    public InquiryRepository getInquiryRepository() { return inquiryRepository; }
    public ProfileRepository getProfileRepository() { return profileRepository; }
}
