package com.ril.ran;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class DlPrbAnalyzer {
	
	static final double MAX_DL_PRB = 50;
	
	public static void main(String[] args) {
			
			DlPrbAnalyzer da = new DlPrbAnalyzer();
			
			File file = new File("C:\\Users\\Ken\\Documents\\5G\\Projects\\Data\\Rajasthan_29102018.csv");
			
			HashMap<String, HashMap<String, NetworkStatsData>> aMap = da.loadFile(file);
			
			// Estimate with current load
			da.printDlPrb(aMap);		
	}
	
	
	
	public HashMap<String, HashMap<String, NetworkStatsData>> loadFile(File file) {
		HashMap<String, HashMap<String, NetworkStatsData>> aMap = new HashMap<String, HashMap<String, NetworkStatsData>>();
		Scanner sc;
		Scanner rowScanner = null;
		int index = 0;
		HashMap<String, NetworkStatsData> bMap = new HashMap<String, NetworkStatsData>();
		String cellNameTrack = "";
		try {
			sc = new Scanner(file);
			sc.nextLine();
			while (sc.hasNextLine()) {
			      rowScanner = new Scanner(sc.nextLine());
			      rowScanner.useDelimiter(",");
			      NetworkStatsData record = new NetworkStatsData();
			      
			      while (rowScanner.hasNext()) {  
			    	  String data = rowScanner.next().trim();
			    	  try {
			    		  if (index == 0)
			    			  record.setDate(data);
			    		  else if (index == 1)
			    			  record.setTime(data);
			    		  else if (index == 2)
			    			  record.setCircle(data);
			    		  else if (index == 3)
			    			  record.setJioCenter(data);
			    		  else if (index == 4)
			    			  record.setCity(data);
			    		  else if (index == 6)
			    			  record.setBand(data);
			    		  else if (index == 7) {
			    			  record.setCellId(data);
			    			  if (!cellNameTrack.equalsIgnoreCase(data)) {
			    				  bMap = new HashMap<String, NetworkStatsData>();
			    			  }
			    			  cellNameTrack = data;
			    		  }
			    		  else if (index == 8)
			    			  record.setDlPrbUtilization_1187(Double.parseDouble(data));
			    		  else if (index == 9)
			    			  record.setUlPrbUtilization_1188(Double.parseDouble(data));
			    		  //else if (index == 10)
			    			//  record.setCellEffectiveAirMacDLThroughput_1397(Double.parseDouble(data));
			    		  //else if (index == 11)
			    		//	  record.setRrcConnectedUsers_0309((int) Math.round(Double.parseDouble(data)));
			    		 // else if (index == 14)
			    		//	  record.setMeanCQI_1052(Double.parseDouble(data));
			    		 // else if (index == 17)
			    		//	  record.setIpDLThroughtputQCI9_1432(Double.parseDouble(data));
			    		//  else if (index == 19)
			    		//	  record.setInterference_0811(Double.parseDouble(data));	  
			    		  //else
			    			  //System.out.println("invalid data:" + rowIndex + ":"+ data);
			    	  } catch (NumberFormatException e) {
			    		  //e.printStackTrace();
			    	  }
			    	  
			    	  index++;
			    	  
			      }
			      rowScanner.close();
			      index = 0;
			      
			  
			    	 
				  bMap.put(record.getDate()+"-"+record.getTime(), record);
				  aMap.put(record.getCellId(), bMap);
			        
			}
			sc.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("total rows in aMap: " + aMap.size());
		return aMap;
	}
	
	public void printDlPrb(HashMap<String, HashMap<String, NetworkStatsData>> aMap) {
		
		Set<String> sectorMaxPrb =  new HashSet<String>();
		
			
		for (Map.Entry<String,HashMap<String, NetworkStatsData>> cell : aMap.entrySet()) { 
		
			double maxDlPrbUtilization = 0;
			
			for (Map.Entry<String, NetworkStatsData> hourlyRecord : cell.getValue().entrySet()) {
				NetworkStatsData mm = hourlyRecord.getValue();
			
				if (mm.getDlPrbUtilization_1187() > maxDlPrbUtilization) {
					maxDlPrbUtilization = mm.getDlPrbUtilization_1187();
				}
			}
					
			
			if (maxDlPrbUtilization <= MAX_DL_PRB) {
				sectorMaxPrb.add(cell.getKey());
			}
		}
		
		
		
		System.out.println("DL PRB < " + MAX_DL_PRB + ": " + sectorMaxPrb.size());
		
//	Iterator<String> itr = sectorMaxPrb.iterator();
//		while (itr.hasNext()) {
//			System.out.println(itr.next());
//		}
	}

}
