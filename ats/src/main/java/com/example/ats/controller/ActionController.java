package com.example.ats.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/action")
public class ActionController {
	
	@PostMapping
	public String getAction() {
		return "";
	}
}
