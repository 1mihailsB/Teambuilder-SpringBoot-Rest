package com.teamplanner.rest.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamplanner.rest.model.User;

public interface UserRepository extends JpaRepository<User, String>{

}
