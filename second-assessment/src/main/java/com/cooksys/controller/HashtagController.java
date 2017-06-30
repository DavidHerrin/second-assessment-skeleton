package com.cooksys.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.entity.Hashtag;
import com.cooksys.entity.Tweet;


@RestController
@RequestMapping("hashtags")
public class HashtagController {
	
	private HashtagService hashtagService;

	public HashtagController(HashtagService hashtagService) {
		this.hashtagService = hashtagService;
	}
	
	@GetMapping("tags")
	public List<Hashtag> findAllHashtag() {
		return hashtagService.findAllHashtag();
	}
	
	@GetMapping("/{label}")
	public List<Tweet> getTagsByLabel(@PathVariable String label) {
		return hashtagService.getTagsByLabel(label);
	}
	
	@GetMapping("validate/tag/exists/{label}")
	public Boolean getTagExists(@PathVariable String label) {
		return hashtagService.getTagExists(label);
	}

}
