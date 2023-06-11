package com.gdu.home.util;

import java.io.File;
import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

@Component
public class MyFileUtil {
	
	// 경로 구분자
	private String seq = Matcher.quoteReplacement(File.separator); 
	
	// String path 만들기
	public String getPath() {
		
		 LocalDate now = LocalDate.now();
		 // 루트/storage/2023/05/08
		return "/storage" + seq + now.getYear() + seq + String.format("%02d", now.getMonthValue()) + seq + String.format("%02d", now.getDayOfMonth());
	}
	
	// String summernoteimagePath 만들기
	public String getSummernoteImagePath() {
		return "/storage" + seq + "summernote";
		
		
	}

	// String filesystemName 만들기
	public String getFilesystemName(String originName) { // 원래 첨부파일명이 매개변수이다. => 확장자명을 알아야 하기 때문
		
		// 원래 첨부 파일의 확장자 꺼내기
		String extName = null;
		
		// 확장자에 마침표(.)가 포함된 예외 상황 처리
		if(originName.endsWith("tar.gz")) {
			extName = "tar.gz";
		} else {
			// String.split(정규식)
			// 정규식에서 마침표(.)는 모든 문자를 의미하므로 이스케이프 처리하거나 문자 클래스로 처리한다.
			// 이스케이프 처리 : \.
			// 문자클래스 처리 : [.]
			
			String[] array = originName.split("\\.");
			extName = array[array.length -1];
		}
		// 결과 반환
		// UUID.extName
		// 랜덤으로 UUID를 부여하고 String으로 변환하고 하이픈(-)을 빈문자열로 변환한다.
			
		return UUID.randomUUID().toString().replaceAll("-", "") + "." + extName; 
}

	// String tempPath 만들기(임시경로)
	public String getTempPath() {
		return "/storage" + seq + "temp";
	}

	// String tempfileName 만들기 (zip 파일)
	public String getTempfileName() {
		return UUID.randomUUID().toString().replaceAll("-", "") + ".zip"; 
	}
	
	// String yesterdayPath 만들기
	public String getYesterdayPath() {
		LocalDate date = LocalDate.now();
		date.minusDays(1);	// 1일 전
		return "/storage" + seq + date.getYear() + seq + String.format("%02d", date.getMonthValue()) + seq + String.format("%02d", date.getDayOfMonth());
	}

}