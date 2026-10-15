package com.example.mobilecomp4b.data.local.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mobilecomp4b.data.local.entity.InquiryEntity;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;

public class InquiryWithProperty {

    @Embedded
    public InquiryEntity inquiry;

    @Relation(
        parentColumn = "propertyId",
        entityColumn = "id"
    )
    public PropertyEntity property;
}
