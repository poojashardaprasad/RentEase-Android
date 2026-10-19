package com.example.mobilecomp4b.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mobilecomp4b.data.local.entity.VisitRequestEntity;
import com.example.mobilecomp4b.data.local.model.VisitRequestWithProperty;

import java.util.List;

@Dao
public interface VisitRequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertVisitRequest(VisitRequestEntity request);

    @Transaction
    @Query("SELECT * FROM visit_requests ORDER BY createdAt DESC")
    LiveData<List<VisitRequestWithProperty>> getAllVisitRequests();

    @Query("UPDATE visit_requests SET status = 'Cancelled' WHERE id = :requestId")
    void cancelVisitRequest(int requestId);

    @Query("UPDATE visit_requests SET status = 'Accepted' WHERE id = :requestId")
    void acceptVisitRequest(int requestId);

    @Query("SELECT COUNT(*) FROM visit_requests WHERE status = 'Pending'")
    LiveData<Integer> getPendingRequestsCount();
}
