package com.basarbk.gameofthree.util;

import java.util.Random;

public class StringUtil {
	
	public static String createUser(){
		Random rand = new Random();
		int value = rand.nextInt(900)+100;
		return "user_"+value;
	}

}
