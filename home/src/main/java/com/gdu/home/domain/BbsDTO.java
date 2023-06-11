package com.gdu.home.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BbsDTO {
	
	private int bbsNo;
	private String writer;
	private String title;
	private String ip;
	private Date createdAt;
	private int state;
	private int depth;
	private int groupNo;
	private int groupOrder;
	

}
