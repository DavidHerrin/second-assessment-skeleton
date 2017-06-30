package com.cooksys.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.cooksys.repository.TweetRepository;
import com.cooksys.repository.UserRepository;
import com.cooksys.dto.UserDelDto;
import com.cooksys.dto.UserDto;
import com.cooksys.dto.UserDtoNoCred;
import com.cooksys.entity.Tweet;
import com.cooksys.entity.UserEnt;

import com.cooksys.mapper.UserMapper;

@Service
public class UserService {
	
	Logger log = LoggerFactory.getLogger(UserService.class);
	private UserRepository uRepository;
	private TweetRepository tRepository;
	private UserMapper mapper;
	
	public UserService(UserRepository uRepository, TweetRepository tRepository, UserMapper mapper) {
		this.uRepository = uRepository;
		this.tRepository = tRepository;
		this.mapper = mapper;
	}
	
	public List<UserDtoNoCred> findAllUser() {
		List<UserEnt> users = uRepository.findByDeletedFalse();
		List<UserDtoNoCred> returnUsers = new ArrayList<>();
		for (UserEnt user : users) {
			if (!user.getDeleted()) returnUsers.add(mapper.toDtoNoCred(user));
		}
		return returnUsers;
	}
	
	public UserEnt patchUser(UserDto userDto) {
		UserEnt user = uRepository.findByCredentialUserName(userDto.getCredential().getUserName());
		if (user == null) {
			log.info("User not found");
			return user;
		}
		
		UserEnt dtoUser = uRepository.findByCredentialUserNameAndCredentialPasswordEquals(userDto.getCredential().getUserName(), userDto.getCredential().getPassword());
		if (dtoUser != null) {
			user.setProfile(mapper.toEntity(userDto).getProfile());
			return uRepository.save(user);
		} else {
			log.info("Invalid credentials");
			return null;
		}
	}
	
	public UserEnt postUser(UserDto userDto) {
		UserEnt user = uRepository.findByCredentialUserName(userDto.getCredential().getUserName());
		if (user == null) {
			if (userDto.getCredential().getUserName() != null && userDto.getCredential().getPassword() != null
					                                          && userDto.getProfile().getEmail() != null) {
				return uRepository.save(mapper.toEntity(userDto));
			} else {
				log.info("Fields may not be null");
				return null;
			}
		} else {
			if (user.getDeleted()) {
				UserEnt dtoUser = uRepository.findByCredentialUserNameAndCredentialPasswordEquals(userDto.getCredential().getUserName(), userDto.getCredential().getPassword());
				if (dtoUser != null) {
					user.setDeleted(false);
					return uRepository.save(user);
				} else {
					log.info("Invalid credentials");
					return null;
				}
			} else {
				log.info("User already exists");
				return null;
			}	
		}
		
	}
	
	public UserDto getUser(String username) {
		UserEnt user = uRepository.findByCredentialUserNameAndDeletedFalse(username);
		if (user == null) {
			log.info("User not found");
			return null;
		} else {
			return mapper.toDto(user);
		}
	}
	
	public Boolean getUserExists(String username) {
		UserEnt user = uRepository.findByCredentialUserNameAndDeletedFalse(username);
		if (user == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public Boolean getUserAvail(String username) {
		UserEnt user = uRepository.findByCredentialUserName(username);
		if (user == null) {
			return true;
		} else {
			return false;
		}
	}
	
	public UserEnt deleteUser(UserDelDto userDelDto) {
		UserEnt user = uRepository.findByCredentialUserNameAndDeletedFalse(userDelDto.getCredential().getUserName());
		if (user == null) {
			log.info("User not found");
			return null;
		}
		UserEnt dtoUser = uRepository.findByCredentialUserNameAndCredentialPasswordEquals(userDelDto.getCredential().getUserName(), userDelDto.getCredential().getPassword());
		if (dtoUser != null && !dtoUser.getDeleted()) {
			user.setDeleted(true);
			uRepository.save(user);
		}
		
		return user;
	}
	
	public List<Tweet> authorTweets(String author) {
		UserEnt user = uRepository.findByCredentialUserName(author);
		if (user == null) {
			log.info("User not found");
			return null;
		}
		List<Tweet> authorTweets = user.getTweets();
		List<Tweet> tweetReturn = new ArrayList<>();
		for (Tweet tweet : authorTweets) {
			if (!tweet.getDeleted()) {
				tweetReturn.add(tweet);
			}
		}
		tweetReturn.sort(Comparator.comparing(Tweet::getPosted).reversed());
		return tweetReturn;
	}
	
	public void followUser(UserDelDto userDelDto, String username) {
		UserEnt userFollowing = uRepository.findByCredentialUserNameAndCredentialPasswordEquals(userDelDto.getCredential().getUserName(), userDelDto.getCredential().getPassword());
		UserEnt userFollowed = uRepository.findByCredentialUserNameAndDeletedFalse(username);
		if (userFollowing == null || userFollowed == null) {
			log.info("User not found");
			return;
		}
		if (userFollowed.getFollowers().contains(userDelDto.getCredential().getUserName())) {
			log.info("User already followed");
			return;
		}
		userFollowed.getFollowers().add(userFollowing);
		uRepository.save(userFollowed);
		userFollowing.getFollowing().add(userFollowed);
		uRepository.save(userFollowing);
		return;
	}
	
	public void unFollowUser(UserDelDto userDelDto, String username) {
		UserEnt userFollowing = uRepository.findByCredentialUserNameAndCredentialPasswordEquals(userDelDto.getCredential().getUserName(), userDelDto.getCredential().getPassword());
		UserEnt userFollowed = uRepository.findByCredentialUserNameAndDeletedFalse(username);
		if (userFollowing == null || userFollowed == null) {
			log.info("User not found");
			return;
		}
		if (!userFollowed.getFollowers().contains(userDelDto.getCredential().getUserName())) { 
			userFollowed.getFollowers().remove(userFollowing);
			uRepository.save(userFollowed);
			userFollowing.getFollowing().remove(userFollowed);
			uRepository.save(userFollowing);
		} else {
			log.info("User not being followed");
		}
		return;
	}
	
	public List<UserDtoNoCred> getFollowers(String username) {
		UserEnt followedUser = uRepository.findByCredentialUserName(username);
		if (followedUser == null) {
			log.info("User not found");
			return null;
		}
		List<UserEnt> followerList = followedUser.getFollowers();
		List<UserDtoNoCred> returnUsers = new ArrayList<>();
		for (UserEnt user : followerList) {
			if (!user.getDeleted()) returnUsers.add(mapper.toDtoNoCred(user));
		}
		return returnUsers;
	}
	
	public List<UserDtoNoCred> getFollowing(String username) {
		UserEnt followingUser = uRepository.findByCredentialUserName(username);
		if (followingUser == null) {
			log.info("User not found");
			return null;
		}
		List<UserEnt> followingList = followingUser.getFollowing();
		List<UserDtoNoCred> returnUsers = new ArrayList<>();
		for (UserEnt user : followingList) {
			if (!user.getDeleted()) returnUsers.add(mapper.toDtoNoCred(user));
		}
		return returnUsers;
	}

}
