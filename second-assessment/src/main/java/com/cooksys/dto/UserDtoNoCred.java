package com.cooksys.dto;

import java.util.Date;

import com.cooksys.entity.Profile;

public class UserDtoNoCred {

	Profile profile;
	
	Date joined;
	
	Boolean deleted;

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
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
}
