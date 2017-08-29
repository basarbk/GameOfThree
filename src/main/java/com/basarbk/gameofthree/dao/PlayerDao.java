package com.basarbk.gameofthree.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basarbk.gameofthree.model.Player;

public interface PlayerDao extends JpaRepository<Player, Long>{

	Player findByName(String name);
	
	List<Player> findByIdNot(long id);

}
