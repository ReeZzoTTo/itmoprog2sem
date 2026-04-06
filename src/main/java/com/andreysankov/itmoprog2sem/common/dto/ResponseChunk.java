package com.andreysankov.itmoprog2sem.common.dto;

import java.io.Serializable;

public class ResponseChunk implements Serializable {
    private final String requestId;
    private final boolean success;
    private final int partIndex;
    private final int totalParts;
    private final String payload;

    public ResponseChunk(String requestId, boolean success, int partIndex, int totalParts, String payload) {
        this.requestId = requestId;
        this.success = success;
        this.partIndex = partIndex;
        this.totalParts = totalParts;
        this.payload = payload;
    }

    public String getRequestId() {
        return requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getPartIndex() {
        return partIndex;
    }

    public int getTotalParts() {
        return totalParts;
    }

    public String getPayload() {
        return payload;
    }
}