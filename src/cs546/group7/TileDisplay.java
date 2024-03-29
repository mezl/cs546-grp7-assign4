/*
   *******************************************************************
   *                                                                 *
   *    CSCI-546, Spring 2009 Assignment IV Solution by Group #7     *
   *                                                                 *
   *    Group Members: Chang, Chin-Kai      chinkaic@usc.edu         *
   *                   Moon, Ji Hyun        jihyunmo@usc.edu         *
   *                   Patlolla, Avinash    patlolla@usc.edu         *
   *                   Viswanathan, Manu    mviswana@usc.edu         *
   *                                                                 *
   *******************************************************************
   *                                                                 *
   * This file defines a class for displaying the appropriate tiles  *
   * of the USC campus map.                                          *
   *                                                                 *
   *******************************************************************
*/

/*
   AssignmentFour -- a photo manager application for the Google Phone

   Copyright (C) 2009 Chin-Kai Chang
                      Ji Hyun Moon
                      Avinash Patlolla
                      Manu Viswanathan

   This file is part of AssignmentFour.

   AssignmentFour is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License as
   published by the Free Software Foundation; either version 2 of the
   License, or (at your option) any later version.

   AssignmentFour is distributed in the hope that it will be useful, but
   WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
   General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with AssignmentFour; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
   USA.
*/

/*
  REVISION HISTORY

  $HeadURL$
  $Id$
*/

//----------------------- PACKAGE SPECIFICATION -------------------------

package cs546.group7 ;

//------------------------------ IMPORTS --------------------------------

// Android UI support
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.util.Log;
import android.widget.ImageView;

//------------------------- CLASS DEFINITION ----------------------------

