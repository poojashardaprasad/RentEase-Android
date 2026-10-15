package com.example.mobilecomp4b.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mobilecomp4b.data.local.entity.InquiryEntity;
import com.example.mobilecomp4b.data.local.model.InquiryWithProperty;

import java.util.List;

@Dao
public interface InquiryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertInquiry(InquiryEntity inquiry);

    @Transaction
    @Query("SELECT * FROM inquiries ORDER BY createdAt DESC")
    LiveData<List<InquiryWithProperty>> getAllInquiries();

    @Query("SELECT COUNT(*) FROM inquiries")
    LiveData<Integer> getInquiryCount();
}
