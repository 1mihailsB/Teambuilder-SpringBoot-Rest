package com.teamplanner.rest.model.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name="gameplan_members", joinColumns = @JoinColumn(name="gameplan_id"), inverseJoinColumns = @JoinColumn(name="member_user_googlesub"))
    @Where(clause = "status = 1")
    List<GameplanMember> gameMembers;

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

    public List<GameplanMember> getGameMembers() {
        return gameMembers;
    }

    public void addGameMember(GameplanMember gameplanMember){
        if(gameMembers != null){
            gameMembers = new ArrayList<>();
        }
        gameMembers.add(gameplanMember);
    }

    public void setGameMembers(List<GameplanMember> gameMembers) {
        this.gameMembers = gameMembers;
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
