package com.krraju.vise.attendancemanager;

import java.util.Objects;

public class Attendance {
    private String SubjectName;
    private int noOfClass;
    private int noOfClassAttended;
    private double percentage;

    private Attendance(String subjectName, int noOfClass, int noOfClassAttended, double percentage) {
        SubjectName = subjectName;
        this.noOfClass = noOfClass;
        this.noOfClassAttended = noOfClassAttended;
        this.percentage = percentage;
    }

    Attendance(String subjectName) {
        this(subjectName,0,0,0.0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return SubjectName.equals(that.SubjectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(SubjectName);
    }

    public void add(boolean isPresent){
        if(isPresent){
            this.noOfClass++;
            this.noOfClassAttended++;
            this.percentage = (noOfClassAttended * 100.0) / noOfClass;
        }
        else{
            this.noOfClass++;
            this.percentage = (noOfClassAttended * 100.0) / noOfClass;
        }
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public int getNoOfClass() {
        return noOfClass;
    }

    public int getNoOfClassAttended() {
        return noOfClassAttended;
    }

    public double getPercentage() {
        return percentage;
    }
}
