package com.basarbk.gameofthree;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.basarbk.gameofthree.dao.PlayerDao;
import com.basarbk.gameofthree.exception.PlayerNotFoundException;
import com.basarbk.gameofthree.model.Player;
import com.basarbk.gameofthree.model.PlayerType;
import com.basarbk.gameofthree.model.Presence;
import com.basarbk.gameofthree.service.PlayerService;
import com.basarbk.gameofthree.service.WebSocketService;

@RunWith(SpringRunner.class)
public class PlayerServiceUnitTest {
	
	@MockBean
	PlayerDao playerDao;
	
	@MockBean
	WebSocketService wsService;
	
	PlayerService playerService;
	
	Player starter;
	Player opponent;
	
	@Before
	public void setup(){
		starter = new Player();
		starter.setName("first");
		starter.setId(1);
		
		opponent = new Player();
		opponent.setName("second");
		opponent.setId(2);
		
		playerService = new PlayerService(playerDao, wsService);
		Mockito.when(playerDao.findOne(1L)).thenReturn(starter);
		Mockito.when(playerDao.findOne(2L)).thenReturn(opponent);
		Mockito.when(playerDao.findByName("first")).thenReturn(starter);
		
	}
	
	@Test
	public void generateRandomPlayer(){
		Player player = playerService.generateRandomPlayer();
		Assert.assertNotNull(player);
		Assert.assertEquals(player.getPresence(), Presence.ONLINE);
		Assert.assertEquals(player.getType(), PlayerType.TEMPORARY);
	}
	
	@Test
	public void getPlayerByNameSuccess(){
		Player player = playerService.getPlayer("first");
		Assert.assertNotNull(player);
		Assert.assertEquals(1L, player.getId());
		Assert.assertEquals("first", player.getName());
	}
	
	@Test(expected = PlayerNotFoundException.class)
	public void getUnknownPlayerByNameException(){
		playerService.getPlayer("unknown");
	}

	@Test
	public void getPlayerByIdSuccess(){
		Player player = playerService.getPlayer(1L);
		Assert.assertNotNull(player);
		Assert.assertEquals(1L, player.getId());
		Assert.assertEquals("first", player.getName());
	}
	
	@Test(expected = PlayerNotFoundException.class)
	public void getUnknownPlayerByIdException(){
		playerService.getPlayer(1000L);
	}
	
	@Test
	public void getOpponents(){
		List<Player> players = new ArrayList<>();
		players.add(starter);
		players.add(opponent);
		Mockito.when(playerDao.findByIdNot(3L)).thenReturn(players);
		
		List<Player> p = playerService.getOpponents(3);
		Assert.assertNotNull(p);
		Assert.assertEquals(2, p.size());
	}
	
	@Test(expected = PlayerNotFoundException.class)
	public void getOpponentsNull(){
		Mockito.when(playerDao.findByIdNot(3L)).thenReturn(new ArrayList<>());
		playerService.getOpponents(3);
	}
	
	@Test
	public void getKnownPlayer(){
		Player player = playerService.getOrCreate("first");
		Assert.assertNotNull(player);
		Assert.assertEquals(1L, player.getId());
		Assert.assertEquals("first", player.getName());
	}
	
	@Test
	public void getNewPlayer(){
		Player player = playerService.getOrCreate("basar");
		Assert.assertNotNull(player);
		Assert.assertEquals("basar", player.getName());
		Assert.assertEquals(PlayerType.REGISTERED, player.getType());
	}
	
	@Test
	public void changePresence(){
		playerService.updatePresence("first", Presence.AWAY);
		Assert.assertEquals(Presence.AWAY, starter.getPresence());
	}
}
