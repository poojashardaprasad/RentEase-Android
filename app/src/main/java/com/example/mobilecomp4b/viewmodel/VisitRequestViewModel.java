package com.example.mobilecomp4b.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mobilecomp4b.RentEaseApplication;
import com.example.mobilecomp4b.data.local.model.VisitRequestWithProperty;
import com.example.mobilecomp4b.data.repository.VisitRepository;

import java.util.List;

public class VisitRequestViewModel extends AndroidViewModel {

    private final VisitRepository visitRepository;

    public VisitRequestViewModel(@NonNull Application application) {
        super(application);
        RentEaseApplication app = (RentEaseApplication) application;
        this.visitRepository = app.getVisitRepository();
    }

    public LiveData<List<VisitRequestWithProperty>> getAllVisitRequests() {
        return visitRepository.getAllVisitRequests();
    }

    public void requestVisit(int propertyId, String name, String phone,
                             String visitDate, String visitTime, String message,
                             Runnable onComplete) {
        visitRepository.requestVisit(propertyId, name, phone, visitDate, visitTime, message, onComplete);
    }

    public void cancelVisitRequest(int requestId) {
        visitRepository.cancelVisitRequest(requestId);
    }
}
