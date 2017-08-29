package com.basarbk.gameofthree;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.basarbk.gameofthree.dao.GameDao;
import com.basarbk.gameofthree.exception.GamesNotFoundException;
import com.basarbk.gameofthree.exception.InvalidActionException;
import com.basarbk.gameofthree.model.ActionType;
import com.basarbk.gameofthree.model.Game;
import com.basarbk.gameofthree.model.Player;
import com.basarbk.gameofthree.service.GameAutomaterService;
import com.basarbk.gameofthree.service.GameService;
import com.basarbk.gameofthree.service.PlayerService;
import com.basarbk.gameofthree.service.WebSocketService;

@RunWith(SpringRunner.class)
public class GameServiceUnitTest {
	
    private GameService gameService;

	@MockBean
	PlayerService playerService;
	
	@MockBean
	private GameDao gameDao;
	
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
		
		gameService = new GameService(gameDao, playerService, wsService);
		Mockito.when(gameDao.findOne(1L)).thenReturn(game);
	}
	
	@Test
	public void createGame(){
		Game result = gameService.createGame(game);
		Assert.assertEquals(1, result.getActions().size());
		Assert.assertEquals(ActionType.START_GAME, result.getActions().get(0).getType());
		Assert.assertEquals(starter.getName(), result.getActions().get(0).getOwner().getName());
	}
	
	@Test
	public void createGameWithOpponentAuto(){
		game.setOpponentAutomatic(true);
		Game result = gameService.createGame(game);
		Assert.assertEquals(1, result.getActions().size());
		Assert.assertEquals(ActionType.START_GAME, result.getActions().get(0).getType());
		Assert.assertEquals(starter.getName(), result.getActions().get(0).getOwner().getName());
		Assert.assertEquals(1, GameAutomaterService.automaticActions.size());
	}
	
	@Test
	public void createGameWithStarterAuto(){
		game.setStarterAutomatic(true);
		Game result = gameService.createGame(game);
		Assert.assertEquals(1, result.getActions().size());
		Assert.assertEquals(ActionType.START_GAME, result.getActions().get(0).getType());
		Assert.assertEquals(starter.getName(), result.getActions().get(0).getOwner().getName());
		Assert.assertEquals(0, GameAutomaterService.automaticActions.size());
	}
	
	@Test
	public void getGameByIdSuccess(){
		Game result = gameService.getGameById(1L);
		Assert.assertNotNull(result);
	}
	
	@Test(expected = GamesNotFoundException.class)
	public void getGameByIdFail(){
		gameService.getGameById(2L);
	}
	
	@Test
	public void toggleAutomaticStateForPlayer1(){
		Game game = gameService.toggleAutomatic(1L, 1L);
		Assert.assertEquals(true, game.isStarterAutomatic());
		Assert.assertEquals(false, game.isOpponentAutomatic());
	}
	
	@Test
	public void toggleAutomaticStateForPlayer2(){
		Game game = gameService.toggleAutomatic(2L, 1L);
		Assert.assertEquals(false, game.isStarterAutomatic());
		Assert.assertEquals(true, game.isOpponentAutomatic());
	}

	@Test(expected = GamesNotFoundException.class)
	public void toggleAutomaticStateForUnknownGame(){
		gameService.toggleAutomatic(1L, 100L);
	}
	
	@Test(expected = InvalidActionException.class)
	public void toggleAutomaticStateForUnknownPlayer(){
		gameService.toggleAutomatic(100L, 1L);
	}

}
