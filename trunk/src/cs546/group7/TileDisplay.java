package cs546.group7 ;

import java.io.BufferedReader;
import java.io.FileReader;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

class TileDisplay extends Activity{
	public void tileDisplay(double latitude, double longitude) {
	
		setContentView(R.layout.tile_display) ;
		
		double get_lat = 34.0620426091;
		double get_lon = -118.3804675908;
		double lati, longi, res, res_temp;
		//String tile = null, code = null;
		String tile_temp = null;
		Uri tile = null;
		try {
			FileReader fr = new FileReader("database_file//code_lat_long_tile.txt");
			BufferedReader br = new BufferedReader(fr);
			String aLine; 
			String lat, lon;
			int i =0;
			res = 100;
			while((aLine = br.readLine()) != null ) {
				lat = aLine.substring(4, 17);
				lon = aLine.substring(18, 33);
				lati = Double.parseDouble(lat);
				longi = Double.parseDouble(lon);
				res_temp = Math.sqrt(Math.pow((get_lat - lati), 2) + Math.pow((get_lon - longi), 2));
				if(res > res_temp || i == 0) {
					tile_temp = aLine.substring(34, 38); 
					res = res_temp;
				}
				i++;
			}
		}
		catch(Exception e) {
			//System.out.println("File output error");
		}

		tile_temp = "R.drawable." + tile_temp;
		tile = Uri.parse(tile_temp);
		
		ImageView img = (ImageView) findViewById(R.id.full_tile) ;
		img.setImageURI(tile);
	}
}