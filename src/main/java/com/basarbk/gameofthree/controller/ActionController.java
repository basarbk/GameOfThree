package com.basarbk.gameofthree.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basarbk.gameofthree.model.Action;
import com.basarbk.gameofthree.service.ActionService;

@RestController
@RequestMapping("/api")
public class ActionController {
	
	ActionService actionService;
	
	public ActionController(ActionService actionService) {
		super();
		this.actionService = actionService;
	}

	@PostMapping("/games/{id:[0-9]+}/actions")
	public ResponseEntity<?> postAction(@PathVariable long id, @RequestBody Action action){
		return ResponseEntity.ok(actionService.handleActionForGame(id, action)); 
	}


}
