package com.teamplanner.rest.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
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
	private String roles = "";
	
	@Column
	private String permissions = "";
	
	

	public User(String googlesub, String name, String email, String nickname, String roles,
			String permissions) {
		
		this.googlesub = googlesub;
		this.name = name;
		this.email = email;
		this.nickname = nickname;
		this.roles = roles;
		this.permissions = permissions;
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

	public List<String> getRoles() {
		if(this.roles.length() > 0) {
			return Arrays.asList(this.roles.split(","));
		}
		return new ArrayList<String>();
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public List<String> getPermissions() {
		if(this.permissions.length() > 0) {
			return Arrays.asList(this.permissions.split(","));
		}
		return new ArrayList<String>();
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	
	
}
