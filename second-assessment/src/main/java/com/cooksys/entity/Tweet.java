package com.cooksys.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name="tweets")
public class Tweet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String content;
	
	@ManyToOne
	@JoinColumn(nullable = false, insertable = true, updatable = false)
	private UserEnt author;
	
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Date posted = new Date();
	
	private Boolean deleted = false;
	
	@ManyToMany(mappedBy = "tweetTags")
	@JsonIgnore
	private List<Hashtag> tags = new ArrayList<>();
	
	@ManyToMany
	@JsonIgnore
	private List<UserEnt> userLikes = new ArrayList<>();
		
	@ManyToOne
	@JsonIgnore
	private Tweet repliesto;
	
	@OneToMany(mappedBy = "repliesto")
	@JsonIgnore
	private List<Tweet> replies;
	
	@ManyToOne
	@JsonIgnore
	private Tweet repostof;
	
	@OneToMany(mappedBy = "repostof")
	@JsonIgnore
	private List<Tweet> reposts;
	
	@ManyToMany(mappedBy = "mentioned", cascade=CascadeType.ALL)
	@JsonIgnore
	private List<UserEnt> usersMentioned = new ArrayList<>();
 
	public List<UserEnt> getUsersMentioned() {
		return usersMentioned;
	}

	public void setUsersMentioned(List<UserEnt> usersMentioned) {
		this.usersMentioned = usersMentioned;
	}

	public Tweet getRepliesto() {
		return repliesto;
	}

	public void setRepliesto(Tweet repliesto) {
		this.repliesto = repliesto;
	}

	public Tweet getRepostof() {
		return repostof;
	}

	public void setRepostof(Tweet repostof) {
		this.repostof = repostof;
	}

	public List<Tweet> getReposts() {
		return reposts;
	}

	public void setReposts(List<Tweet> reposts) {
		this.reposts = reposts;
	}

	public List<Tweet> getReplies() {
		return replies;
	}

	public void setReplies(List<Tweet> replies) {
		this.replies = replies;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

//	public UserEnt getAuthor() {
//		return author;
//	}

	public void setAuthor(UserEnt author) {
		this.author = author;
	}

	public Date getPosted() {
		return posted;
	}

	public void setPosted(Date posted) {
		this.posted = posted;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public List<Hashtag> getTags() {
		return tags;
	}

	public void setTags(List<Hashtag> tags) {
		this.tags = tags;
	}

	public List<UserEnt> getUserLikes() {
		return userLikes;
	}

	public void setUserLikes(List<UserEnt> userLikes) {
		this.userLikes = userLikes;
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
		Tweet other = (Tweet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
