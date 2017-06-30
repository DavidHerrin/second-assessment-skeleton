package com.cooksys.dto;

import com.cooksys.entity.Credential;

public class TweetDto {

	private Credential credential;
	
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}
}