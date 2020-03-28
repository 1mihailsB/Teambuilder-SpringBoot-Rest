package com.teamplanner.rest.service;

import com.teamplanner.rest.model.User;

import java.util.List;



public interface UserService {
	
	public List<User> findAll();

	public User findById(String id);
	
	public void save(User user);
	
}
