package com.gdu.home.util;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter 	// 값을 가져다 쓰기 위해서 선언
			// Setter는 값을 수정할 여지가 생기기 때문에 제외
@Component  // Bean으로 만들어 스프링컨테이너에 보관한다.
public class PageUtil {
									// 여기서 레코드는 행의 개수이다. 즉 총 사원수 107개가 되는거임.
	private int page;				// 현재 페이지 (파라미터로 받아온다.)
	private int totalRecord;		// 전체 레코드 개수 (DB에서 구해온다.)
	private int recordPerPage;		// 한 페이지에 표시할 레코드 개수 (파라미터로 받아온다.)
	private int begin;				// 한 페이지에 표시할 레코드의 시작 번호 (계산한다.)
	
									// begin, end 값을 계산하려면 page, totalRecord, recordPerPage 값이 필요하다.
	
	private int pagePerBlock = 5;   // 한 블록에 표시할 페이지의 개수 (임의로 정한다.)
	private int totalPage;		    // 전체 페이지 개수 (계산한다.)
	private int beginPage;		    // 한 블록에 표시할 페이지의 시작 번호 (계산한다.)
	private int endPage;			// 한 블록에 표시할 페이지의 종료 번호 (계산한다.)
	
	public void setPageUtil(int page, int totalRecord, int recordPerPage) {
		                                            
		// page, totalRecord, recordPerPage 저장
		this.page = page;
		this.totalRecord = totalRecord;
		this.recordPerPage = recordPerPage;
		
		// begin
		begin = (page -1) * recordPerPage;
		
		// totalPage 계산
		totalPage = totalRecord / recordPerPage; // 여기서 끝내면 나눠서 나머지가 있는애들은 오류가 생긴다.
												 // ex) 26 ~ 29페이지의 경우.. 이 경우엔 +1을 해주자.
		if(totalRecord % recordPerPage != 0) {	 // 나눈 나머지가 0이 아니다. (나머지가 생겼다.)
			totalPage++;
		}
		
		// beginPage, endPage 계산
		/*
			totalPage=6, pagePerBlock=4인 상황
			block(page)	beginPage	endPage
			1(1~4)		1			4
			2(5~6)		5			6
		*/
		
		beginPage = ((page - 1) / pagePerBlock) * pagePerBlock + 1;
		endPage = beginPage + pagePerBlock - 1;
		if(endPage > totalPage) {
			endPage = totalPage;
		}
		
	}
	
	public String getPagination(String path) { // 매개변수로 path를 선언해서 서비스의 경로를 모듈화해준다. 
		   									   // 이렇게 해주면 서비스에서 해당 경로만 넣어주면 된다.
											  
		 // path에 물음표(?), 즉 파라미터가 붙어있는지 확인해야한다.
		 // path에 ?가 포함되어 있으면 이미 파라미터가 포함된 경로이므로 &를 붙여서 page 파라미터를 추가한다.
		
		if(path.contains("?")) { // path에 물음표(?)가 포함되어있다면.
			path += "&";		 // path = "/app09/employees/pagination.do?order=ASC&"
		} else {
			path += "?";		 // path = "/app09/employees/pagination.do?"
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<div class=\"pagination\">");
		
		
		// 이전 블록 : 1블록은 이전 블록이 없고, 나머지 블록은 이전 블록이 있다.
		if(beginPage == 1) {
			sb.append("<span class=\"hidden\">◀</span>");
			
		} else {
			sb.append("<a class=\"link\" href=\"" + path + "page=" + (beginPage -1) + "\">◀</a>");
		}
		
		
		// 페이지번호 : 현재 페이지는 링크가 없다.         
		for(int p = beginPage; p <= endPage; p++) {
			if(p == page) {
				sb.append("<span class=\"strong\">" + p + "</span>");
			} else {
				sb.append("<a class=\"link\" href=\"" + path + "page=" + p + "\">" + p + "</a>");
			}
		}
		
		// 다음 블록 : 마지막 블록은 다음 블록이 없고, 나머지 블록은 다음 블록이 있다.
		if(endPage == totalPage) {
			sb.append("<span class=\"hidden\">▶</span>");
			
		} else {
			sb.append("<a class=\"link\" href=\"" + path + "page=" + (endPage + 1) + "\">▶</a>");
		}
		
		sb.append("</div>");
		
		return sb.toString();	// StringBuilder 값은 String값으로 변환을 해줘야 한다.
		
	}
}
