package com.cooksys.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.dto.UserDelDto;
import com.cooksys.dto.UserDto;
import com.cooksys.dto.UserDtoNoCred;
import com.cooksys.entity.Tweet;
import com.cooksys.entity.UserEnt;
import com.cooksys.service.UserService;

@RestController
@RequestMapping("userents")
public class UserController {
	
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("users")
	public List<UserDtoNoCred> findAllUser() {
		return userService.findAllUser();
	}
	
	@GetMapping("@{username}")
	public UserDto getUser(@PathVariable String username) {
		return userService.getUser(username);
	}
	
	@GetMapping("validate/username/exists/@{username}")
	public Boolean getUserExists(@PathVariable String username) {
		return userService.getUserExists(username);
	}
	
	@GetMapping("validate/username/available/@{username}")
	public Boolean getUserAvail(@PathVariable String username) {
		return userService.getUserAvail(username);
	}
	
	@PostMapping("user")
    public UserEnt postUser(@RequestBody UserDto user) {
        return userService.postUser(user);
    }
	
	@PatchMapping("@{username}")
	public UserEnt patchUser(@RequestBody UserDto user) {
		return userService.patchUser(user);
	}
	
	@DeleteMapping("@{username}")
	public UserEnt deleteUser(@RequestBody UserDelDto user) {
		return userService.deleteUser(user);
	}
	
	@PostMapping("/@{username}/follow")
	public void followUser(@RequestBody UserDelDto user, @PathVariable String username) {
		userService.followUser(user, username);
	}
	
	@PostMapping("/@{username}/unfollow")
	public void unFollowUser(@RequestBody UserDelDto user, @PathVariable String username) {
		userService.unFollowUser(user, username);
	}
	
	@GetMapping("/@{username}/followers")
	public List<UserDtoNoCred> getFollowers(@PathVariable String username) {
		return userService.getFollowers(username);
	}
	
	@GetMapping("/@{username}/following")
	public List<UserDtoNoCred> getFollowing(@PathVariable String username) {
		return userService.getFollowing(username);
	}
	
	@GetMapping("/@{username}/tweets")
	public List<Tweet> authorTweets(@PathVariable String username) {
		return userService.authorTweets(username);
	}
 
}
