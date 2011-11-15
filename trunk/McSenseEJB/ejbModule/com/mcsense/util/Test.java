package com.mcsense.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;

public class Test {

	public Test() {
		
	}
 
	public static void main(String Args[]){
		String time = "2011-11-13 23:09:36.0";

		SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date lFromDate1 = null;
		try {
			lFromDate1 = datetimeFormatter1.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("formated date :" + lFromDate1);
		
		Timestamp fromTS1 = null;
		if(lFromDate1!=null)
			fromTS1 = new Timestamp(lFromDate1.getTime());
		
		String timeformat = fromTS1.toString();
		System.out.println("Timestamp date :" + timeformat.substring(0,timeformat.length() - 5));
	}
}
