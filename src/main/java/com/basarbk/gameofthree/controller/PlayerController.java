package com.basarbk.gameofthree.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basarbk.gameofthree.model.Player;
import com.basarbk.gameofthree.model.Presence;
import com.basarbk.gameofthree.service.PlayerService;

@RestController
@RequestMapping("/api")
public class PlayerController {
	
	PlayerService playerService;
	
	public PlayerController(PlayerService playerService) {
		super();
		this.playerService = playerService;
	}

	@GetMapping("/players/my-player")
	public ResponseEntity<?> getSession(HttpSession session){
		Object currentPlayer = session.getAttribute("whoami");
		
		Player p = null;
		if(currentPlayer == null || ((String) currentPlayer).isEmpty()){
			p = playerService.generateRandomPlayer();
			session.setAttribute("whoami", p.getName());
		} else {
			p = playerService.getPlayer((String) currentPlayer);
			if(p.getPresence() == Presence.OFFLINE){
				playerService.updatePresence(p.getName(), Presence.ONLINE);
			}
		}
		return ResponseEntity.ok(p);
	}
	
	@PutMapping("/players/my-player/{newname}")
	public ResponseEntity<?> getSession(HttpSession session, @PathVariable String newname){
		
		Object currentPlayer = session.getAttribute("whoami");
		if(currentPlayer!=null) 
			playerService.updatePresence((String)currentPlayer, Presence.OFFLINE);
		
		Player p = playerService.getOrCreate(newname);
		session.setAttribute("whoami", newname);
		return ResponseEntity.ok(p);
	}

	
	@GetMapping("/players/{id:[0-9]+}/opponents")
	public ResponseEntity<?> getOpponents(@PathVariable long id){
		return ResponseEntity.ok(playerService.getOpponents(id));
	}
	
}
