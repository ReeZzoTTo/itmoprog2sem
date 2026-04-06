package com.andreysankov.itmoprog2sem.server.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.dto.ResponseChunk;

public class ResponseChunker {
    private static final int CHUNK_SIZE = 12_000;

    public static List<ResponseChunk> split(Response response) {
        String responseMessage = response.getMessage();
        String requestId = UUID.randomUUID().toString();

        List<ResponseChunk> chunkList = new ArrayList<>();
        
        if (responseMessage == null) {
            chunkList.add(new ResponseChunk(requestId, response.isSuccess(), 0, 1, ""));
        }

        int total_parts = (int) Math.ceil((double) responseMessage.length() / CHUNK_SIZE);

        for (int i = 0; i < total_parts; i++) {
            int start_ind = i * CHUNK_SIZE;
            int end_ind = (int) Math.min(start_ind + CHUNK_SIZE, responseMessage.length());

            String partString = responseMessage.substring(start_ind, end_ind);

            chunkList.add(
                new ResponseChunk(requestId, response.isSuccess(), i, total_parts, partString)
            );
        }

        return chunkList;
    }
}
