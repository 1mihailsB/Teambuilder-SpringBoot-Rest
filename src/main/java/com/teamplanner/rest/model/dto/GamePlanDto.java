package com.teamplanner.rest.model.dto;

import java.time.ZonedDateTime;

public class GamePlanDto {

    public GamePlanDto(){
    }

    private int id;

    private String title;

    private String mainText;

    private String authorNickname;

    private ZonedDateTime creationDateTime;

    @Override
    public String toString() {
        return "GamePlanDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", mainText='" + mainText + '\'' +
                ", authorNickname='" + authorNickname + '\'' +
                ", creationDateTime=" + creationDateTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }

    public ZonedDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(ZonedDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}
