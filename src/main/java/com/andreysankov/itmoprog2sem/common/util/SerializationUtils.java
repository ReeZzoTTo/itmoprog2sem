package com.andreysankov.itmoprog2sem.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationUtils {
    private SerializationUtils() {}

    public static byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(object);
            objectStream.flush();

            return byteStream.toByteArray();
        }
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);

        try (ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
            return objectStream.readObject();
        }
    }
}
