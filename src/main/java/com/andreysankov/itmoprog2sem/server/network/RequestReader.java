package com.andreysankov.itmoprog2sem.server.network;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.util.SerializationUtils;

public class RequestReader {
    private static final Logger logger = LoggerFactory.getLogger(RequestReader.class);

    public Request read(byte[] data, int length) throws IOException, ClassNotFoundException {
        byte[] actualData = Arrays.copyOf(data, length);
        Object object = SerializationUtils.deserialize(actualData);

        if (!(object instanceof Request request)) {
            logger.warn("Получен объект неверного типа при десериализации запроса");
            throw new IOException("Получен объект неверного типа");
        }

        logger.debug("Запрос успешно десериализован");
        return request;
    }
}
