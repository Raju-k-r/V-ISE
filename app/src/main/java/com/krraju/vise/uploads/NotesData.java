package com.krraju.vise.uploads;

public class NotesData {
    private String fileName;
    private String filePath;

    public NotesData(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public NotesData() {
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }
}
