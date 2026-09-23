package com.example.ats.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ats.dto.AtsEvent;
import com.example.ats.exception.EventValidationException;
import com.example.ats.exception.StorageOverflowException;
import com.example.ats.service.ActionService;

@RestController
@RequestMapping("/api/action")
public class ActionController {

	private final ActionService actionService;
	private final String internalToken;

	public ActionController(ActionService actionService, @Value("${INTERNAL_TOKEN}") String internalToken) {

		this.actionService = actionService;
		this.internalToken = internalToken;
	}

	@PostMapping
	public ResponseEntity<Void> saveAction(@RequestHeader(value = "X-Internal-Token", required = true) String token,
			@RequestBody AtsEvent event) throws EventValidationException, StorageOverflowException {

		if (internalToken.equals(token)) {
			actionService.save(event);
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	@GetMapping
	public ResponseEntity<List<AtsEvent>> getAllActions() {
		return ResponseEntity.ok(actionService.findAllSortedByTimeDesc());
	}
}
