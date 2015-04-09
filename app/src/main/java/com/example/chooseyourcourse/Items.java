package com.example.chooseyourcourse;

/**
 * Created by abhishek on 3/31/2015.
 */
public class Items {

    private String Course;

    public Items(String Course)
    {
        this.Course=Course;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }
}
