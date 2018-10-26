/**
 * 
 */
package com.ril.ran;

/**
 * @author Ken
 * 
 * Read the daily network statistic csv file. 
 * Process the bi-sectors by consolidating the RRC_Connected_Users, averaging the CQI and IOT_Interference. 
 * Evaluate the number of cells that can benefit from M-MIMO. 
 *
 */

public class NetworkStatsData {

	private String cellId;
	private Boolean isBiSector = false;
	private String date;
	private String time;
	private String circle;
	private String jioCenter;
	private String city;
	private String band;
	private double dlPrbUtilization_1187=0.0;
	private double ulPrbUtilization_1188=0.0;
	private double cellEffectiveAirMacDLThroughput_1397=0.0;
	private int rrcConnectedUsers_0309=0;
	private double ipDLThroughtputQCI9_1432=0.0;
	private double meanCQI_1052=0.0;
	private double interference_0811=0.0;

	
	public String getCellId() {
		return cellId;
	}




	public void setCellId(String cellId) {
		this.cellId = cellId;
	}




	public Boolean getIsBiSector() {
		return isBiSector;
	}




	public void setIsBiSector(Boolean isBiSector) {
		this.isBiSector = isBiSector;
	}




	public String getDate() {
		return date;
	}

	

	public void setDate(String date) {
		this.date = date;
	}




	public String getCircle() {
		return circle;
	}




	public void setCircle(String circle) {
		this.circle = circle;
	}




	public String getJioCenter() {
		return jioCenter;
	}




	public void setJioCenter(String jioCenter) {
		this.jioCenter = jioCenter;
	}




	public String getCity() {
		return city;
	}




	public void setCity(String city) {
		this.city = city;
	}




	public String getBand() {
		return band;
	}




	public void setBand(String band) {
		this.band = band;
	}




	public double getDlPrbUtilization_1187() {
		return dlPrbUtilization_1187;
	}




	public void setDlPrbUtilization_1187(double dlPrbUtilization_1187) {
		this.dlPrbUtilization_1187 = dlPrbUtilization_1187;
	}




	public double getUlPrbUtilization_1188() {
		return ulPrbUtilization_1188;
	}




	public void setUlPrbUtilization_1188(double ulPrbUtilization_1188) {
		this.ulPrbUtilization_1188 = ulPrbUtilization_1188;
	}




	public double getCellEffectiveAirMacDLThroughput_1397() {
		return cellEffectiveAirMacDLThroughput_1397;
	}




	public void setCellEffectiveAirMacDLThroughput_1397(double cellEffectiveAirMacDLThroughput_1397) {
		this.cellEffectiveAirMacDLThroughput_1397 = cellEffectiveAirMacDLThroughput_1397;
	}




	public int getRrcConnectedUsers_0309() {
		return rrcConnectedUsers_0309;
	}




	public void setRrcConnectedUsers_0309(int rrcConnectedUsers_0309) {
		this.rrcConnectedUsers_0309 = rrcConnectedUsers_0309;
	}




	public double getMeanCQI_1052() {
		return meanCQI_1052;
	}




	public void setMeanCQI_1052(double meanCQI_1052) {
		this.meanCQI_1052 = meanCQI_1052;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public double getIpDLThroughtputQCI9_1432() {
		return ipDLThroughtputQCI9_1432;
	}


	public void setIpDLThroughtputQCI9_1432(double ipDLThroughtputQCI9_1432) {
		this.ipDLThroughtputQCI9_1432 = ipDLThroughtputQCI9_1432;
	}


	public double getInterference_0811() {
		return interference_0811;
	}


	public void setInterference_0811(double interference_0811) {
		this.interference_0811 = interference_0811;
	}


}
