package com.cooksys.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.cooksys.dto.TweetDelDto;
import com.cooksys.dto.TweetDto;
import com.cooksys.dto.UserDelDto;
import com.cooksys.dto.UserDtoNoCred;
import com.cooksys.entity.Credential;
import com.cooksys.entity.Hashtag;
import com.cooksys.entity.Tweet;
import com.cooksys.entity.UserEnt;
import com.cooksys.mapper.TweetMapper;
import com.cooksys.mapper.UserMapper;
import com.cooksys.repository.HashtagRepository;
import com.cooksys.repository.TweetRepository;
import com.cooksys.repository.UserRepository;

@Service
public class TweetService {
	
	Logger log = LoggerFactory.getLogger(UserService.class);
	private TweetRepository tRepository;
	private UserRepository uRepository;
	private HashtagRepository hRepository;
	private TweetMapper mapper;
	private UserMapper uMapper;
	
	public TweetService(TweetRepository tRepository, UserRepository uRepository, HashtagRepository hRepository, TweetMapper mapper, UserMapper uMapper) {
		this.tRepository = tRepository;
		this.uRepository = uRepository;
		this.hRepository = hRepository;
		this.mapper = mapper;
		this.uMapper = uMapper;
	}
	
	public List<Tweet> findAllTweet() {
		return tRepository.findByDeletedFalseOrderByPostedDesc();
	}
	
	public Tweet postTweet(@RequestBody TweetDto tweet) {
		UserEnt author = uRepository.findByCredentialUserNameAndDeletedFalse(tweet.getCredential().getUserName());
		if (author == null) {
			log.info("User not found");
			return null;
		}
		Tweet newTweet = new Tweet();
		newTweet.setContent(tweet.getContent());
		newTweet.setAuthor(author);
		tRepository.save(newTweet);
		
		List <UserEnt> mentions = new ArrayList<>();
				
		String[] words = tweet.getContent().split(" ");
		String username = new String();
		
		for(String word : words) {
			if(word.charAt(0) == '#') {
				Hashtag newtag = hRepository.findByLabel(word.substring(1));
				if (newtag == null) {
					Hashtag saveTag = new Hashtag();
					saveTag.setLabel(word.substring(1));
					saveTag.getTweetTags().add(newTweet);
					hRepository.save(saveTag);
				} else {
					newtag.setLastUsed(new Date());
					newtag.getTweetTags().add(newTweet);
					hRepository.save(newtag);
				}
			} else if (word.charAt(0) == '@') {
				username = word.substring(1);
				UserEnt userMentioned = uRepository.findByCredentialUserNameAndDeletedFalse(username);
				
				if(userMentioned != null) {
					mentions.add(userMentioned);
					userMentioned.getMentioned().add(newTweet);
					uRepository.save(userMentioned);
				}			
			}
		}
		newTweet.setUsersMentioned(mentions);
		tRepository.save(newTweet);
		return newTweet;
	}
	
	public Tweet getTweetId(Integer id) {
		Tweet tweet = tRepository.findByIdAndDeletedFalse(id);
		if (tweet == null) {
			log.info("Tweet not found");
			return null;
		} else {
			return tweet;
		}
	}
	
	public Tweet deleteTweet(TweetDelDto tweet, Integer id) {
		UserEnt delUser = uRepository.findByCredentialUserNameAndCredentialPasswordEquals(tweet.getCredential().getUserName(), tweet.getCredential().getPassword());
		if (delUser == null) {
			log.info("User not found");
			return null;
		}
		Tweet delTweet = mapper.toEntity(tweet);
		if (delTweet == null) {
			log.info("Tweet not found");
			return null;
		} else {
			delTweet.setDeleted(true);
		}
		return delTweet;
	}
	
	public void postLike(TweetDelDto tweet, Integer id) {
		UserEnt likeUser = uRepository.findByCredentialUserNameAndCredentialPasswordEquals(tweet.getCredential().getUserName(), tweet.getCredential().getPassword());
		if (likeUser == null) {
			log.info("User not found");
			return;
		}
		Tweet likeTweet = tRepository.findByIdAndDeletedFalse(id);
		if (likeTweet.getDeleted() || likeTweet == null) {
			log.info("Tweet not found");
			return;
		}
		likeUser.getLikes().add(likeTweet);
		uRepository.save(likeUser);
		likeTweet.getUserLikes().add(likeUser);
		tRepository.save(likeTweet);
		return;
	}
	
