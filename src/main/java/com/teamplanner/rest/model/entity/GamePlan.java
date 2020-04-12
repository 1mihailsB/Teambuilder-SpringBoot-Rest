package com.teamplanner.rest.model.entity;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name="gameplans")
public class GamePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column(name="main_text")
    private String mainText;

    @Column(name="creation_datetime", columnDefinition = "TIMESTAMP")
    private ZonedDateTime creationDateTime;

    @ManyToOne
    @JoinColumn(name = "author_googlesub")
    User author;

    public GamePlan() {
    }

    public GamePlan(String title, String mainText, ZonedDateTime creationDateTime) {
        this.title = title;
        this.mainText = mainText;
        this.creationDateTime = creationDateTime;
    }

    @Override
    public String toString() {
        return "GamePlan{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", mainText='" + mainText + '\'' +
                ", creationDateTime=" + creationDateTime +
                ", author=" + author +
                '}';
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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

    public ZonedDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(ZonedDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}
