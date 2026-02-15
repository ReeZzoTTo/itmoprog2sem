package com.andreysankov.itmoprog2sem.managers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.LinkedHashSet;

import com.andreysankov.itmoprog2sem.models.LabWork;
import com.andreysankov.itmoprog2sem.util.LabWorkWrapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class FileManager {
    private String filename;

    public FileManager(String filename) {
        if (!new File(filename).exists()) {  }
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
            System.out.println("Файл не найден " + this.filename + "\nБудет создан новый файл. Введите save");
            return new LinkedHashSet<>();
        }
        else {
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                XmlMapper xmlMapper = new XmlMapper();
                LabWorkWrapper labWorkWrapper = xmlMapper.readValue(bis, LabWorkWrapper.class);

                return labWorkWrapper.getLabWork();
            } catch (IOException e) {
                System.out.println("Ошибка чтения файла: " + e.getMessage());
                return new LinkedHashSet<>();
            }
        }
    }
}
