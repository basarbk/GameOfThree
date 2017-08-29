package com.basarbk.gameofthree.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basarbk.gameofthree.model.Game;
import com.basarbk.gameofthree.model.Player;

public interface GameDao extends JpaRepository<Game, Long> {

	List<Game> findByStarterOrOpponent(Player player, Player player2);

}
