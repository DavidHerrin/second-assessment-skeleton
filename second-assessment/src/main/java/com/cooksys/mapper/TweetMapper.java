package com.cooksys.mapper;

import org.mapstruct.Mapper;

import com.cooksys.dto.TweetDto;
import com.cooksys.dto.TweetDelDto;

import com.cooksys.entity.Tweet;


@Mapper(componentModel = "spring", uses = { ReferenceMapper.class })
public interface TweetMapper {
	
	TweetDto toDto(Tweet entity);

	Tweet toEntity(TweetDto dto);
	
	TweetDelDto toDelDto(Tweet entity);

	Tweet toEntity(TweetDelDto dto);

}
