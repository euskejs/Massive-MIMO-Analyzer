package com.ril.ran;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/*
 * Leave bi-sectors
 */
public class MimoAnalyzer2 {
	
	public static void main(String[] args) {
		
		MimoAnalyzer2 ma = new MimoAnalyzer2();
		
		File file = new File("C:\\Users\\Ken\\Documents\\5G\\Projects\\Data\\Rajasthan_29102018.csv");
		
		HashMap<String, HashMap<String, NetworkStatsData>> aMap = ma.loadFile(file);
		
		ma.process2300Carriers(aMap);
		
		// Estimate with current load
		System.out.println("Under current load:");
		ma.printMimoSiteNum(aMap, 1);
		
		// Estimate with x1.6 load
		System.out.println("Under x1.6 load");
		ma.printMimoSiteNum(aMap, 1.6);
		
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
			    		  else if (index == 10)
			    			  record.setCellEffectiveAirMacDLThroughput_1397(Double.parseDouble(data));
			    		  else if (index == 11)
			    			  record.setRrcConnectedUsers_0309((int) Math.round(Double.parseDouble(data)));
			    		  else if (index == 14)
			    			  record.setMeanCQI_1052(Double.parseDouble(data));
			    		  else if (index == 17)
			    			  record.setIpDLThroughtputQCI9_1432(Double.parseDouble(data));
			    		  else if (index == 19)
			    			  record.setInterference_0811(Double.parseDouble(data));	  
			    		  //else
			    			  //System.out.println("invalid data:" + rowIndex + ":"+ data);
			    	  } catch (NumberFormatException e) {
			    		  //e.printStackTrace();
			    	  }
			    	  