	public Tweet postReply(TweetDto tweet, Integer id) {
		UserEnt likeUser = uRepository.findByCredentialUserNameAndCredentialPasswordEquals(tweet.getCredential().getUserName(), tweet.getCredential().getPassword());
		if (likeUser == null) {
			log.info("User not found");
			return null;
		}
		Tweet replyToTweet = tRepository.findByIdAndDeletedFalse(id);
		if (replyToTweet == null) {
			log.info("Tweet not found");
			return null;
		}
		if (tweet.getContent() == null) {
			log.info("Tweet must have content");
			return null;
		}
		Tweet replyTweet = new Tweet();
		replyTweet.setAuthor(likeUser);
		replyTweet.setContent(tweet.getContent());
		replyTweet.setRepliesto(replyToTweet);
		tRepository.save(replyTweet);
		replyToTweet.getReplies().add(replyTweet);
		tRepository.save(replyToTweet);
		return replyTweet;
	}
	
	public List<Tweet> getTweetReplies(Integer id) {
		Tweet replyToTweet = tRepository.findByIdAndDeletedFalse(id);
		if (replyToTweet == null) {
			log.info("Tweet not found");
			return null;
		}
		List<Tweet> returnTweets = new ArrayList<>();
		for (Tweet tweet : replyToTweet.getReplies()) {
			if (!tweet.getDeleted()) {
				returnTweets.add(tweet);
			}
		}
		return returnTweets;
	}
	
	public Tweet postRepost(TweetDelDto tweet, Integer id) {
		UserEnt repostUser = uRepository.findByCredentialUserNameAndCredentialPasswordEquals(tweet.getCredential().getUserName(), tweet.getCredential().getPassword());
		if (repostUser == null) {
			log.info("User not found");
			return null;
		}
		Tweet repostToTweet = tRepository.findByIdAndDeletedFalse(id);
		if (repostToTweet == null) {
			log.info("Tweet not found");
			return null;
		}
		Tweet repostTweet = new Tweet();
		repostTweet.setAuthor(repostUser);
		repostTweet.setRepostof(repostToTweet);
		tRepository.save(repostTweet);
		repostToTweet.getReposts().add(repostTweet);
		tRepository.save(repostToTweet);
		return repostTweet;
	}
	
	public List<Tweet> getTweetReposts(@PathVariable Integer id) {
		Tweet repostToTweet = tRepository.findByIdAndDeletedFalse(id);
		if (repostToTweet == null) {
			log.info("Tweet not found");
			return null;
		}
		List<Tweet> returnTweets = new ArrayList<>();
		for (Tweet tweet : repostToTweet.getReposts()) {
			if (!tweet.getDeleted()) {
				returnTweets.add(tweet);
			}
		}
		return returnTweets;
	}
	
	public List<UserDtoNoCred> getLikeUsers(Integer id) {
		Tweet likedTweet = tRepository.findByIdAndDeletedFalse(id);
		if (likedTweet == null) {
			log.info("Tweet not found");
			return null;
		}
		List<UserEnt> usersLiked = likedTweet.getUserLikes();
		List<UserDtoNoCred> userReturn = new ArrayList<>();
		for(UserEnt user : usersLiked) {
			userReturn.add(uMapper.toDtoNoCred(user));
		}
		return userReturn;
	}
	
	public List<UserDtoNoCred> getTweetMentions(Integer id) {
		Tweet mentTweet = tRepository.findByIdAndDeletedFalse(id);
		if (mentTweet == null) {
			log.info("Tweet not found");
			return null;
		}
		List<UserEnt> usersMent = mentTweet.getUsersMentioned();
		List<UserDtoNoCred> userReturn = new ArrayList<>();
		for(UserEnt user : usersMent) {
			userReturn.add(uMapper.toDtoNoCred(user));
		}
		return userReturn;
	}
	
	public List<Hashtag> getTweetTags(Integer id) {
		Tweet tagTweet = tRepository.findByIdAndDeletedFalse(id);
		if (tagTweet == null) {
			log.info("Tweet not found");
			return null;
		}
		
		return tagTweet.getTags();
	}

}
