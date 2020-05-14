package com.teamplanner.rest.service;

import com.teamplanner.rest.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface UserService {
	
	public List<User> findAll();

	public User findById(String googlesub);
	
	public User save(User user);

	public User findByNickname(String nickname);

	public List<User> findFriendsInvitableToGame(List<String> friendNicknames, int gameId);
}
