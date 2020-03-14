package com.teamplanner.rest.service;

import java.util.List;

import com.teamplanner.rest.model.User;

public interface UserService {
	
	public List<User> findAll();

	public User findById(String id);
	
	public void save(User user);
	
}
