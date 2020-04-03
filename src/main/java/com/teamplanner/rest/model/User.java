package com.teamplanner.rest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {

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

	public String getGoogleSub() {
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
}
