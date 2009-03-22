package cs546.group7 ;

import java.util.HashMap;

import android.app.Activity;
import android.widget.ImageView;

class TileDisplay extends Activity{
	
	public void tileDisplay(double latitude, double longitude) {
	
		setContentView(R.layout.tile_display) ;
		
		HashMap hm = new HashMap(35);
		hm.put("t110", R.drawable.t110);
		hm.put("t120", R.drawable.t120);
		hm.put("t130", R.drawable.t130);
		hm.put("t140", R.drawable.t140);
		hm.put("t150", R.drawable.t150);
		hm.put("t160", R.drawable.t160);
		hm.put("t170", R.drawable.t170);
		hm.put("t210", R.drawable.t210);
		hm.put("t220", R.drawable.t220);
		hm.put("t230", R.drawable.t230);
		hm.put("t240", R.drawable.t240);
		hm.put("t250", R.drawable.t250);
		hm.put("t260", R.drawable.t260);
		hm.put("t270", R.drawable.t270);
		hm.put("t310", R.drawable.t310);
		hm.put("t320", R.drawable.t320);
		hm.put("t330", R.drawable.t330);
		hm.put("t340", R.drawable.t340);
		hm.put("t350", R.drawable.t350);
		hm.put("t360", R.drawable.t360);
		hm.put("t370", R.drawable.t370);
		hm.put("t410", R.drawable.t410);
		hm.put("t420", R.drawable.t420);
		hm.put("t430", R.drawable.t430);
		hm.put("t440", R.drawable.t440);
		hm.put("t450", R.drawable.t450);
		hm.put("t460", R.drawable.t460);
		hm.put("t470", R.drawable.t470);
		hm.put("t510", R.drawable.t510);
		hm.put("t520", R.drawable.t520);
		hm.put("t530", R.drawable.t530);
		hm.put("t540", R.drawable.t540);
		hm.put("t550", R.drawable.t550);
		hm.put("t560", R.drawable.t560);
		hm.put("t570", R.drawable.t570);
		
		BuildingMap bm = new BuildingMap();
		
		String tile = null;
		
		tile =  bm.tileCalForLatLong(latitude, longitude);
		
		ImageView img = (ImageView) findViewById(R.id.full_tile) ;
		
		int Tile = (Integer)hm.get(tile);
	    img.setImageResource(Tile);
	}
}