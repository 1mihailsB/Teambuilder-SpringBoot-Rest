package com.teamplanner.rest.service;

import com.teamplanner.rest.dao.UserRepository;
import com.teamplanner.rest.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
	public User findByNickname(String nickname){
		Optional<User> result = userRepository.findByNicknameEquals(nickname);

		User user;

		if(result.isPresent()){
			user = result.get();
		}else{
			user = null;
		}
		return user;
	}

	@Override
	public List<User> findFriendsInvitableToGame(List<String> friendNicknames, int gameId) {
		return userRepository.findFriendsInvitableToGame(friendNicknames, gameId);
	}

	@Override
	public User findById(String googlesub) {
		Optional<User> result = userRepository.findById(googlesub);
		
		User user;
		
		if(result.isPresent()) {
			user = result.get();
		}else {
			user = null;
		}
		return user;
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
}
