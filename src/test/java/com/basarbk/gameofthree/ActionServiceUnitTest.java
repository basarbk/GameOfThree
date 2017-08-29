package com.basarbk.gameofthree;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.basarbk.gameofthree.dao.ActionDao;
import com.basarbk.gameofthree.exception.InvalidActionException;
import com.basarbk.gameofthree.model.Action;
import com.basarbk.gameofthree.model.ActionType;
import com.basarbk.gameofthree.model.Game;
import com.basarbk.gameofthree.model.GameState;
import com.basarbk.gameofthree.model.Player;
import com.basarbk.gameofthree.service.ActionService;
import com.basarbk.gameofthree.service.GameService;
import com.basarbk.gameofthree.service.WebSocketService;

@RunWith(SpringRunner.class)
public class ActionServiceUnitTest {
	
	ActionService actionService;
    
	@MockBean
    private GameService gameService;
	
	@MockBean
	private ActionDao actionDao;
	
	@MockBean
	private WebSocketService wsService;
	
	Player starter;
	Player opponent;
	
	Game game;
	
	@Before
	public void setup(){
		starter = new Player();
		starter.setName("first");
		starter.setId(1);
		
		opponent = new Player();
		opponent.setName("second");
		opponent.setId(2);
		
		game = new Game();
		game.setId(1);
		game.setStarter(starter);
		game.setOpponent(opponent);
		game.setState(GameState.COMPLETED);
		
		Action action = new Action();
		action.setType(ActionType.START_GAME);
		action.setResult(55);
		action.setOwner(game.getStarter());
		action.setGame(game);
		game.getActions().add(action);
		
		actionService = new ActionService(actionDao, gameService,wsService);
		
		Mockito.when(gameService.getGameById(1)).thenReturn(game);
		Mockito.when(actionDao.save(action)).thenReturn(action);
	}
	
	
	@Test(expected = InvalidActionException.class)
	public void completedGameException(){
		Action a = new Action();
		a.setOwner(opponent);
		a.setType(ActionType.ADD_ONE);
		
		actionService.handleActionForGame(1, a);
	}
	
	@Test(expected = InvalidActionException.class)
	public void startedGameInvalidActionException(){
		game.setState(GameState.STARTED);
		Action a = new Action();
		a.setOwner(opponent);
		a.setType(ActionType.ADD_ONE);
		
		actionService.handleActionForGame(1, a);
	}
	
	@Test
	public void startedGameValidAction(){
		game.setState(GameState.STARTED);
		Action a = new Action();
		a.setId(111);
		a.setOwner(opponent);
		a.setType(ActionType.MINUS_ONE);
		Assert.assertEquals(actionService.handleActionForGame(1, a).getId(), a.getId());
	}
	
	@Test(expected = InvalidActionException.class)
	public void invalidPartnerSendsAction(){
		game.setState(GameState.STARTED);
		Action a = new Action();
		a.setId(111);
		a.setOwner(starter);
		a.setType(ActionType.MINUS_ONE);
		actionService.handleActionForGame(1, a);
	}


}
