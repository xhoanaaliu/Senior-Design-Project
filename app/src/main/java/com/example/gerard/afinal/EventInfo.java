package com.example.gerard.afinal;

public class EventInfo {
    private String event_id;
    private String category;
    private String date;
    private String description;
    private String imageName;
    private String location;
    private String times;
    private String title;
    private String URL;

    public EventInfo() {
    }

    public EventInfo(String event_id, String category, String date, String description, String imageName, String location, String times, String title) {
        this.event_id = event_id;
        this.category = category;
        this.date = date;
        this.description = description;
        this.imageName = imageName;
        this.location = location;
        this.times = times;
        this.title = title;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return times;
    }

    public void setTime(String times) {
        this.times = times;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
