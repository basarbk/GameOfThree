package com.basarbk.gameofthree.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basarbk.gameofthree.model.Game;
import com.basarbk.gameofthree.service.GameService;

@RestController
@RequestMapping("/api")
public class GameController {
	
	GameService gameService;
	
	public GameController(GameService gameService) {
		this.gameService = gameService;
	}
	
	@PostMapping("/games")
	public ResponseEntity<?> startGame(@RequestBody Game game){
		return ResponseEntity.ok(gameService.createGame(game));
	}

	@GetMapping("/players/{id:[0-9]+}/games")
	public ResponseEntity<?> getPlayersNames(@PathVariable long id){
		return ResponseEntity.ok(gameService.getGamesForPlayer(id));
	}
	
	@PutMapping("/players/{id:[0-9]+}/games/{gameid:[0-9]+}/automatic")
	public ResponseEntity<?> toggleAutomatic(@PathVariable long id, @PathVariable long gameid){
		return ResponseEntity.ok(gameService.toggleAutomatic(id, gameid));
	}
}
