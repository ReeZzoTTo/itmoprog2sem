package com.andreysankov.itmoprog2sem.common.dto;

import java.util.List;

import com.andreysankov.itmoprog2sem.common.models.LabWork;

public class CollectionResponse extends Response { private static final long serialVersionUID = 1L;

    private final List<LabWork> collection;

    public CollectionResponse(boolean success, String message, List<LabWork> collection) {
        super(success, message);
        this.collection = collection;
    }

    public List<LabWork> getCollection() {
        return collection;
    }
    
}
