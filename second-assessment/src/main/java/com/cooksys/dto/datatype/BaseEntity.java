package com.cooksys.dto.datatype;

import java.io.Serializable;

public interface BaseEntity<Id extends Serializable> {

	public Id getId();

	public void setId(Id id);

}