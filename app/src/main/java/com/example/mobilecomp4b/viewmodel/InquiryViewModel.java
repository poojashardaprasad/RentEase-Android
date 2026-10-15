package com.example.mobilecomp4b.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mobilecomp4b.RentEaseApplication;
import com.example.mobilecomp4b.data.local.model.InquiryWithProperty;
import com.example.mobilecomp4b.data.repository.InquiryRepository;

import java.util.List;

public class InquiryViewModel extends AndroidViewModel {

    private final InquiryRepository inquiryRepository;

    public InquiryViewModel(@NonNull Application application) {
        super(application);
        RentEaseApplication app = (RentEaseApplication) application;
        this.inquiryRepository = app.getInquiryRepository();
    }

    public LiveData<List<InquiryWithProperty>> getAllInquiries() {
        return inquiryRepository.getAllInquiries();
    }

    public void submitInquiry(int propertyId, String name, String phone,
                              String email, String message, Runnable onComplete) {
        inquiryRepository.submitInquiry(propertyId, name, phone, email, message, onComplete);
    }
}
