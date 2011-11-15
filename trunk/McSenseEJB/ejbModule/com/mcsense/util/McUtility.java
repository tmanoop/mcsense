package com.mcsense.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class McUtility {
	
	public static <E> E lookupEJB(String binding){
		try { 
			return (E) new InitialContext().lookup(binding);
		} catch(NamingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Timestamp getTimestamp(){
		return new Timestamp(getCurrentTime());
	}
	
	public static long getCurrentTime(){
		return System.currentTimeMillis();
	}

}
