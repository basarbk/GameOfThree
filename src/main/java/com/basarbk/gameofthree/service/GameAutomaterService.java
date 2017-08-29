package com.basarbk.gameofthree.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.basarbk.gameofthree.model.Action;

@Service
@EnableScheduling
public class GameAutomaterService {
	
	public static BlockingQueue<Action> automaticActions = new LinkedBlockingQueue<>();
	
	@Autowired
	ActionService actionService;
	
	public static void addToQueue(Action action){
		automaticActions.add(action);
	}
	
	@Scheduled(fixedDelay = 1000)
	public void run() {
		Action act = automaticActions.peek();
		if(act!=null){
			try {
				Action action = automaticActions.take();
				actionService.handleActionForGame(action.getGame().getId(), action);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

}
