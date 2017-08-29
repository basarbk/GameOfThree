package com.basarbk.gameofthree.model.vm;

public class WebSocketMessage {
	
	public static final String GAME_START = "game-start";
	public static final String GAME_COMPLETED = "game-completed";
	public static final String ACTION = "action";

	String type;
	
	Object body;
	
	public WebSocketMessage(String type, Object body) {
		super();
		this.type = type;
		this.body = body;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

}
