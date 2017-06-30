package com.cooksys.dto;

import com.cooksys.entity.Credential;
import com.cooksys.entity.Profile;

public class UserDto {
	
	Profile profile;
	
	Credential credential;

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
	
}
