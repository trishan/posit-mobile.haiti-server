/*
 * File: SmsReader.java
 * 
 * Copyright (C) 2011 Humanitarian FOSS Project
 * 
 * This file is part of POSIT-Haiti Server.
 *
 * POSIT-Haiti Server is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation; either version 3.0 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not visit http://www.gnu.org/licenses/gpl.html.
 *
 */

package haiti.server.gui;

import haiti.server.datamodel.Beneficiary;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;


/**  
 * A command-line program to read a SMS message file, one message per line.
 *
 *  <BR>To compile: javac -classpath ../../haiti-server.jar:. SmsReader.java
 *  <BR>To run:     java -classpath ../../haiti-server.jar:. SmsReader <filename> [encoding]
 *
 */

public class SmsReader {
	
	
	public static final String TAG = "SmsReader";
	
	private String filename;
	private String encoding;
	private ArrayList<String> messages = new ArrayList<String>();;

	
	
	
	
	public SmsReader(String filename) { 
		this(filename, System.getProperty("file.encoding"));
	}
	
	public SmsReader(String filename, String encoding) {
		log("Encoding = " + encoding);
		this.filename = filename;
		this.encoding = encoding;
	}
	
	public SmsReader(){
		
	}
	/**
	 * Details for implementing the DataBase can be found on the below link
	 * http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC#Usage
	 * @param dbName
	 * @throws ClassNotFoundException
	 */
	public void readMsgsFromDb(String dbName) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try{
		   connection = DriverManager.getConnection(dbName);
		   Statement statement = connection.createStatement();
		   statement.setQueryTimeout(60);  // set timeout to 30 sec.
		   statement.executeUpdate("drop table if exists beneficiary");
		   statement.executeUpdate("create table beneficiary (sms string)");
		   statement.executeUpdate("insert into  beneficiary values('f=tom&l=jones&a=11 Main Street&c=Hartford&z=06106')");
		   statement.executeUpdate("insert into beneficiary values('f=sarah&l=smith&a=12 Main Street&c=Hartford&z=06106')");
		   ResultSet rs = statement.executeQuery("select * from beneficiary");
		   while(rs.next())
		   {
		     readSMS(rs.getString("sms"));
		      }
			}
		catch(SQLException e)
	    {
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    }
	    finally
	    {
	      try
	      {
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e)
	      {
	        // connection close failed.
	        System.err.println(e);
	      }
	    }
	}
	
	  /** 
	   * Reads the file line by line into the arraylist. 
	   */
	public void readFile() throws IOException {
	    log("Reading from file.");
	    Scanner scanner = new Scanner(new FileInputStream(filename), encoding);
	    //Scanner scanner = new Scanner(new FileInputStream(filename));

	    try {
	      while (scanner.hasNextLine()){
	    	  messages.add(scanner.nextLine());
	      }
	    }
	    finally{
	      scanner.close();
	    }
	    log("Text read from " + filename + " : " + System.getProperty("line.separator") + toString());
	  }
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public ArrayList<String> getMessages() {
		return messages;
	}
	
	public String[] getMessagesAsArray() {
		String arr[] = new String[messages.size()];
		for (int k = 0; k < arr.length; k++) 
			arr[k] = messages.get(k);
		return arr;
	}

	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < messages.size(); k++) 
			sb.append(messages.get(k) + System.getProperty("line.separator"));
		return sb.toString();
	}
	
	
	
	private void log(String s) {
		System.out.println(TAG + " " + s);
	}
	
	public void readSMS(String s){
		messages.add(s);
	}
	
	public String[] getCommune(){
		String[] commune = {"Anse a Pitres","Bainet","Belle Anse","Cote de fer","Grand Gosier","La vallee","Thiotte"};
		return commune;
		
	} 
	
	public String[] getCommuneSection(String commune){
		String[] communeSection;
		if (commune.equals("Anse a Pitres")) {
			communeSection = new String[5];
			communeSection[0] = "Anse a Pitres";
			communeSection[1] = "Bois d_Orme";
			communeSection[2] = "BoucanGuillaume";
			communeSection[3] = "Centre de Sante Anse a Pitres";
			communeSection[4] = "Platon Cedre";
		}
		else if (commune.equals("Bainet")) {
			communeSection = new String[12];
			communeSection[0] = "11eme La vallee";
			communeSection[1] = "3eme La vallee";
			communeSection[2] = "4eme La vallee";
			communeSection[3] = "5eme Haut Gandou";
			communeSection[4] = "6eme Bas de la croix";
			communeSection[5] = "8eme orangers";
			communeSection[6] = "9eme Bas gris gris";
			communeSection[7] = "Bas Grandou";
			communeSection[8] = "Bras de gauche";
			communeSection[9] = "Bresilienne";
			communeSection[10] = "Haut Grandou";
			communeSection[11] = "Tou mahot";
		}
		else if (commune.equals("Belle Anse")) {
			communeSection = new String[7];
			communeSection[0] = "Baie d_Orange";
			communeSection[1] = "BelAir";
			communeSection[2] = "Callumette";
			communeSection[3] = "CorailLamothe";
			communeSection[4] = "Mabriole";
			communeSection[5] = "Mapou";
			communeSection[6] = "Pichon";
		}
		else if (commune.equals("Cote de fer")) {
			communeSection = new String[8];
			communeSection[0] = "3eme Bras de gauche";
			communeSection[1] = "6eme Jamais-Vu";
			communeSection[2] = "Amazone";
			communeSection[3] = "Boucan Belier";
			communeSection[4] = "Cote de fer";
			communeSection[5] = "Gris-Gris";
			communeSection[6] = "Jamais vus";
			communeSection[7] = "Labich";
		}
		else if (commune.equals("Grand Gosier")) {
			communeSection = new String[3];
			communeSection[0] = "Bodarie";
			communeSection[1] = "CollinedesChaines";
			communeSection[2] = "Grand Gosier";
		}		
		else if (commune.equals("La vallee")) {
			communeSection = new String[2];
			communeSection[0] = "1ere Musac";
			communeSection[1] = "Morne a Brule";
		}		
		else if (commune.equals("Thiotte")) {
			communeSection = new String[2];
			communeSection[0] = "Pot de Chambre/2eMareMirande";
			communeSection[1] = "Thiotte 1ereColombier";
		}
		else {
			communeSection = new String[0];
		}
		return communeSection;
		
	}
	 
   
    public static void main (String args[]) throws Exception {
    	SmsReader reader = null;
    	if (args.length < 1) {
    		reader = new SmsReader();
            reader.readMsgsFromDb("jdbc:sqlite:sample.db");
            String[] arr = reader.getMessagesAsArray();
            for (int i = 0; i < arr.length; i ++) {
            	System.out.println(arr[i]);
            }
        } else if (args.length < 2) {
        	reader = new SmsReader(args[0]);  // Uses default encoding
            reader.readFile();
        } else {
            reader = new SmsReader(args[0], args[1]);
            reader.readFile();
        }
        
    }
}

