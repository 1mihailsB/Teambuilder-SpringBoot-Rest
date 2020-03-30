package com.teamplanner.rest.service;

import com.teamplanner.rest.model.User;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface UserService {
	
	public List<User> findAll();

	public User findById(String id);
	
	public User save(User user);
	
}
