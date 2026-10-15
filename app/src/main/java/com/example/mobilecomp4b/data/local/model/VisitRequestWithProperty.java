package com.example.mobilecomp4b.data.local.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.data.local.entity.VisitRequestEntity;

public class VisitRequestWithProperty {

    @Embedded
    public VisitRequestEntity request;

    @Relation(
        parentColumn = "propertyId",
        entityColumn = "id"
    )
    public PropertyEntity property;
}
