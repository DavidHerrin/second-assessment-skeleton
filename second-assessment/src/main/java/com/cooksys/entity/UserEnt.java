package com.cooksys.entity;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cooksys.entity.Credential;
import com.cooksys.entity.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="userents")
public class UserEnt {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Profile profile;
	
	private Credential credential;
	
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Date joined = new Date();
	
	private Boolean deleted = false;
	
	@ManyToMany
	private List<UserEnt> following;
	
	@ManyToMany(mappedBy="following")
	private List<UserEnt> followers;
	
	@OneToMany(mappedBy = "author")
	@JsonIgnore
	private List<Tweet> tweets;
	
	@ManyToMany
	@JsonIgnore
	private List<Tweet> mentioned = new ArrayList<>();
	
	@ManyToMany(mappedBy = "userLikes")
	@JsonIgnore
	private List<Tweet> likes = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

	public Date getJoined() {
		return joined;
	}

	public void setJoined(Date joined) {
		this.joined = joined;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public List<UserEnt> getFollowing() {
		return following;
	}

	public void setFollowing(List<UserEnt> following) {
		this.following = following;
	}

	public List<UserEnt> getFollowers() {
		return followers;
	}

	public void setFollowers(List<UserEnt> followers) {
		this.followers = followers;
	}

	public List<Tweet> getMentioned() {
		return mentioned;
	}

	public void setMentioned(List<Tweet> mentioned) {
		this.mentioned = mentioned;
	}

	public List<Tweet> getLikes() {
		return likes;
	}

	public void setLikes(List<Tweet> likes) {
		this.likes = likes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEnt other = (UserEnt) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
