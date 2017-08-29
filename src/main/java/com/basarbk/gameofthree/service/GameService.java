package com.basarbk.gameofthree.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.basarbk.gameofthree.dao.GameDao;
import com.basarbk.gameofthree.exception.GamesNotFoundException;
import com.basarbk.gameofthree.exception.InvalidActionException;
import com.basarbk.gameofthree.model.Action;
import com.basarbk.gameofthree.model.Game;
import com.basarbk.gameofthree.model.GameState;
import com.basarbk.gameofthree.model.Player;
import com.basarbk.gameofthree.model.vm.WebSocketMessage;
import com.basarbk.gameofthree.util.ActionUtil;

@Service
public class GameService {
	
	GameDao gameDao;
	
	PlayerService playerService;
	
	WebSocketService wsService;
	
	public GameService(GameDao gameDao, PlayerService playerService, WebSocketService webSoketService) {
		super();
		this.gameDao = gameDao;
		this.playerService = playerService;
		this.wsService = webSoketService;
	}

	public Game createGame(Game game) {
		Action action = ActionUtil.generateGameStartAction(game);
		game.getActions().add(action);
		game.setState(GameState.STARTED);
		gameDao.save(game);
		wsService.sendGameStateRequests(game.getOpponent(), new WebSocketMessage(WebSocketMessage.GAME_START, game));
		if(game.isOpponentAutomatic()){
			GameAutomaterService.addToQueue(ActionUtil.generateAutomaticAction(game, game.getOpponent(), action.getResult()));			
		}
		return game;
	}

	public List<Game> getGamesForPlayer(long id) {
		Player player = playerService.getPlayer(id);
		List<Game> games = gameDao.findByStarterOrOpponent(player, player);
		if(games.size() == 0)
			throw new GamesNotFoundException();
		return games;
	}

	public Game getGameById(long id) {
		Game game = gameDao.findOne(id);
		if(game == null)
			throw new GamesNotFoundException();
		return game;
	}


	public void setGameComplete(long id) {
		Game game = getGameById(id);
		game.setState(GameState.COMPLETED);
		gameDao.save(game);
		
	}

	public Game toggleAutomatic(long playerId, long gameId) {
		Game game = getGameById(gameId);
		Player currentPlayer = null;
		if(game.getStarter().getId() == playerId) {
			currentPlayer = game.getStarter();
			game.setStarterAutomatic(!game.isStarterAutomatic());
		} else if (game.getOpponent().getId() == playerId){
			currentPlayer = game.getOpponent();
			game.setOpponentAutomatic(!game.isOpponentAutomatic());
		} else {
			throw new InvalidActionException();
		}
		
		if(game.getActions().size()>0){			
			Action latestAction = game.getActions().get(game.getActions().size()-1);
			if(latestAction.getOwner().getId() != currentPlayer.getId())
				GameAutomaterService.addToQueue(ActionUtil.generateAutomaticAction(game, currentPlayer, latestAction.getResult()));
		}
		
		gameDao.save(game);
		return game;
	}
}
