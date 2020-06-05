package com.krraju.vise.homescreen.announcement;

public class DataClass {
    private String message;
    private String fileType;
    private String fileUri;
    private String dateAndTime;

    public DataClass() {
        // Used to Retrieve Data From fireBase
    }

    public DataClass(String message, String fileType, String fileUri, String dateAndTime) {
        this.message = message;
        this.fileType = fileType;
        this.fileUri = fileUri;
        this.dateAndTime = dateAndTime;
    }

    public String getMessage() {
        return message;
    }
    public String getFileType() {return fileType; }
    public String getFileUri() {
        return fileUri;
    }
    public String getDateAndTime() {
        return dateAndTime;
    }
}
