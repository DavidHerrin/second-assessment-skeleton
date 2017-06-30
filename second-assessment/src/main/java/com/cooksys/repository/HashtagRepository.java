package com.cooksys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.entity.Hashtag;


public interface HashtagRepository extends JpaRepository<Hashtag, Long>{
	
	Hashtag findByLabel(String label);

}
