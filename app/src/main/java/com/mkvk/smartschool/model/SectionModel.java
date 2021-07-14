package com.mkvk.smartschool.model;

import java.util.ArrayList;

public class SectionModel {

    private String section_title;

    public String getSection_title() {
        return section_title;
    }

    public void setSection_title(String section_title) {
        this.section_title = section_title;
    }

    ArrayList<LessonModel> lessons = new ArrayList<>();

    public ArrayList<LessonModel> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<LessonModel> lessons) {
        this.lessons = lessons;
    }
}
