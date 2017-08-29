package com.basarbk.gameofthree.model.vm;

import com.basarbk.gameofthree.model.Action;

public class ActionWrapper {
	
	long gameid;
	
	Action action;

	public ActionWrapper(long gameid, Action action) {
		super();
		this.gameid = gameid;
		this.action = action;
	}

	public long getGameid() {
		return gameid;
	}

	public void setGameid(long gameid) {
		this.gameid = gameid;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
}
