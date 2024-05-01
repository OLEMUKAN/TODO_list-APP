package com.example.todoapp.Model;

import java.io.Serializable;

public class TodoModel implements Serializable {
    private int id;
    private String taskName;
    private String courseName;
    private String courseUnit;
    private long reminder;
    private boolean taskStatus;

    public TodoModel(){
    }

    public TodoModel(int id, String taskName, String courseName, String courseUnit, long reminder, boolean taskStatus) {
        this.id = id;
        this.taskName = taskName;
        this.courseName = courseName;
        this.courseUnit = courseUnit;
        this.reminder = reminder;
        this.taskStatus = taskStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseUnit() {
        return courseUnit;
    }

    public void setCourseUnit(String courseUnit) {
        this.courseUnit = courseUnit;
    }

    public long getReminder() {
        return reminder;
    }

    public void setReminder(long reminder) {
        this.reminder = reminder;
    }

    public boolean isTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }
}