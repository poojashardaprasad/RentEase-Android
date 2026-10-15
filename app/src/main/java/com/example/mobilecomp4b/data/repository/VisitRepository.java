package com.example.mobilecomp4b.data.repository;

import androidx.lifecycle.LiveData;

import com.example.mobilecomp4b.data.local.dao.VisitRequestDao;
import com.example.mobilecomp4b.data.local.entity.VisitRequestEntity;
import com.example.mobilecomp4b.data.local.model.VisitRequestWithProperty;
import com.example.mobilecomp4b.utils.AppExecutors;

import java.util.List;

public class VisitRepository {

    private final VisitRequestDao visitRequestDao;
    private final AppExecutors executors;

    public VisitRepository(VisitRequestDao visitRequestDao, AppExecutors executors) {
        this.visitRequestDao = visitRequestDao;
        this.executors = executors;
    }

    public void requestVisit(int propertyId, String name, String phone,
                             String visitDate, String visitTime, String message,
                             Runnable onComplete) {
        executors.diskIO().execute(() -> {
            VisitRequestEntity request = new VisitRequestEntity(
                    0, propertyId, name, phone, visitDate, visitTime,
                    message, "Pending", System.currentTimeMillis()
            );
            visitRequestDao.insertVisitRequest(request);
            if (onComplete != null) {
                executors.mainThread().execute(onComplete);
            }
        });
    }

    public LiveData<List<VisitRequestWithProperty>> getAllVisitRequests() {
        return visitRequestDao.getAllVisitRequests();
    }

    public void cancelVisitRequest(int requestId) {
        executors.diskIO().execute(() -> visitRequestDao.cancelVisitRequest(requestId));
    }

    public LiveData<Integer> getPendingRequestsCount() {
        return visitRequestDao.getPendingRequestsCount();
    }
}
