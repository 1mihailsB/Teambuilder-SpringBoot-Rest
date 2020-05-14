package com.teamplanner.rest.model.entity;

import javax.persistence.*;

@Entity
@Table(name="gameplan_members")
public class GameplanMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="member_user_googlesub")
    private User member;

    @ManyToOne
    @JoinColumn(name="gameplan_id")
    private GamePlan gamePlan;

    /** status: 0 - pending
     *  status: 1 - accepted */
    @Column
    private int status;

    public GameplanMember() {}

    public GameplanMember(User member, GamePlan gamePlan, int status) {
        this.member = member;
        this.gamePlan = gamePlan;
        this.status = status;
    }

    @Override
    public String toString() {
        return "GameplanMember{" +
                "id=" + id +
                ", member=" + member +
                ", gamePlan=" + gamePlan +
                ", status=" + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public GamePlan getGamePlan() {
        return gamePlan;
    }

    public void setGamePlan(GamePlan gamePlan) {
        this.gamePlan = gamePlan;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
