package com.mcsense.test;

import java.sql.Timestamp;
import java.util.Calendar;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test1();
	}

	public static void test1(){
		String resp = "aaa";
		if(!resp.equals("") && resp.matches("[\\d]+")){
			System.out.println("true");
		} else {
			System.out.println("false");
		}
	}
	
	public static void test2(){
		int duration = 113;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		now.setHours(23);
		now.setMinutes(0);
		now.setNanos(0);
		now.setSeconds(0);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, -duration);
		
		Calendar calNow = Calendar.getInstance();

		System.out.println("Is task available: "+calNow.before(cal));
	}
}
