package com.cooksys.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.dto.TweetDelDto;
import com.cooksys.dto.TweetDto;
import com.cooksys.dto.UserDelDto;
import com.cooksys.dto.UserDtoNoCred;
import com.cooksys.entity.Credential;
import com.cooksys.entity.Hashtag;
import com.cooksys.entity.Tweet;
import com.cooksys.entity.UserEnt;
import com.cooksys.service.TweetService;

@RestController
@RequestMapping("tweets")
public class TweetController {
	
	private TweetService tweetService;

	public TweetController(TweetService tweetService) {
		this.tweetService = tweetService;
	}
	
	@GetMapping("tweets")
	public List<Tweet> findAllTweet() {
		return tweetService.findAllTweet();
	}
	
	@PostMapping("tweets")
	public Tweet postTweet(@RequestBody TweetDto tweet) {
		return tweetService.postTweet(tweet);
	}
	
	@GetMapping("/{id}")
	public Tweet getTweetId(@PathVariable Integer id) {
		return tweetService.getTweetId(id);
	}
	
	@DeleteMapping("/{id}")
	public Tweet deleteTweet(@RequestBody TweetDelDto tweet, @PathVariable Integer id) {
		return tweetService.deleteTweet(tweet, id);
	}
	
	@PostMapping("/{id}/like")
	public void postLike(@RequestBody TweetDelDto tweet, @PathVariable Integer id) {
		tweetService.postLike(tweet, id);
	}
	
	@GetMapping("/{id}/likes")
	public List<UserDtoNoCred> getLikeUsers(@PathVariable Integer id) {
		return tweetService.getLikeUsers(id);
	}
	
	@GetMapping("/{id}/mentions")
	public List<UserDtoNoCred> getTweetMentions(@PathVariable Integer id) {
		return tweetService.getTweetMentions(id);
	}
	
	@GetMapping("/{id}/tags")
	public List<Hashtag> getTweetTags(@PathVariable Integer id) {
		return tweetService.getTweetTags(id);
	}
	
	@PostMapping("/{id}/reply")
	public Tweet postReply(@RequestBody TweetDto tweet, @PathVariable Integer id) {
		return tweetService.postReply(tweet, id);
	}
	
	@GetMapping("/{id}/replies")
	public List<Tweet> getTweetReplies(@PathVariable Integer id) {
		return tweetService.getTweetReplies(id);
	}
	
	@PostMapping("/{id}/repost")
	public Tweet postRepost(@RequestBody TweetDelDto tweet, @PathVariable Integer id) {
		return tweetService.postRepost(tweet, id);
	}
	
	@GetMapping("/{id}/reposts")
	public List<Tweet> getTweetReposts(@PathVariable Integer id) {
		return tweetService.getTweetReposts(id);
	}
}
 