			    	  index++;
			    	  
			      }
			      rowScanner.close();
			      index = 0;
			      
			      if (record.getBand().contentEquals("2300_1") || record.getBand().contentEquals("2300_2") ) {
			    	 
				      bMap.put(record.getDate()+"-"+record.getTime(), record);
				      //System.out.println("***"+record.getDate()+"-"+record.getTime());
				      aMap.put(record.getCellId(), bMap);
			      } 
			         
			}
			sc.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("total rows in aMap: " + aMap.size());
		return aMap;
	}
	
	void process2300Carriers(HashMap<String, HashMap<String, NetworkStatsData>> aMap) {
		for (Map.Entry<String,HashMap<String, NetworkStatsData>> cell : aMap.entrySet()) { 
			String cellId = cell.getKey();
			if (cellId.contains("_c12") || cellId.contains("_c13") || cellId.contains("_c14") ||
					cellId.contains("_c9") || cellId.contains("_c10") || cellId.contains("_c11")) {
				String sectorName = cellId;
				int sector = Integer.parseInt(sectorName.split("_c")[1]);
				String siteName = sectorName.split("_c")[0];
				
				String sectorCellName_1;
				if (sectorName.contains("_c12") || sectorName.contains("_c13") || sectorName.contains("_c14")) {
					sectorCellName_1 = siteName + "_c" + (sector - 12);
				} else {
					sectorCellName_1 = siteName + "_c" + (sector + 9);
				}
				
				
				for (Map.Entry<String, NetworkStatsData> hourlyRecord : cell.getValue().entrySet()) {
					HashMap<String, NetworkStatsData> carrier1CellMap = aMap.get(sectorCellName_1);
				
					if (carrier1CellMap != null) {
						NetworkStatsData carrier_1_hourlyRecord = carrier1CellMap.get(hourlyRecord.getKey());
						NetworkStatsData carrier_2_hourlyRecord = hourlyRecord.getValue();
						
						if ((carrier_1_hourlyRecord != null) && (carrier_2_hourlyRecord != null)) {
							carrier_1_hourlyRecord.setMeanCQI_1052((carrier_1_hourlyRecord.getMeanCQI_1052() + carrier_2_hourlyRecord.getMeanCQI_1052())/2);
							carrier_2_hourlyRecord.setMeanCQI_1052((carrier_1_hourlyRecord.getMeanCQI_1052() + carrier_2_hourlyRecord.getMeanCQI_1052())/2);
							
							carrier_1_hourlyRecord.setInterference_0811((carrier_1_hourlyRecord.getInterference_0811() + carrier_2_hourlyRecord.getInterference_0811())/2);
							carrier_2_hourlyRecord.setInterference_0811((carrier_1_hourlyRecord.getInterference_0811() + carrier_2_hourlyRecord.getInterference_0811())/2);
							
							carrier_1_hourlyRecord.setRrcConnectedUsers_0309(carrier_1_hourlyRecord.getRrcConnectedUsers_0309() + carrier_2_hourlyRecord.getRrcConnectedUsers_0309());
							carrier_2_hourlyRecord.setRrcConnectedUsers_0309(carrier_1_hourlyRecord.getRrcConnectedUsers_0309() + carrier_2_hourlyRecord.getRrcConnectedUsers_0309());
						}
					}
				}	
			} 
			
			if (cellId.contains("_c18") || cellId.contains("_c19") || cellId.contains("_c20") ||
					cellId.contains("_c9") || cellId.contains("_c10") || cellId.contains("_c11")) {
				String biSectorCellName = cellId;
				int sector = Integer.parseInt(biSectorCellName.split("_c")[1]);
				String siteName = biSectorCellName.split("_c")[0];
				
				String biSectorCellName_1;
				if (biSectorCellName.contains("_c18") || biSectorCellName.contains("_c19") || biSectorCellName.contains("_c20")) {
					biSectorCellName_1 = siteName + "_c" + (sector - 18);
				} else {
					biSectorCellName_1 = siteName + "_c" + (sector + 3);
				}
				
				for (Map.Entry<String, NetworkStatsData> hourlyRecord : cell.getValue().entrySet()) {
					HashMap<String, NetworkStatsData> biSectorCell_1_map = aMap.get(biSectorCellName_1);
					//System.out.println(biSectorCellName_1 + ":" + biSectorCellName + ":" + hourlyRecord.getKey());
					
					if (biSectorCell_1_map != null) {
						
						NetworkStatsData biSector_1_hourlyRecord = biSectorCell_1_map.get(hourlyRecord.getKey());
						if (biSector_1_hourlyRecord != null) {
							NetworkStatsData biSector_2_hourlyRecord = hourlyRecord.getValue();
							biSector_1_hourlyRecord.setIsBiSector(true);
							biSector_2_hourlyRecord.setIsBiSector(true);
						}
					} 
				}	
			}
		}	
		//HashMap<String, NetworkStatsData> bMap = aMap.get("I-MH-TTWL-ENB-0005_c0");
	}
	
	public void printMimoSiteNum(HashMap<String, HashMap<String, NetworkStatsData>> aMap, double grownth_factor ) {
		
		final double CQI = 7.5;
		final double CQI_BISECTOR = 7;
		final int INTERFERENCE = -107;
		final double GROWTH_FACTOR = grownth_factor;
		
		Set<String> sectorRRC_180 = new HashSet<String>();
		Set<String> sectorRRC_150 = new HashSet<String>();
		Set<String> sectorRRC_120 = new HashSet<String>();
		Set<String> sectorRRC_90 = new HashSet<String>();
		Set<String> sectorRRC_60 = new HashSet<String>();
		
		Set<String> biSectors = new HashSet<String>();
		
		
			
		for (Map.Entry<String,HashMap<String, NetworkStatsData>> cell : aMap.entrySet()) { 
			
			int maxRrcConnectedUsers = 0;
			
			for (Map.Entry<String, NetworkStatsData> hourlyRecord : cell.getValue().entrySet()) {
				NetworkStatsData mm = hourlyRecord.getValue();
				double CqiToCheck = CQI;
				
				if (mm.getIsBiSector()) {
					CqiToCheck = CQI_BISECTOR;
					biSectors.add(mm.getCellId());
				}
					
				if ((mm.getMeanCQI_1052() > CqiToCheck) && (mm.getInterference_0811() < INTERFERENCE) && 
						(mm.getRrcConnectedUsers_0309() > maxRrcConnectedUsers)) {		
					maxRrcConnectedUsers = mm.getRrcConnectedUsers_0309();
				}
			}
					
			if (maxRrcConnectedUsers  * GROWTH_FACTOR >= 180) {
				sectorRRC_180.add(cell.getKey());
			} else if (maxRrcConnectedUsers  * GROWTH_FACTOR >= 150) {
				sectorRRC_150.add(cell.getKey());
			} else if (maxRrcConnectedUsers * GROWTH_FACTOR >= 120) {
				sectorRRC_120.add(cell.getKey());
			} else if (maxRrcConnectedUsers * GROWTH_FACTOR >= 90) {
				sectorRRC_90.add(cell.getKey());
			} else if (maxRrcConnectedUsers * GROWTH_FACTOR >= 60) {
				sectorRRC_60.add(cell.getKey());
			}	
			
		}
		
		System.out.println(">180: " + sectorRRC_180.size() + ", Percentage: " + 
		(double)sectorRRC_180.size()/(double)aMap.size() );
		
		System.out.println("150-180: " + sectorRRC_150.size() + ", Percentage: " + 
		(double)sectorRRC_150.size()/(double)aMap.size() );
		
		System.out.println("120-150: " + sectorRRC_120.size() + ", Percentage: " + 
		(double)sectorRRC_120.size()/(double)aMap.size() );
		
		System.out.println("90-120: " + sectorRRC_90.size() + ", Percentage: " + 
		(double)sectorRRC_90.size()/(double)aMap.size() );
		
		System.out.println("60-90: " + sectorRRC_60.size() + ", Percentage: " + 
		(double)sectorRRC_60.size()/(double)aMap.size() );
		
		System.out.println("Bi-Sector cell amount: " + biSectors.size());
		
		
	/*Iterator<String> itr = sectorRRC_180.iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}*/	
	}
	
}
