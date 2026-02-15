package com.andreysankov.itmoprog2sem.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;

import com.andreysankov.itmoprog2sem.models.LabWork;
import com.andreysankov.itmoprog2sem.util.LabWorkWrapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class FileManager {
    private String filename;

    public FileManager(String filename) {
        if (!new File(filename).exists()) {  }
        this.filename = filename;
    } 

    public String getFileName() {
        return this.filename;
    }
    
    public void saveFile(LinkedHashSet<LabWork> collection) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        LabWorkWrapper wrapper = new LabWorkWrapper(collection);

        File file = new File(this.filename);
        File parentDir = file.getParentFile();

        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        xmlMapper.writeValue(new OutputStreamWriter(new FileOutputStream(this.filename), StandardCharsets.UTF_8), wrapper);
    }
}
