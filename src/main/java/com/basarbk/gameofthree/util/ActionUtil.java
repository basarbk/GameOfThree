package com.basarbk.gameofthree.util;

import java.util.Random;

import com.basarbk.gameofthree.exception.InvalidActionException;
import com.basarbk.gameofthree.model.Action;
import com.basarbk.gameofthree.model.ActionType;
import com.basarbk.gameofthree.model.Game;
import com.basarbk.gameofthree.model.Player;

public class ActionUtil {
	
	public static Action generateGameStartAction(Game game){
		Action action = new Action();
		action.setType(ActionType.START_GAME);
		action.setResult(initValue());
		action.setOwner(game.getStarter());
		action.setGame(game);
		return action;
	}

	public static Action generateAutomaticAction(Game game, Player player, int lastValue){
		Action automaticAct = new Action();
		automaticAct.setGame(game);
		automaticAct.setOwner(player);
		automaticAct.setType(nextActionType(lastValue));
		return automaticAct;
	}
	
	public static int calculateLastValue(int actionValue, int lastValue){
		int sum = lastValue + actionValue;
		if(sum % 3f  == 0f)
			return sum/3;
		else 
			throw new InvalidActionException();
	}

	public static ActionType nextActionType(int value){
		int rem = value % 3;
		if(rem == 0)
			return ActionType.ZERO;
		
		if(rem==1)
			return ActionType.MINUS_ONE;
			
		return ActionType.ADD_ONE;
	}
	
	public static int getActionValue(ActionType type){
		switch(type){
		case ADD_ONE :
			return 1;
		case MINUS_ONE :
			return -1;
		default:
			return 0;
		}
	}
	
	public static int initValue(){
		return new Random().nextInt(97)+4; // this would generate a value between 4 to 100 .. below 4 would not be ok
	}
}
