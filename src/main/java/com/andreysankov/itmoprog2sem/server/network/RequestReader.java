package com.andreysankov.itmoprog2sem.server.network;

import java.io.IOException;
import java.util.Arrays;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.util.SerializationUtils;

public class RequestReader {
    public Request read(byte[] data, int length) throws IOException, ClassNotFoundException {
        byte[] actualData = Arrays.copyOf(data, length);
        Object object = SerializationUtils.deserialize(actualData);

        if (!(object instanceof Request request)) {
            throw new IOException("Получен объект неверного типа");
        }

        return request;
    }
}
