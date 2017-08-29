package com.basarbk.gameofthree.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.basarbk.gameofthree.model.Player;
import com.basarbk.gameofthree.model.vm.WebSocketMessage;

@Service
public class WebSocketService {

	@Autowired
	SimpMessagingTemplate messaging;
	
	public void sendPresence(Player player){
		messaging.convertAndSend("/topic/presence", player);
	}
	
	public void sendNewPlayer(Player player){
		messaging.convertAndSend("/topic/new-player", player);
	}
	
	public void sendGameStateRequests(Player target, WebSocketMessage message) {
		messaging.convertAndSend("/topic/player/"+target.getName(), message);
	}
	
	public void sendActionForGame(Player target, WebSocketMessage message) {
		messaging.convertAndSend("/topic/player/"+target.getName(), message);
	}
}
