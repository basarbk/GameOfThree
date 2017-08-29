package com.basarbk.gameofthree.service;

import org.springframework.stereotype.Service;

import com.basarbk.gameofthree.dao.ActionDao;
import com.basarbk.gameofthree.exception.InvalidActionException;
import com.basarbk.gameofthree.model.Action;
import com.basarbk.gameofthree.model.Game;
import com.basarbk.gameofthree.model.GameState;
import com.basarbk.gameofthree.model.vm.ActionWrapper;
import com.basarbk.gameofthree.model.vm.WebSocketMessage;
import com.basarbk.gameofthree.util.ActionUtil;

@Service
public class ActionService {
	
	ActionDao actionDao;
	
	GameService gameService;
	
	WebSocketService wsService;

	public ActionService(ActionDao actionDao, GameService gameService, WebSocketService webSocketService) {
		super();
		this.actionDao = actionDao;
		this.gameService = gameService;
		this.wsService = webSocketService;
	}

	public Action handleActionForGame(long id, Action currentAction) {
		Game game = gameService.getGameById(id);
		if(game.getState() != GameState.STARTED)
			throw new InvalidActionException();
		
		if(currentAction.getOwner()==null)
			throw new InvalidActionException();
		
		Action previousAction = game.getActions().get(game.getActions().size()-1);

		if(currentAction.getOwner().equals(previousAction.getOwner()))
			throw new InvalidActionException();
		
		int lastValue = previousAction.getResult();

		lastValue = ActionUtil.calculateLastValue(ActionUtil.getActionValue(currentAction.getType()), lastValue);
		currentAction.setResult(lastValue);
		currentAction.setGame(game);
		actionDao.save(currentAction);
		wsService.sendActionForGame(previousAction.getOwner(), new WebSocketMessage(WebSocketMessage.ACTION, new ActionWrapper(game.getId(), currentAction)));
		wsService.sendActionForGame(currentAction.getOwner(), new WebSocketMessage(WebSocketMessage.ACTION, new ActionWrapper(game.getId(), currentAction)));
		if(lastValue == 1) {
			gameService.setGameComplete(id);
		} else {
			if((previousAction.getOwner().getId() == game.getStarter().getId() && game.isStarterAutomatic()) ||
					(previousAction.getOwner().getId() == game.getOpponent().getId() && game.isOpponentAutomatic())){
				// play automatically for the next player if it set as automatic
				GameAutomaterService.addToQueue(ActionUtil.generateAutomaticAction(game, previousAction.getOwner(), lastValue));
			}

		}
		return currentAction;
	}
	
	

}
