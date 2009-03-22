package cs546.group7 ;

import java.io.BufferedReader;
import java.io.FileReader;

class BuildingMap {
	
	/* The function Checks if there is a Code (for a building) in the database
	 * for the received Code value
	 * If exists it returns true 
	 * else returns false
	 */
	public boolean codeCheckInDB(String Code) {
		boolean res = false;
		
		try {
			FileReader fr = new FileReader("database_file//code_lat_long_tile.txt");
			BufferedReader br = new BufferedReader(fr);
			String aLine;
			String code;
			while((aLine = br.readLine()) != null ) {
				code = aLine.substring(0, 3);
				if(code.equals(Code)) {
					res = true;
					break;
				}
			}
		}
		catch(Exception e) {
			//System.out.println("File output error");
		}
		
		return res;
	}
	
	/* The function Checks if there is a Code (for a building) in the database
	 * for the received Latitude and Longitude values
	 * If exists it returns the code 
	 * else returns "null" value
	 */
	public String codeCalForLatLong( double Lat, double Lon) {
		String Code = null;
		
		try {
			FileReader fr = new FileReader("database_file//code_lat_long_tile.txt");
			BufferedReader br = new BufferedReader(fr);
			String aLine;
			String lat, lon;
			double lati, longi;
			while((aLine = br.readLine()) != null ) {
				lat = aLine.substring(4, 17);
				lon = aLine.substring(18, 33);
				lati = Double.parseDouble(lat);
				longi = Double.parseDouble(lon);
				if(lati == Lat && longi == Lon) {
					Code = aLine.substring(0, 3);
					break;
				}
			}
		}
		catch(Exception e) {
			//System.out.println("File output error");
		}
		
		return Code;
	}
	
	/* The function returns the most appropriate tile for the received Latitude and Longitude values
	 */
	public String tileCalForLatLong(double Lat, double Lon) {
		String Tile = null;
		
		try {
			FileReader fr = new FileReader("code_lat_long_tile.txt");
			BufferedReader br = new BufferedReader(fr);
			String aLine; 
			String lat, lon;
			double lati = 0, longi = 0;
			int i =0;
			double res = 100, res_temp = 0;
			while((aLine = br.readLine()) != null ) {
				lat = aLine.substring(4, 17);
				lon = aLine.substring(18, 33);
				lati = Double.parseDouble(lat);
				longi = Double.parseDouble(lon);
				res_temp = Math.sqrt(Math.pow((Lat - lati), 2) + Math.pow((Lon - longi), 2));
				if(res > res_temp || i == 0) {
					Tile = aLine.substring(34, 37);
					res = res_temp;
				}
				i++;
			}
		}
		catch(Exception e) {
			System.out.println("File input error");
		}
		
		return Tile;
	}
	
	/*The function gets Code for a building 
	 * and returns the corresponding Latitude and Longitude values 
	 * of the building as an object for the class "LatitudeLongitude"
	 * The class "LatitudeLongitude" has two variable of double : "lat" and "lon"
	 * 
	 * The lat and lon values are true if the Code exsits,
	 * else lat and lon values are 999, 999 to idicate that the Code does not exist in the database 
	 */
	public LatitudeLongitude latLongCalForCode( String Code) {
		LatitudeLongitude LatLonObj = new LatitudeLongitude();
		LatLonObj.lat = 999;
		LatLonObj.lon = 999;
		
		try {
			FileReader fr = new FileReader("database_file//code_lat_long_tile.txt");
			BufferedReader br = new BufferedReader(fr);
			String aLine;
			String code;
			String lat, lon;
			while((aLine = br.readLine()) != null ) {
				code = aLine.substring(0, 3);
				if(code.equals(Code)) {
					lat = aLine.substring(4, 17);
					lon = aLine.substring(18, 33);
					LatLonObj.lat = Double.parseDouble(lat);
					LatLonObj.lon = Double.parseDouble(lon);
					break;
				}
			}
		}
		catch(Exception e) {
			//System.out.println("File output error");
		}
		
		return LatLonObj;
	}
}

class LatitudeLongitude {
	double lat;
	double lon;
}