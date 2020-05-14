package com.teamplanner.rest.model.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
public class User {

	/** format of "sub" field of Google ID token is described here:
	 https://developers.google.com/identity/protocols/oauth2/openid-connect#an-id-tokens-payload **/
	@Id
	private String googlesub;
	
	@Column
	private String name;
	
	@Column
	private String email;

	@Column(name="creation_datetime", columnDefinition = "TIMESTAMP")
	private ZonedDateTime creationDateTime;

	@Column
	private String nickname;
	
	@Column
	private int enabled = 1;
	
	@Column
	private String role = "ROLE_UNCONFIGURED";

	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
	List<GamePlan> gamePlans;

	@OneToMany(mappedBy = "invitingUser",fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@Where(clause = "status = 1")
	List<Friendship> initiatedFriendships;

	@OneToMany(mappedBy = "invitingUser",fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@Where(clause = "status = 0")
	List<Friendship> outgoingFriendRequests;

	@OneToMany(mappedBy = "invitedUser", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@Where(clause = "status = 1")
	List<Friendship> invitedToFriendships;

	@OneToMany(mappedBy = "invitedUser", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@Where(clause = "status = 0")
	List<Friendship> incomingFriendRequests;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="gameplan_members", joinColumns = @JoinColumn(name="member_user_googlesub"), inverseJoinColumns = @JoinColumn(name="gameplan_id"))
	@Where(clause = "status = 0")
	List<GameplanMember> gameInvitations;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="gameplan_members", joinColumns = @JoinColumn(name="member_user_googlesub"), inverseJoinColumns = @JoinColumn(name="gameplan_id"))
	@Where(clause = "status = 1")
	List<GameplanMember> acceptedGameInvitations;

	public User() {
	}

	public User(String googlesub, String name, String email, ZonedDateTime creationDateTime) {
		this.googlesub = googlesub;
		this.name = name;
		this.email = email;
		this.creationDateTime = creationDateTime;
	}

	@Override
	public String toString() {
		return "User{" +
				"googlesub='" + googlesub + '\'' +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", nickname='" + nickname + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object user){

		if(user == this){return true;}
		if(!(user instanceof User)){return false;}

		return this.googlesub.equals(((User) user).getGooglesub());
	}

	public List<GameplanMember> getAcceptedGameInvitations() {
		return acceptedGameInvitations;
	}



	public List<GameplanMember> getGameInvitations() {
		return gameInvitations;
	}

	public List<Friendship> getOutgoingFriendRequests() {
		return outgoingFriendRequests;
	}


	public List<Friendship> getIncomingFriendRequests() {
		return incomingFriendRequests;
	}

	public void addIncomingFriendRequests(Friendship friendship){
		if(incomingFriendRequests == null){
			incomingFriendRequests = new ArrayList<>();
		}
		incomingFriendRequests.add(friendship);
	}

	public void setIncomingFriendRequests(List<Friendship> incomingFriendRequests) {
		this.incomingFriendRequests = incomingFriendRequests;
	}

	public List<Friendship> getInitiatedFriendships() {
		return initiatedFriendships;
	}

	public void addInitiatedFriendship(Friendship friendship){
		if(initiatedFriendships == null){
			initiatedFriendships = new ArrayList<>();
		}
		initiatedFriendships.add(friendship);
	}

	public void setInitiatedFriendships(List<Friendship> initiatedFriendships) {
		this.initiatedFriendships = initiatedFriendships;
	}

	public List<Friendship> getInvitedToFriendships() {
		return invitedToFriendships;
	}

	public void addInvitedToFriendship(Friendship friendship){
		if(invitedToFriendships == null){
			invitedToFriendships = new ArrayList<>();
		}
		invitedToFriendships.add(friendship);
	}

	public void setInvitedToFriendships(List<Friendship> invitedToFriendships) {
		this.invitedToFriendships = invitedToFriendships;
	}

	public String getGooglesub() {
		return googlesub;
	}

	public void setGooglesub(String googlesub) {
		this.googlesub = googlesub;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ZonedDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(ZonedDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<GamePlan> getGamePlans() {
		return gamePlans;
	}

	public void setGamePlans(List<GamePlan> gamePlans) {
		this.gamePlans = gamePlans;
	}
}
