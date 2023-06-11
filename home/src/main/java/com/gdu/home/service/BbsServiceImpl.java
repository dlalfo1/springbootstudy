package com.gdu.home.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.gdu.home.domain.BbsDTO;
import com.gdu.home.mapper.BbsMapper;
import com.gdu.home.util.PageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BbsServiceImpl implements BbsService {
	
	private final BbsMapper bbsMapper;
	private final PageUtil pageUtil;
	
	@Override
	public void loadBbsList(HttpServletRequest request, Model model) {
		
		Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt.orElse("1"));
		
		int totalRecord = bbsMapper.getBbsCount();
		
		int recordPerPage = 20;
		
		pageUtil.setPageUtil(page, totalRecord, recordPerPage); // 여기까지 해줘야 쿼리문 #{begin} #{end} 변수에 값을 보낼 수 있다.
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);
		
		List<BbsDTO> bbsList = bbsMapper.getBbsList(map);
		
		model.addAttribute("bbsList", bbsList);
		model.addAttribute("beginNo", totalRecord - (page - 1) * recordPerPage);
		model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/bbs/list.do"));
		
	}
	
	@Transactional
	@Override
	public int addBbs(HttpServletRequest request) {
		
		// 파라미터 writer, title
		String writer = request.getParameter("writer");
		String title = request.getParameter("title");
		
		// ip 주소
		String ip = request.getRemoteAddr(); 
		
		// DB로 보낼 BbsDTO 객체
		BbsDTO bbsDTO = new BbsDTO();
		bbsDTO.setWriter(writer);
		bbsDTO.setTitle(title);
		bbsDTO.setIp(ip);
		
		// 원글 달기 -1
		int addResult = bbsMapper.addBbs(bbsDTO); // 인수 bbsDTO의 bbsNo 필드 값은 bbs.xml의 addBbs 쿼리문이 실행되면서 채워진다.
		// addBbs 실행을 통해서 채운 값
		/*
  		BBS_NO      : AUTO_INCREMENT
  		WRITER      : #{writer}
  		TITLE       : #{title}
  		IP          : #{ip}
  		CREATED_AT  : NOW()
  		STATE       : 1
  		DEPTH       : 0
  		GROUP_NO    : 비어있음
  		GROUP_ORDER : 0
		*/
		
		// 아직 GROUP_NO 칼럼의 값이 비어 있기 때문에, bbsDTO에 저장된 bbsNo값을 GROUP_NO 칼럼으로 저장해야 한다.
		// 원글 달기 -2
		addResult += bbsMapper.addBbsGroupNo(bbsDTO);
		
		// 결과 반환
		return addResult;
	}
	
	@Override
	public int removeBbs(int bbsNo) {
		
		int removeResult = bbsMapper.removeBbs(bbsNo);
		
		return removeResult;
	}
	// readOnly=true는 성능향상 도구라 생각하고 항상 붙여주기.
	@Transactional  // @Transactional : INSERT, UPDATE, DELETE 중 2개 이상의 쿼리를 실행하는 경우 반드시 추가한다.
								  // DBConfig 파일에 이미 트랜잭션 사용 가능한 Bean을 만들어뒀다.
	@Override
	public int addReply(HttpServletRequest request) {
		
		// 파라미터 writer, title
		String writer = request.getParameter("writer");
		String title = request.getParameter("title");
		
		// IP
		String ip = request.getRemoteAddr();
		
		// 원글의 정보(파라미터 depth, groupNo, groupOrder)
		int dept = Integer.parseInt(request.getParameter("depth"));
		int groupNo = Integer.parseInt(request.getParameter("groupNo"));
		int groupOrder = Integer.parseInt(request.getParameter("groupOrder"));
		
		// 원글 BbsDTO (기존 답글 선행 작업 : increaseGroupOrder를 위한 DTO)
		BbsDTO bbsDTO = new BbsDTO();
		bbsDTO.setGroupNo(groupNo);
		bbsDTO.setGroupOrder(groupOrder);
		
		// 기존 답글 선행 작업
		bbsMapper.increaseGroupOrder(bbsDTO);
		
		// 답글 replyDTO
		BbsDTO replyDTO = new BbsDTO();
		replyDTO.setWriter(writer);
		replyDTO.setTitle(title);
		replyDTO.setIp(ip);
		replyDTO.setDepth(dept +1);
		replyDTO.setGroupNo(groupNo);
		replyDTO.setGroupOrder(groupOrder +1);
		
		// 답글 달기
		int addReplyResult = bbsMapper.addReply(replyDTO);
		
		return addReplyResult;
	}

}
