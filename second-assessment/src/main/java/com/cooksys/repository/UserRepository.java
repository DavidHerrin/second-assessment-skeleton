package com.cooksys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.entity.UserEnt;

public interface UserRepository extends JpaRepository<UserEnt, Long>{

	List<UserEnt> findByDeletedFalse();
	
	UserEnt findByCredentialUserNameAndDeletedFalse(String username);
	
	UserEnt findByCredentialUserName(String username);
	
	UserEnt findByCredentialUserNameAndCredentialPasswordEquals(String username, String password);
	
	
	
}
