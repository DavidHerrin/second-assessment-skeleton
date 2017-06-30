package com.cooksys.mapper;

import org.mapstruct.Mapper;

import com.cooksys.dto.UserDelDto;
import com.cooksys.dto.UserDto;
import com.cooksys.dto.UserDtoNoCred;
import com.cooksys.entity.UserEnt;

@Mapper(componentModel = "spring", uses = { ReferenceMapper.class })
public interface UserMapper {
	
	UserDto toDto(UserEnt entity);

	UserEnt toEntity(UserDto dto);
	
	UserDelDto toDelDto(UserEnt entity);

	UserEnt toEntity(UserDelDto dto);
	
	UserDtoNoCred toDtoNoCred(UserEnt entity);

	UserEnt toEntity(UserDtoNoCred dto);

}