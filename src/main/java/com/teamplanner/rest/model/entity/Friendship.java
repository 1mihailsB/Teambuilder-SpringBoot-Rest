package com.teamplanner.rest.model.entity;

import javax.persistence.*;

@Entity
@Table(name="friendships")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "friend_1_googlesub")
    private User invitingUser;

    @ManyToOne
    @JoinColumn(name = "friend_2_googlesub")
    private User invitedUser;

    /** status: 0 - pending
     *  status: 1 - accepted */
    @Column
    private int status;

    public Friendship() {
    }

    public Friendship(User invitingUser, User invitedUser, int status) {
        this.invitingUser = invitingUser;
        this.invitedUser = invitedUser;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", invitingUser=" + invitingUser +
                ", invitedUser=" + invitedUser +
                ", status=" + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getInvitingUser() {
        return invitingUser;
    }

    public void setInvitingUser(User invitingUser) {
        this.invitingUser = invitingUser;
    }

    public User getInvitedUser() {
        return invitedUser;
    }

    public void setFriend2(User invitedUser) {
        this.invitedUser = invitedUser;
    }

}