/**
   This class displays the tiles of USC's campus map corresponding to the
   current GPS coordinates.
*/
class TileDisplay implements GPSRecorder.RefreshCB {

//public static String dest = null;
/// The tiles are all drawable resources and are referenced using
/// integers defined in R.java. However, our program's database maintains
/// the names of the individual tile files. Thus, we need to map the file
/// names to the resource IDs.
private static HashMap m_tiles_resources_map ;

/// Since map tiles are stored as application resources, we need a
/// suitable Android context to access them.
private Context m_context ;

/// In order to display the tile, this object will need a reference to
/// the main UI element that will show the bitmaps.
private ImageView m_tile_view ;

//-------------------------- INITIALIZATION -----------------------------

/// On instantiation, this object needs to be passed a reference to the
/// custom database that maps building codes to GPS coordinates and tile
/// names. Additionally, it needs to know which UI element to use for
/// displaying the campus map tiles.
public TileDisplay(Context C, ImageView V)
{
   if (m_tiles_resources_map == null)
     setup_tiles_resources_map() ;

   m_context = C ;
   m_tile_view = V ;

   GPSRecorder.instance().addCallback(this) ;
}

//---------------------------- TILE UPDATE ------------------------------

public void gpsUpdated(Location L)
{
   update(L.getLatitude(), L.getLongitude()) ;
}

/*public void update(double lat, double lon) {
	String tile = null;
	BuildingMap buildm = new BuildingMap();
	tile = buildm.tileCalForLatLong(lat, lon);
	int Tile = (Integer) m_tiles_resources_map.get(tile);
	m_tile_view.setImageResource(Tile);
}*/
///*
public void update(double latitude, double longitude)
{
	String tile = null;
	Bitmap bm_to_display = null;
  	Bitmap bmtemp = null;
  	
  	Coordinates coords = new Coordinates();
  	
	BuildingMap buildm = new BuildingMap();
	tile = buildm.tileCalForLatLong(latitude, longitude);
	Log.e(null, " tile is :" + tile);
	coords = buildm.coordCalForLatLong(latitude, longitude);
	
    int tile_num_X = 0, tile_num_Y = 0;
    try {
      int Tile = (Integer) m_tiles_resources_map.get(tile);
      Log.e(null, " Tile is " + Tile);
      Bitmap bitmap = BitmapFactory.decodeStream(m_context.getResources().openRawResource(Tile));
  	  Bitmap bm = Bitmap.createBitmap(bitmap);
  	  //Toast.makeText(GPSRecorder.this, "tile is :" + tile,Toast.LENGTH_LONG).show();
  	  //Toast.makeText(GPSRecorder.this, "X-coord: " + coords.X_coord + " Y-coord: " + coords.Y_coord,Toast.LENGTH_LONG).show();
  	  if(coords.X_coord > 128 && coords.Y_coord < 256) {
  		  if(coords.Y_coord > 0 && coords.Y_coord < 129) {
  			 // coordinate = 1;
  			tile_num_X = Integer.parseInt(tile.substring(1,2));
  			tile_num_Y = Integer.parseInt(tile.substring(2,3));
  			tile_num_Y += 1;
  			coords.Y_coord += bm.getHeight();
  		  }
  		  else if(coords.Y_coord > 128 && coords.Y_coord < 256) {
  			//  coordinate = 4;
  			tile_num_X = Integer.parseInt(tile.substring(1,2));
  			tile_num_Y = Integer.parseInt(tile.substring(2,3));
  			tile_num_X += 1;
  			tile_num_Y += 1;
  		  }
  	  }
  	  else if(coords.X_coord > 0 && coords.X_coord < 129) {
  		if(coords.Y_coord > 0 && coords.Y_coord < 129) {
			//  coordinate = 2;
			tile_num_X = Integer.parseInt(tile.substring(1,2));
	  		tile_num_Y = Integer.parseInt(tile.substring(2,3));
	  		coords.X_coord += bm.getWidth();
  			coords.Y_coord += bm.getHeight();
		  }
		  else if(coords.Y_coord > 128 && coords.Y_coord < 256) {
			//  coordinate = 3;
			tile_num_X = Integer.parseInt(tile.substring(1,2));
	  		tile_num_Y = Integer.parseInt(tile.substring(2,3));
	  		tile_num_X += 1;
	  		coords.X_coord += bm.getWidth();
		  }
  	  }
  	  int w = bm.getWidth();
      int h = bm.getHeight();
      bm_to_display = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
      bmtemp = Bitmap.createBitmap(w * 2, h * 2, Bitmap.Config.RGB_565);
      int[] pix = new int[(w * 2)  * (h * 2)];
	  
	  //Toast.makeText(GPSRecorder.this, "tile_num_X = " + tile_num_X + "tile_num_Y = " + tile_num_Y,Toast.LENGTH_LONG).show();
	  
	  int X_temp = tile_num_X - 1, Y_temp, k = 0, l = 0;
      for(int i = 0; i < 2; i ++) {
    	  Y_temp = tile_num_Y - 1;
    	  for(int j = 0; j < 2; j ++) {
    		  String tile_temp = null;
    		  int Tile_temp;
    		  if(X_temp < 1 || Y_temp < 1) {
    			  Tile_temp = R.drawable.no_data;
    		  }
    		  else {
    			  tile_temp = "t" + X_temp + Y_temp + 0;
    			 // Toast.makeText(GPSRecorder.this, "tile calculated as " + tile_temp, Toast.LENGTH_SHORT).show();
    			  Tile_temp = (Integer) m_tiles_resources_map.get(tile_temp); 
    		  }

    		  Bitmap bitmap_temp = BitmapFactory.decodeStream(m_context.getResources().openRawResource(Tile_temp));
    		  Bitmap bm_temp = Bitmap.createBitmap(bitmap_temp);
    		  bm_temp.getPixels(pix, 0, w, 0, 0, w, h);
    		  
    		  k = i * 256;
    		  for(int m = 0, k_temp = 0; k_temp < bm_temp.getWidth(); k++, k_temp++) {
    			  l = j * 256;
    			  for(int l_temp = 0; l_temp < bm_temp.getHeight(); l++, l_temp++) {
    				  bmtemp.setPixel(l, k, pix[m]);
    				  m++;
    			  }
    		}
    		Y_temp++;
    	  }
    	  X_temp++;
      }
  		
  		h = bmtemp.getHeight();
  		w = bmtemp.getWidth();
  		int[] pixels = new int[w * h];
  		bmtemp.getPixels(pixels, 0, w, 0, 0, w, h);
  		
  		int m = (coords.Y_coord - (bmtemp.getHeight() / 4)) * bmtemp.getWidth();
  	  	m += coords.X_coord - (bmtemp.getWidth() / 4);
  	  	//Toast.makeText(MapTab.this, "m1 = " + m, Toast.LENGTH_SHORT).show();
  	  	//Utils.notify(m_context, "m1 = " + m);

  		for(k = 0; k < bm_to_display.getHeight(); k++) {
  			for(l = 0; l < bm_to_display.getWidth(); l++) {
  				bm_to_display.setPixel(l, k, pixels[m]);
  				m++;
  			}
  			m += ((bmtemp.getWidth() - coords.X_coord - (bmtemp.getWidth() / 4)) + (coords.X_coord - (bmtemp.getWidth() / 4)));
  		}
    }
    catch (Exception e) {
    	Log.e(null, "This is the error : ",  e);
    }
    
    //Log.e(null, " Here it is to display the map");
    
    Canvas c = new Canvas(bm_to_display);
    Paint pt1 = new Paint();
    Paint pt2 = new Paint();
    
    pt1.setARGB(255, 255, 0, 0);
    pt2.setARGB(255, 255, 0, 0);
    
    pt1.setStyle(Paint.Style.STROKE);
    pt2.setStyle(Paint.Style.FILL_AND_STROKE);
    
    c.drawCircle(128, 128, 5, pt1);
    c.drawCircle(128, 128, 3, pt2);
    
   /* if(dest != null) {
    	Coordinates dest_coords = new Coordinates();
    	dest_coords = buildm.coordCalForCode(dest);
    	if(dest_coords.X_coord != 999 && dest_coords.Y_coord != 999) {
    		String dest_tile;
    		dest_tile = buildm.tileCalForCode(dest);
    		if(dest_tile != null) {
    			int dest_X_coord = ((int)(dest_tile.charAt(1)) * 256) + dest_coords.X_coord;
    			int dest_Y_coord = ((int)(dest_tile.charAt(2)) * 256) + dest_coords.Y_coord;
    			
    			Coordinates src_coords = new Coordinates();
    			src_coords = buildm.coordCalForCode(tile);
    			
    			int src_X_coord = ((int)(tile.charAt(1)) * 256) + src_coords.X_coord;
    			int src_Y_coord = ((int)(tile.charAt(2)) * 256) + src_coords.Y_coord;
    			
    			Log.e(null, "dest tile is : " + dest_tile.charAt(1) + dest_tile.charAt(2) + " src tile is " + tile.charAt(1) + tile.charAt(2));
    			while(Math.abs(src_X_coord - dest_X_coord) > 25) {
    				dest_X_coord = (src_X_coord + dest_X_coord) / 2;
    				dest_Y_coord = (src_Y_coord + dest_Y_coord) / 2;
    			}
    			
    			Paint pt3 = new Paint();
    			pt3.setARGB(255, 0, 0, 205);
    			pt3.setStyle(Paint.Style.FILL);
    			c.drawLine(src_X_coord, src_Y_coord, dest_X_coord, dest_Y_coord, pt3);
    			c.drawLine(dest_X_coord - 2, dest_Y_coord, dest_X_coord, dest_Y_coord, pt2);
    			c.drawLine(dest_X_coord, dest_Y_coord + 2, dest_X_coord, dest_X_coord, pt2);
    		}
    	}
    }*/
     
    m_tile_view.setImageBitmap(bm_to_display);
}
//*/

//------------------------------ HELPERS --------------------------------

private void setup_tiles_resources_map()
{
   m_tiles_resources_map = new HashMap(35) ;
   m_tiles_resources_map.put("t110", R.drawable.t110) ;
   m_tiles_resources_map.put("t120", R.drawable.t120) ;
   m_tiles_resources_map.put("t130", R.drawable.t130) ;
   m_tiles_resources_map.put("t140", R.drawable.t140) ;
   m_tiles_resources_map.put("t150", R.drawable.t150) ;
   m_tiles_resources_map.put("t160", R.drawable.t160) ;
   m_tiles_resources_map.put("t170", R.drawable.t170) ;
   m_tiles_resources_map.put("t210", R.drawable.t210) ;
   m_tiles_resources_map.put("t220", R.drawable.t220) ;
   m_tiles_resources_map.put("t230", R.drawable.t230) ;
   m_tiles_resources_map.put("t240", R.drawable.t240) ;
   m_tiles_resources_map.put("t250", R.drawable.t250) ;
   m_tiles_resources_map.put("t260", R.drawable.t260) ;
   m_tiles_resources_map.put("t270", R.drawable.t270) ;
   m_tiles_resources_map.put("t310", R.drawable.t310) ;
   m_tiles_resources_map.put("t320", R.drawable.t320) ;
   m_tiles_resources_map.put("t330", R.drawable.t330) ;
   m_tiles_resources_map.put("t340", R.drawable.t340) ;
   m_tiles_resources_map.put("t350", R.drawable.t350) ;
   m_tiles_resources_map.put("t360", R.drawable.t360) ;
   m_tiles_resources_map.put("t370", R.drawable.t370) ;
   m_tiles_resources_map.put("t410", R.drawable.t410) ;
   m_tiles_resources_map.put("t420", R.drawable.t420) ;
   m_tiles_resources_map.put("t430", R.drawable.t430) ;
   m_tiles_resources_map.put("t440", R.drawable.t440) ;
   m_tiles_resources_map.put("t450", R.drawable.t450) ;
   m_tiles_resources_map.put("t460", R.drawable.t460) ;
   m_tiles_resources_map.put("t470", R.drawable.t470) ;
   m_tiles_resources_map.put("t510", R.drawable.t510) ;
   m_tiles_resources_map.put("t520", R.drawable.t520) ;
   m_tiles_resources_map.put("t530", R.drawable.t530) ;
   m_tiles_resources_map.put("t540", R.drawable.t540) ;
   m_tiles_resources_map.put("t550", R.drawable.t550) ;
   m_tiles_resources_map.put("t560", R.drawable.t560) ;
   m_tiles_resources_map.put("t570", R.drawable.t570) ;
}

//-----------------------------------------------------------------------

} // end of class cs546.group7.TileDisplay
