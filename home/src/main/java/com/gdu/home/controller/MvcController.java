package com.gdu.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MvcController {
	
	@GetMapping("/")
	public String welcom() {
		return "index"; // src/main/resources/templates/index.html
		                // 컨트롤러 수정
	}

}
