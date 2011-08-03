package com.mcsense.util;

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

}
