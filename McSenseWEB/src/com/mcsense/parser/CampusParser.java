package com.mcsense.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcsense.util.WebConstants;

public class CampusParser {

	private static final String DIRECTORY_PATH= "C:\\Manoop\\McSense\\McSenseWEB\\WebContent\\files\\";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<LocationInfo> locationList = new ArrayList<LocationInfo>();
		String fileName = "762.txt";
		locationList = parseCampusLog(WebConstants.DESTINATION_DIR_PATH, fileName);
//		for(LocationInfo l : locationList)
//			System.out.println("lat: "+l.getLatitude()+" long: "+l.getLongitude()+" speed: "+l.getSpeed());	
	}

	public static ArrayList<LocationInfo> parseCampusLog(String path, String fileName){
		//read 597.txt
//		String fileName = "597.txt";
		ArrayList<LocationInfo> locationList = new ArrayList<LocationInfo>();
		
		try {
			Scanner sc = new Scanner(new File(path+"\\"+fileName));
			while(sc.hasNext()){
				String st = sc.next();
				if(st.contains("Latitude")){
					LocationInfo loc = new LocationInfo();
					loc.setLatitude(st.substring(st.lastIndexOf("Latitude")+9,st.lastIndexOf("Longitude")-1));
					loc.setLongitude(st.substring(st.lastIndexOf("Longitude")+10,st.lastIndexOf("Speed")-1));
					loc.setSpeed(st.substring(st.lastIndexOf("Speed")+6,st.length()));
					locationList.add(loc);
				}
			}
			
		} catch (FileNotFoundException e) {
			Logger.getLogger(CampusParser.class.getName()).log(Level.SEVERE, null, e);
		}
		return locationList;
	}
	
}
