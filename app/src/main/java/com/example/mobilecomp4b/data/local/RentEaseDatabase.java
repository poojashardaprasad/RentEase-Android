package com.example.mobilecomp4b.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mobilecomp4b.data.local.dao.FavoriteDao;
import com.example.mobilecomp4b.data.local.dao.InquiryDao;
import com.example.mobilecomp4b.data.local.dao.PropertyDao;
import com.example.mobilecomp4b.data.local.dao.UserProfileDao;
import com.example.mobilecomp4b.data.local.dao.VisitRequestDao;
import com.example.mobilecomp4b.data.local.entity.FavoriteEntity;
import com.example.mobilecomp4b.data.local.entity.InquiryEntity;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.data.local.entity.UserProfileEntity;
import com.example.mobilecomp4b.data.local.entity.VisitRequestEntity;

@Database(
    entities = {
        PropertyEntity.class,
        FavoriteEntity.class,
        VisitRequestEntity.class,
        InquiryEntity.class,
        UserProfileEntity.class
    },
    version = 2,
    exportSchema = false
)
public abstract class RentEaseDatabase extends RoomDatabase {

    private static volatile RentEaseDatabase INSTANCE;

    public abstract PropertyDao propertyDao();
    public abstract FavoriteDao favoriteDao();
    public abstract VisitRequestDao visitRequestDao();
    public abstract InquiryDao inquiryDao();
    public abstract UserProfileDao userProfileDao();

    public static RentEaseDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RentEaseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RentEaseDatabase.class,
                            "RentEaseDatabase"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
