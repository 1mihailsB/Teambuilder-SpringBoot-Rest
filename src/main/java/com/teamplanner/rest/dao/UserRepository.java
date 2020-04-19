package com.teamplanner.rest.dao;

import com.teamplanner.rest.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>{

    User findByGooglesub(String username);

    Optional<User> findByNickname(String nickname);
}
