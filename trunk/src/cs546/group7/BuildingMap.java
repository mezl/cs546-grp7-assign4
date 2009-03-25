package cs546.group7 ;

import java.io.BufferedReader;
import java.io.FileReader;

import android.util.Log;

class BuildingMap {

private static String[] m_db = new String[] {
   "CAP 34.0261840820 -118.2894515991 t120.gif 42 150",
   "CAR 34.0263252258 -118.2872619629 t120.gif 208 167",
   "LAB 34.0262565613 -118.2885742188 t120.gif 117 148",
   "OHC 34.0258293152 -118.2855682373 t130.gif 138 190",
   "TRH 34.0254020691 -118.2816162109 t140.gif 245 246",
   "BUN 34.0255355835 -118.2795715332 t150.gif 216 242",
   "TRE 34.0256843567 -118.2812118530 t150.gif 71 207",
   "ABM 34.0255966187 -118.2760162354 t170.gif 17 225",
   "BDX 34.0239944458 -118.2899017334 t210.gif 230 160",
   "GEE 34.0252418518 -118.2899703979 t210.gif 241 18",
   "GPC 34.0244598389 -118.2900238037 t210.gif 236 105",
   "GSE 34.0239677429 -118.2913742065 t210.gif 106 160",
   "MTS 34.0231056213 -118.2906112671 t210.gif 169 242",
   "BDF 34.0231437683 -118.2896041870 t220.gif 20 251",
   "FLT 34.0249023438 -118.2883453369 t220.gif 142 23",
   "FMS 34.0236663818 -118.2877044678 t220.gif 196 177",
   "KAB 34.0238342285 -118.2886505127 t220.gif 107 167",
   "KOH 34.0246353149 -118.2879409790 t220.gif 181 43",
   "LRC 34.0242805481 -118.2883224487 t220.gif 138 130",
   "MAC 34.0238342285 -118.2882080078 t220.gif 121 153",
   "MCC 34.0249557495 -118.2874145508 t220.gif 218 39",
   "MCE 34.0249023438 -118.2871627808 t220.gif 249 50",
   "POA 34.0234718323 -118.2873611450 t220.gif 221 204",
   "POB 34.0233917236 -118.2871475220 t220.gif 239 217",
   "PSB 34.0247612000 -118.2894592285 t220.gif 39 58",
   "WTO 34.0245933533 -118.2876739502 t220.gif 199 51",
   "CSS 34.0231590271 -118.2854080200 t230.gif 150 241",
   "DEN 34.0240211487 -118.2863388062 t230.gif 68 139",
   "HSS 34.0236396790 -118.2869110107 t230.gif 15 189",
   "IMS 34.0239868164 -118.2856292725 t230.gif 135 151",
   "JEF 34.0247421265 -118.2868423462 t230.gif 18 67",
   "LUC 34.0231170654 -118.2858276367 t230.gif 116 235",
   "SCA 34.0233116150 -118.2861938477 t230.gif 79 231",
   "SHC 34.0234451294 -118.2851943970 t230.gif 169 205",
   "URC 34.0234184265 -118.2846527100 t230.gif 217 213",
   "UUC 34.0231437683 -118.2842254639 t240.gif 10 237",
   "CHE 34.0249557495 -118.2797622681 t250.gif 197 30",
   "COR 34.0247077942 -118.2808456421 t250.gif 101 59",
   "HAS 34.0246276855 -118.2806625366 t250.gif 74 68",
   "ROM 34.0249671936 -118.2800216675 t250.gif 142 35",
   "RZC 34.0242347717 -118.2792663574 t250.gif 208 117",
   "SHR 34.0235595703 -118.2810745239 t250.gif 56 171",
   "TSS 34.0237045288 -118.2799224854 t250.gif 164 161",
   "IFT 34.0242347717 -118.2772445679 t260.gif 118 119",
   "SGA 34.0250473022 -118.2785491943 t260.gif 17 17",
   "DRB 34.0214538574 -118.2910919189 t310.gif 131 168",
   "DRC 34.0226631165 -118.2900772095 t310.gif 238 37",
   "DWE 34.0219764709 -118.2913970947 t310.gif 110 116",
   "KAP 34.0223884583 -118.2910156250 t310.gif 142 66",
   "PSA 34.0208930969 -118.2899856567 t310.gif 202 204",
   "RRI 34.0221633911 -118.2901306152 t310.gif 220 84",
   "801 34.0210342407 -118.2883758545 t320.gif 137 221",
   "CFH 34.0220413208 -118.2888107300 t320.gif 93 116",
   "CFX 34.0219764709 -118.2878646851 t320.gif 178 128",
   "CWO 34.0219078064 -118.2895507812 t320.gif 19 132",
   "CWT 34.0219421387 -118.2897186279 t320.gif 9 123",
   "DNI 34.0214157104 -118.2887954712 t320.gif 92 152",
   "GFS 34.0213356018 -118.2880020142 t320.gif 165 193",
   "HRC 34.0217552185 -118.2893905640 t320.gif 34 144",
   "IMF 34.0230712891 -118.2877120972 t320.gif 207 11",
   "LTS 34.0226249695 -118.2883682251 t320.gif 161 65",
   "MAR 34.0222816467 -118.2872772217 t320.gif 233 87",
   "OHE 34.0208396912 -118.2894287109 t320.gif 31 246",
   "PIC 34.0228233337 -118.2878723145 t320.gif 180 36",
   "SGM 34.0214614868 -118.2889785767 t320.gif 62 201",
   "ADM 34.0209388733 -118.2855682373 t330.gif 143 235",
   "ASC 34.0220413208 -118.2862777710 t330.gif 88 123",
   "ASI 34.0229988098 -118.2864761353 t330.gif 46 3",
   "BIT 34.0223884583 -118.2859649658 t330.gif 100 70",
   "BMH 34.0227050781 -118.2846527100 t330.gif 223 36",
   "CTV 34.0229988098 -118.2855987549 t330.gif 139 4",
   "HER 34.0224761963 -118.2868347168 t330.gif 20 54",
   "LPB 34.0227508545 -118.2858581543 t330.gif 99 26",
   "MUS 34.0228576660 -118.2849655151 t330.gif 188 10",
   "NCT 34.0220298767 -118.2849655151 t330.gif 173 110",
   "PED 34.0214538574 -118.2866516113 t330.gif 65 194",
   "RHM 34.0226249695 -118.2851943970 t330.gif 174 45",
   "SSS 34.0227966309 -118.2855758667 t330.gif 136 26",
   "THH 34.0221900940 -118.2845382690 t330.gif 236 90",
   "AHN 34.0229301453 -118.2840957642 t340.gif 24 13",
   "BSR 34.0213890076 -118.2822189331 t340.gif 195 159",
   "CAS 34.0222358704 -118.2836227417 t340.gif 64 81",
   "EDL 34.0225639343 -118.2830581665 t340.gif 116 49",
   "JEP 34.0228233337 -118.2839050293 t340.gif 39 14",
   "LVL 34.0217094421 -118.2828216553 t340.gif 140 145",
   "MRF 34.0223083496 -118.2824020386 t340.gif 177 80",
   "NEW 34.0209197998 -118.2816009521 t340.gif 251 238",
   "PLB 34.0228233337 -118.2827148438 t340.gif 142 28",
   "SOS 34.0215415955 -118.2836914062 t340.gif 58 169",
   "SWC 34.0225028992 -118.2822189331 t340.gif 197 60",
   "VKC 34.0212402344 -118.2838516235 t340.gif 34 217",
   "WPH 34.0219078064 -118.2837753296 t340.gif 48 113",
   "DCC 34.0216026306 -118.2805328369 t350.gif 97 158",
   "GEC 34.0209541321 -118.2796630859 t350.gif 183 206",
   "NRC 34.0212669373 -118.2813796997 t350.gif 61 220",
   "PSD 34.0220031738 -118.2813796997 t350.gif 22 142",
   "EEB 34.0197181702 -118.2900390625 t410.gif 234 104",
   "GER 34.0202064514 -118.2905120850 t410.gif 176 49",
   "IRC 34.0187835693 -118.2909622192 t410.gif 145 151",
   "PKS 34.0194244385 -118.2904357910 t410.gif 203 144",
   "PRB 34.0186882019 -118.2899551392 t410.gif 229 218",
   "RTH 34.0200119019 -118.2900009155 t410.gif 252 69",
   "SCD 34.0206871033 -118.2912139893 t410.gif 123 11",
   "BHE 34.0205535889 -118.2884750366 t420.gif 123 13",
   "HAR 34.0190162659 -118.2872085571 t420.gif 232 191",
   "HED 34.0202598572 -118.2891082764 t420.gif 63 52",
   "HNB 34.0207405090 -118.2878189087 t420.gif 179 1",
   "HSH 34.0203857422 -118.2870712280 t420.gif 251 36",
   "LHI 34.0196838379 -118.2877883911 t420.gif 188 110",
   "LJS 34.0199394226 -118.2870254517 t420.gif 247 81",
   "MBC 34.0189437866 -118.2883224487 t420.gif 133 130",
   "PCE 34.0201110840 -118.2887649536 t420.gif 93 71",
   "PHE 34.0191955566 -118.2888107300 t420.gif 92 166",
   "RRB 34.0200195312 -118.2874984741 t420.gif 213 82",
   "SAL 34.0195159912 -118.2894287109 t420.gif 32 137",
   "SLH 34.0196762085 -118.2874755859 t420.gif 241 131",
   "SSC 34.0198173523 -118.2892227173 t420.gif 52 90",
   "SSL 34.0195846558 -118.2887039185 t420.gif 102 128",
   "VHE 34.0201644897 -118.2881927490 t420.gif 149 50",
   "WAH 34.0192565918 -118.2880020142 t420.gif 177 162",
   "YWC 34.0205459595 -118.2873306274 t420.gif 3 26",
   "ACB 34.0192375183 -118.2869415283 t430.gif 13 157",
   "ACC 34.0191688538 -118.2855453491 t430.gif 139 173",
   "AHF 34.0193710327 -118.2848129272 t430.gif 191 115",
   "BKS 34.0205650330 -118.2866058350 t430.gif 41 16",
   "BRI 34.0188636780 -118.2858276367 t430.gif 119 210",
   "CEM 34.0194702148 -118.2866210938 t430.gif 43 138",
   "HOH 34.0188217163 -118.2853546143 t430.gif 171 205",
   "LAW 34.0187568665 -118.2843551636 t430.gif 240 216",
   "MHP 34.0188293457 -118.2868652344 t430.gif 38 221",
   "OCW 34.0196914673 -118.2864990234 t430.gif 70 123",
   "SHS 34.0196304321 -118.2868881226 t430.gif 16 114",
   "STO 34.0202522278 -118.2868347168 t430.gif 10 91",
   "STU 34.0201454163 -118.2856216431 t430.gif 126 57",
   "TCC 34.0201911926 -118.2862319946 t430.gif 67 70",
   "TGF 34.0193099976 -118.2843627930 t430.gif 246 175",
   "ZHS 34.0192832947 -118.2863845825 t430.gif 67 156",
   "ALM 34.0194244385 -118.2826614380 t440.gif 151 148",
   "DML 34.0200843811 -118.2836990356 t440.gif 61 72",
   "DMT 34.0196304321 -118.2819747925 t440.gif 220 111",
   "DXM 34.0192909241 -118.2825393677 t440.gif 178 134",
   "FAC 34.0189170837 -118.2837982178 t440.gif 51 213",
   "JHH 34.0195426941 -118.2841796875 t440.gif 19 143",
   "JKP 34.0188636780 -118.2825775146 t440.gif 138 217",
   "PSX 34.0204582214 -118.2824783325 t440.gif 217 32",
   "PTD 34.0199394226 -118.2825241089 t440.gif 167 77",
   "REG 34.0186882019 -118.2823028564 t440.gif 186 231",
   "RGL 34.0192832947 -118.2834854126 t440.gif 78 162",
   "TRO 34.0191574097 -118.2821655273 t440.gif 217 179",
   "FIG 34.0198516846 -118.2814712524 t450.gif 8 101",
   "PS2 34.0190505981 -118.2805480957 t450.gif 96 179",
   "RMH 34.0192489624 -118.2809753418 t450.gif 34 183",
   "TFE 34.0204315186 -118.2810745239 t450.gif 65 48",
   "UGB 34.0198440552 -118.2804794312 t450.gif 108 106",
   "CAL 34.0190429688 -118.2763519287 t460.gif 243 136",
   "ELB 34.0194435120 -118.2779388428 t460.gif 80 135",
   "UPX 34.0198364258 -118.2773284912 t460.gif 145 81",
   "WWG 34.0184020996 -118.2889709473 t520.gif 79 18",
   "PWE 34.0184402466 -118.2833709717 t540.gif 79 10",
   "TYL 34.0181808472 -118.2809982300 t550.gif 40 35",
} ;

