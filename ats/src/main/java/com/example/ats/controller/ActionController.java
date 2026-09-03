package com.example.ats.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ats.dto.AtsEvent;
import com.example.ats.service.ActionService;



@RestController
@RequestMapping("/api/action")
public class ActionController {

	private final ActionService actionService;

	public ActionController(ActionService actionService) {
		this.actionService = actionService;
	}

	@PostMapping
	public ResponseEntity<Void> saveAction(@RequestBody AtsEvent event) {
		actionService.save(event);
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<List<AtsEvent>> getAllActions() {
		return ResponseEntity.ok(actionService.findAllSortedByTimeDesc());
	}
}