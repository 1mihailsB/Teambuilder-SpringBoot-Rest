package com.teamplanner.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamplanner.rest.dao.UserRepository;
import com.teamplanner.rest.model.User;

@Service
public class UserServiceImpl implements UserService{

	private UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public List<User> findAll() {
		
		return userRepository.findAll();
	}

	@Override
	public User findById(String id) {
		Optional<User> result = userRepository.findById(id);
		
		User user;
		
		if(result.isPresent()) {
			user = result.get();
		}else {
			user = null;
		}
		return user;
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}
	
	

}
