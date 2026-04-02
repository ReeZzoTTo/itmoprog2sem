package com.andreysankov.itmoprog2sem.server.managers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.LinkedHashSet;

import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.andreysankov.itmoprog2sem.common.util.LabWorkWrapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class FileManager {
    private String filename;

    public FileManager(String filename) {
        this.filename = filename;
    } 

    public String getFileName() {
        return this.filename;
    }
    
    public FileTime getFileCreationTime() {
        try {
            Path path = Paths.get(this.filename);
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            
            return attributes.creationTime();
        } catch (IOException e) {
            System.out.println("Ошибка получения даты создания файла: " + e.getMessage());
            return null;
        }
    }

    public void saveFile(LinkedHashSet<LabWork> collection) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        LabWorkWrapper wrapper = new LabWorkWrapper(collection);

        File file = new File(this.filename);
        File parentDir = file.getParentFile();

        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        xmlMapper.writeValue(new OutputStreamWriter(new FileOutputStream(this.filename), StandardCharsets.UTF_8), wrapper);
    }

    public LinkedHashSet<LabWork> readFile() {
        File file = new File(this.filename);

        if (!file.exists()) {
            return new LinkedHashSet<>();
        }
        else {
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                InputStreamReader reader = new InputStreamReader(bis, StandardCharsets.UTF_8);
                
                XmlMapper xmlMapper = new XmlMapper();
                LabWorkWrapper labWorkWrapper = xmlMapper.readValue(reader, LabWorkWrapper.class);

                return labWorkWrapper.getLabWork();
            } catch (IOException e) {
                e.printStackTrace();
                //*  System.out.println("Ошибка чтения файла: " + e.getMessage()); --- может это в Логах делать            
                // return null;
                return new LinkedHashSet<>();
            }
        }
    }
}
