package com.andreysankov.itmoprog2sem.managers;

import java.io.File;

public class FileManager {
    private String filename;

    public FileManager(String filename) {
        if (!(new File(filename).exists())) {
            filename = "../" + filename;
        }
        this.filename = filename;
    } 

    public String getFileName() {
        return this.filename;
    }
    
}