	/* The function Checks if there is a Code (for a building) in the database
	 * for the received Code value
	 * If exists it returns true 
	 * else returns false
	 */
	public boolean codeCheckInDB(String Code) {
		boolean res = false;
		
		try {
			String code;
			int i = 0;
			while(i < m_db.length) {
				code = m_db[i].substring(0, 3);
				if(code.equals(Code)) {
					res = true;
					break;
				}
				i++;
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
			String lat, lon;
			double lati, longi;
			int i = 0;
			double res = 100, res_temp = 0;
			while(i < m_db.length) {
				lat = m_db[i].substring(4, 17);
				lon = m_db[i].substring(18, 33);
				lati = Double.parseDouble(lat);
				longi = Double.parseDouble(lon);
				res_temp = Math.sqrt(Math.pow((Lat - lati), 2) + Math.pow((Lon - longi), 2));
				if(res > res_temp || i == 0) {
					Code = m_db[i].substring(0, 3);
					res = res_temp;
				}
				i++;
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
			String lat, lon;
			double lati = 0, longi = 0;
			int i =0;
			double res = 100, res_temp = 0;
			while(i < m_db.length) {
				lat = m_db[i].substring(4, 17);
				lon = m_db[i].substring(18, 33);
				lati = Double.parseDouble(lat);
				longi = Double.parseDouble(lon);
				res_temp = Math.sqrt(Math.pow((Lat - lati), 2) + Math.pow((Lon - longi), 2));
				if(res > res_temp || i == 0) {
					Tile = m_db[i].substring(34, 38);
					res = res_temp;
				}
				i++;
			}
		}
		catch(Exception e) {
			//System.out.println("File input error");
			Log.e(null, "Error: " + e);
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
			String code;
			String lat, lon;
			int i = 0;
			while(i < m_db.length) {
				code = m_db[i].substring(0, 3);
				if(code.equals(Code)) {
					lat = m_db[i].substring(4, 17);
					lon = m_db[i].substring(18, 33);
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
	
	/*
	 * The function takes the latitude and longitude values and returns 
	 * appropriate coordinates for the given latitude and longitude values
	 * as an object for a class Coordinates which has two variables as X_coord and
	 * X_coord
	 */
	public Coordinates coordCalForLatLong(double Lat, double Lon) {
		Coordinates Coords = new Coordinates();

		try { 
			String lat, lon;
			double lati = 0, longi = 0;
			int i =0;
			double res = 100, res_temp = 0;
			String xtemp = "", ytemp = "";
			while(i < m_db.length) {
				lat = m_db[i].substring(4, 17);
				lon = m_db[i].substring(18, 33);
				lati = Double.parseDouble(lat);
				longi = Double.parseDouble(lon);
				res_temp = Math.sqrt(Math.pow((Lat - lati), 2) + Math.pow((Lon - longi), 2));
				if(res > res_temp || i == 0) {
					int flag = 0;
					for(int j = 43; j < m_db[i].length(); j++) {
						if(m_db[i].charAt(j) == ' ') {
							flag = 1;
							j++;
						}
						if(flag == 0) {
							xtemp = xtemp + m_db[i].charAt(j);
						}
						else {
							ytemp = ytemp + m_db[i].charAt(j);
						} 
					}
					
					Coords.X_coord = Integer.parseInt(xtemp);
					Coords.Y_coord = Integer.parseInt(ytemp);
					xtemp = ""; ytemp = "";
					
					res = res_temp;
				}
				i++;
			}
		}
		catch(Exception e) {
			Log.e(null, "Error: " + e);
		}
		
		return Coords;
	}
	
	/*
	 * The function returns the coordinates for the given Code of the building
	 */
	public Coordinates coordCalForCode(String Code) {
		Coordinates coords = new Coordinates();
		
		coords.X_coord = 999;
		coords.Y_coord = 999;
		
		try { 
			String code;
			int i =0;
			String xtemp = "", ytemp = "";
			while(i < m_db.length) {
				code = m_db[i].substring(0, 3);
				if(code.equals(Code)) {
					int flag = 0;
					for(int j = 43; j < m_db[i].length(); j++) {
						if(m_db[i].charAt(j) == ' ') {
							flag = 1;
							j++;
						}
						if(flag == 0) {
							xtemp = xtemp + m_db[i].charAt(j);
						}
						else {
							ytemp = ytemp + m_db[i].charAt(j);
						} 
					}
					
					coords.X_coord = Integer.parseInt(xtemp);
					coords.Y_coord = Integer.parseInt(ytemp);
					xtemp = ""; ytemp = "";
					break;
				}
				i++;
			}
		}
		catch(Exception e) {
			Log.e(null, "Error: " + e);
		}
		
		return coords;
	}
	
	/*
	 * Function to return Tile number for the Building with a Code
	 */
	public String tileCalForCode(String Code) {
		String tile = null;
		
		try {
			String code;
			int i = 0;
			while(i < m_db.length) {
				code = m_db[i].substring(0, 3);
				if(code.equals(Code)) {
					tile = m_db[i].substring(34, 38);
					break;
				}
			}
		}
		catch(Exception e) {
			//System.out.println("File output error");
		}
		
		return tile;
	}
}

class LatitudeLongitude {
	double lat;
	double lon;
}

class Coordinates {
	int X_coord;
	int Y_coord;
}