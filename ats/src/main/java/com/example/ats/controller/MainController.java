package com.example.ats.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	private final String telephonyUrl;

	public MainController(@Value("${telephony.url}") String telephonyUrl) {
		this.telephonyUrl = telephonyUrl;
	}

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("telephonyUrl", telephonyUrl);
		return "home";
	}
}