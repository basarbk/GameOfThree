package com.basarbk.gameofthree.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Game {
	
	@Id @GeneratedValue
	private long id;
	
	@ManyToOne
	private Player starter;
	
	@ManyToOne
	private Player opponent;
	
	@OneToMany(mappedBy="game", cascade=CascadeType.ALL)
	private List<Action> actions = new ArrayList<>();
	
	private GameState state;
	
	private boolean starterAutomatic = false;
	
	private boolean opponentAutomatic = false;

	public Game() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Player getStarter() {
		return starter;
	}

	public void setStarter(Player starter) {
		this.starter = starter;
	}

	public Player getOpponent() {
		return opponent;
	}

	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public boolean isStarterAutomatic() {
		return starterAutomatic;
	}

	public void setStarterAutomatic(boolean starterAutomatic) {
		this.starterAutomatic = starterAutomatic;
	}

	public boolean isOpponentAutomatic() {
		return opponentAutomatic;
	}

	public void setOpponentAutomatic(boolean opponentAutomatic) {
		this.opponentAutomatic = opponentAutomatic;
	}}
