package com.cooksys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.entity.Tweet;
import com.cooksys.entity.UserEnt;

public interface TweetRepository extends JpaRepository<Tweet, Long>{

	List<Tweet> findByDeletedFalseOrderByPostedDesc();
	
	Tweet findByIdAndDeletedFalse(Integer id);
	
	List<Tweet> findByAuthorAndDeletedFalse(String author);
	
}
