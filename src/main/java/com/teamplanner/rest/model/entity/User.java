package com.teamplanner.rest.model.entity;

import javax.persistence.*;
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
	
	@Column
	private String nickname;
	
	@Column
	private int enabled = 1;
	
	@Column
	private String role = "ROLE_UNCONFIGURED";

	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
	List<GamePlan> gamePlans;
	
	public User() {
	}

	public User(String googlesub, String name, String email) {
		
		this.googlesub = googlesub;
		this.name = name;
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "User [googlesub=" + googlesub + ", name=" + name + ", email=" + email + "]";
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
