package com.ril.ran;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/*cNum	Band
0	2300 C1
1	2300 C1
2	2300 C1
3	1800
4	1800
5	1800
6	850-C1
7	850-C2
9	2300 C2
10	2300 C2
11	2300 C2
12	2300 C2
13	2300 C2
14	2300 C2
15	850-C1
16	850-C2
18	2300 C1
19	2300 C1
20	2300 C1
24	850-C1
25	850-C2
*/


public class MimoAnalyzer {

	int biSectorNum = 0;
	final int reportedHours = 24;
	
	public static void main(String[] args) {
		
		MimoAnalyzer ma = new MimoAnalyzer();
		
		File file = new File("C:\\Users\\Ken\\Documents\\5G\\Projects\\Data\\Haryana_30102018.csv");
		
		HashMap<String, HashMap<String, NetworkStatsData>> aMap = ma.loadFile(file);
		
		ma.processBiSectors(aMap);
		
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
				      //if (record.getBand().contentEquals("2300_1"))
				    	  //System.out.println(record.getCellId()+"{"+ bMap +"}");
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
	
	void processBiSectors(HashMap<String, HashMap<String, NetworkStatsData>> aMap) {
		for (Map.Entry<String,HashMap<String, NetworkStatsData>> cell : aMap.entrySet()) { 
			String cellId = cell.getKey();
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
						biSectorNum++;
						NetworkStatsData biSector_1_hourlyRecord = biSectorCell_1_map.get(hourlyRecord.getKey());
						if (biSector_1_hourlyRecord != null) {
							NetworkStatsData biSector_2_hourlyRecord = hourlyRecord.getValue();
							biSector_1_hourlyRecord.setIsBiSector(true);
							biSector_1_hourlyRecord.setRrcConnectedUsers_0309(
									biSector_1_hourlyRecord.getRrcConnectedUsers_0309()+biSector_2_hourlyRecord.getRrcConnectedUsers_0309());
							biSector_1_hourlyRecord.setMeanCQI_1052(
									(biSector_1_hourlyRecord.getMeanCQI_1052()+biSector_2_hourlyRecord.getMeanCQI_1052())/2);
							biSector_1_hourlyRecord.setInterference_0811(
									(biSector_1_hourlyRecord.getInterference_0811()+biSector_2_hourlyRecord.getInterference_0811())/2);
						}
					} 
				}	
			}
		}
		System.out.println("Bi-sector Num: " + biSectorNum/reportedHours);
	}
	
	public void printMimoSiteNum(HashMap<String, HashMap<String, NetworkStatsData>> aMap, double growth_factor) {
		
		final double CQI = 7.5;
		final double CQI_BISECTOR = 7;
		final int INTERFERENCE = -107;
		final double GROWTH_FACTOR = growth_factor;
		
		int total2300Sectors = 0;
		
		Set<String> sectorRRC_180 = new HashSet<String>();
		Set<String> sectorRRC_150 = new HashSet<String>();
		Set<String> sectorRRC_120 = new HashSet<String>();
		Set<String> sectorRRC_90 = new HashSet<String>();
		Set<String> sectorRRC_60 = new HashSet<String>();
		
		Set<String> biSector_180 = new HashSet<String>();
		Set<String> biSector_150 = new HashSet<String>();
		Set<String> biSector_120 = new HashSet<String>();
		Set<String> biSector_90 = new HashSet<String>();
		Set<String> biSector_60 = new HashSet<String>();
		
			
		for (Map.Entry<String,HashMap<String, NetworkStatsData>> cell : aMap.entrySet()) { 
			String cellName = cell.getKey();
			String sector = cellName.split("_c")[1];
			if (sector.equalsIgnoreCase("0") || sector.equalsIgnoreCase("1") || sector.equalsIgnoreCase("2") ||
					sector.equalsIgnoreCase("12") || sector.equalsIgnoreCase("13") || sector.equalsIgnoreCase("14")) {
				total2300Sectors++;
				int maxRrcConnectedUsers = 0;
				boolean isBiSector = false;
				double CqiToCheck = CQI;
				
				for (Map.Entry<String, NetworkStatsData> hourlyRecord : cell.getValue().entrySet()) {
					NetworkStatsData mm = hourlyRecord.getValue();
					
					if (mm.getIsBiSector()) {
						CqiToCheck = CQI_BISECTOR;
						isBiSector = true;
					}
					
					if ((mm.getMeanCQI_1052() > CqiToCheck) && (mm.getInterference_0811() < INTERFERENCE) && 
							(mm.getRrcConnectedUsers_0309() > maxRrcConnectedUsers)) {		
						maxRrcConnectedUsers = mm.getRrcConnectedUsers_0309();
					}
				}	
	
				if (maxRrcConnectedUsers  * GROWTH_FACTOR >= 180) {
					sectorRRC_180.add(cell.getKey());
					if (isBiSector) biSector_180.add(cell.getKey());
				} else if (maxRrcConnectedUsers  * GROWTH_FACTOR >= 150) {
					sectorRRC_150.add(cell.getKey());
					if (isBiSector) biSector_150.add(cell.getKey());
				} else if (maxRrcConnectedUsers * GROWTH_FACTOR >= 120) {
					sectorRRC_120.add(cell.getKey());
					if (isBiSector) biSector_120.add(cell.getKey());
				} else if (maxRrcConnectedUsers * GROWTH_FACTOR >= 90) {
					sectorRRC_90.add(cell.getKey());
					if (isBiSector) biSector_90.add(cell.getKey());
				} else if (maxRrcConnectedUsers * GROWTH_FACTOR >= 60) {
					sectorRRC_60.add(cell.getKey());
					if (isBiSector) biSector_60.add(cell.getKey());
				}
			}
		}
		System.out.println("Total 2300 band sectors with bi-sectors consolidated: " + total2300Sectors);
		
		System.out.println(">180: " + sectorRRC_180.size() + ", Percentage: " + 
		(double)sectorRRC_180.size()/(double)total2300Sectors + ", BiSectorCells: " + biSector_180.size());
		
		System.out.println("150-180: " + sectorRRC_150.size() + ", Percentage: " + 
		(double)sectorRRC_150.size()/(double)total2300Sectors + ", BiSectorCells: " + biSector_150.size());
		
		System.out.println("120-150: " + sectorRRC_120.size() + ", Percentage: " + 
		(double)sectorRRC_120.size()/(double)total2300Sectors + ", BiSectorCells: " + biSector_120.size());
		
		System.out.println("90-120: " + sectorRRC_90.size() + ", Percentage: " + 
		(double)sectorRRC_90.size()/(double)total2300Sectors + ", BiSectorCells: " + biSector_90.size());
		
		System.out.println("60-90: " + sectorRRC_60.size() + ", Percentage: " + 
		(double)sectorRRC_60.size()/(double)total2300Sectors + ", BiSectorCells: " + biSector_60.size());
		
		//System.out.println("======");
		//System.out.println(aMap.get(key));
		//System.out.println("======");
		
		/*Iterator<String> itr = sectorRRC_180.iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}*/
	}
	
	
	public void printFwaSiteNum(HashMap<String, HashMap<String, NetworkStatsData>> aMap) {
		for (Map.Entry<String,HashMap<String, NetworkStatsData>> cell : aMap.entrySet()) {
			
		}
	}
	
}
