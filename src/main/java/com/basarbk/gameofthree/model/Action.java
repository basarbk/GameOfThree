package com.basarbk.gameofthree.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Action {
	
	@Id @GeneratedValue
	private long id;
	
	private ActionType type;
	
	@ManyToOne
	private Player owner;
	
	@ManyToOne
	@JsonIgnore
	private Game game;
	
	private int result;

	public Action() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}



}
