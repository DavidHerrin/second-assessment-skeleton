package com.cooksys.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.cooksys.repository.HashtagRepository;
import com.cooksys.dto.UserDtoNoCred;
import com.cooksys.entity.Hashtag;
import com.cooksys.entity.Tweet;
import com.cooksys.entity.UserEnt;
import com.cooksys.service.UserService;

@Service
public class HashtagService {
	
	Logger log = LoggerFactory.getLogger(UserService.class);
	private HashtagRepository hRepository;
	public HashtagService(HashtagRepository hRepository) {
		this.hRepository = hRepository;
	}
	
	public List<Hashtag> findAllHashtag() {
		return hRepository.findAll();
	}
	
	public List<Tweet> getTagsByLabel(String label) {
		Hashtag tagToCheck = hRepository.findByLabel(label);
		if (tagToCheck == null) {
			log.info("Tag not found");
			return null;
		}
		List<Tweet> taggedTweets = tagToCheck.getTweetTags();
		List<Tweet> tweetReturn = new ArrayList<>();
		for(Tweet tweet : taggedTweets) {
			if (!tweet.getDeleted()) {
				tweetReturn.add(tweet);
			}
		}
		tweetReturn.sort(Comparator.comparing(Tweet::getPosted).reversed());
		return tweetReturn;
	}
	
	public Boolean getTagExists(String label) {
		Hashtag searchTag = hRepository.findByLabel(label);
		return !(searchTag == null);
	}

} 
