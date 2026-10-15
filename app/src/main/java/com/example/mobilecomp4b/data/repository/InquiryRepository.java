package com.example.mobilecomp4b.data.repository;

import androidx.lifecycle.LiveData;

import com.example.mobilecomp4b.data.local.dao.InquiryDao;
import com.example.mobilecomp4b.data.local.entity.InquiryEntity;
import com.example.mobilecomp4b.data.local.model.InquiryWithProperty;
import com.example.mobilecomp4b.utils.AppExecutors;

import java.util.List;

public class InquiryRepository {

    private final InquiryDao inquiryDao;
    private final AppExecutors executors;

    public InquiryRepository(InquiryDao inquiryDao, AppExecutors executors) {
        this.inquiryDao = inquiryDao;
        this.executors = executors;
    }

    public void submitInquiry(int propertyId, String name, String phone,
                              String email, String message, Runnable onComplete) {
        executors.diskIO().execute(() -> {
            InquiryEntity inquiry = new InquiryEntity(
                    0, propertyId, name, phone, email, message, "Pending", System.currentTimeMillis()
            );
            inquiryDao.insertInquiry(inquiry);
            if (onComplete != null) {
                executors.mainThread().execute(onComplete);
            }
        });
    }

    public LiveData<List<InquiryWithProperty>> getAllInquiries() {
        return inquiryDao.getAllInquiries();
    }

    public LiveData<Integer> getInquiryCount() {
        return inquiryDao.getInquiryCount();
    }
}
