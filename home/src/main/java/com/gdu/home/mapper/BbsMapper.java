package com.gdu.home.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.home.domain.BbsDTO;

@Mapper
public interface BbsMapper {
	public int getBbsCount();
	public List<BbsDTO> getBbsList(Map<String, Object> map);
	public int addBbs(BbsDTO bbsDTO);
	public int addBbsGroupNo(BbsDTO bbsDTO);
	public int removeBbs(int bbsNo);
	public int increaseGroupOrder(BbsDTO bbsDTO);
	public int addReply(BbsDTO replyDTO);
}
