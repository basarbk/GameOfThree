package com.basarbk.gameofthree.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.basarbk.gameofthree.dao.PlayerDao;
import com.basarbk.gameofthree.exception.PlayerNotFoundException;
import com.basarbk.gameofthree.model.Player;
import com.basarbk.gameofthree.model.PlayerType;
import com.basarbk.gameofthree.model.Presence;
import com.basarbk.gameofthree.util.StringUtil;

@Service
public class PlayerService {
	
	PlayerDao playerDao;
	
	WebSocketService wsService;
	
	public PlayerService(PlayerDao dao, WebSocketService webSocketService) {
		super();
		this.playerDao = dao;
		this.wsService = webSocketService;
	}

	public Player generateRandomPlayer(){
		String randomName = StringUtil.createUser();
		Player currentPlayer = new Player(randomName, PlayerType.TEMPORARY);
		currentPlayer.setPresence(Presence.ONLINE);
		playerDao.save(currentPlayer);
		broadcastNewPlayer(currentPlayer);
		return currentPlayer;
	}
	
	public Player getPlayer(String name){
		Player player = playerDao.findByName(name);
		if(player == null)
			throw new PlayerNotFoundException();
		return player;
	}
	
	public Player getPlayer(long id){
		Player player = playerDao.findOne(id);
		if(player == null)
			throw new PlayerNotFoundException();
		return player;
	}

	public List<Player> getOpponents(long id) {
		List<Player> opponents = playerDao.findByIdNot(id);
		if(opponents.size() == 0)
			throw new PlayerNotFoundException();
		return opponents;
	}

	public Player getOrCreate(String newname) {
		Player player = playerDao.findByName(newname);
		if(player != null) {
			updatePresence(newname, Presence.ONLINE);
			return player;
		}
		
		player = new Player();
		player.setName(newname);
		player.setType(PlayerType.REGISTERED);
		player.setPresence(Presence.ONLINE);

		playerDao.save(player);
		
		broadcastNewPlayer(player);
		
		return player;
	}
	
	private void broadcastNewPlayer(Player player){
		wsService.sendNewPlayer(player);		
	}

	/**
	 * The presence is not a mature future yet.
	 * Not checking multiple login condition. Assuming user will open single browser for now
	 * 
	 * @param name
	 * @param presence
	 */
	public void updatePresence(String name, Presence presence) {
		Player p = playerDao.findByName(name);
		if(p==null)
			return;
		
		p.setPresence(presence);
		playerDao.save(p);
		wsService.sendPresence(p);
	}

}
