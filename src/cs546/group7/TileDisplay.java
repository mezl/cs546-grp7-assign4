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
import android.widget.ImageView ;

// Android GPS support
import android.location.Location ;

// Android application and OS support
import android.content.Context ;

// Java utilities
import java.util.HashMap ;

//------------------------- CLASS DEFINITION ----------------------------

/**
   This class displays the tiles of USC's campus map corresponding to the
   current GPS coordinates.
*/
class TileDisplay implements GPSRecorder.RefreshCB {

/// The tiles are all drawable resources and are referenced using
/// integers defined in R.java. However, our program's database maintains
/// the names of the individual tile files. Thus, we need to map the file
/// names to the resource IDs.
private static HashMap m_tiles_resources_map ;

/// In order to figure out which tile to display, this object needs a
/// connection to the application's custom database that maps building
/// codes to GPS coordinates and tile names.
private BuildingMap m_building_db ;

/// In order to display the tile, this object will need a reference to
/// the main UI element that will show the bitmaps.
private ImageView m_tile_view ;

//-------------------------- INITIALIZATION -----------------------------

/// On instantiation, this object needs to be passed a reference to the
/// custom database that maps building codes to GPS coordinates and tile
/// names. Additionally, it needs to know which UI element to use for
/// displaying the campus map tiles.
public TileDisplay(Context C, BuildingMap B, ImageView V)
{
   if (m_tiles_resources_map == null)
      setup_tiles_resources_map() ;

   m_building_db = B ;
   m_tile_view = V ;

   GPSRecorder.instance().addCallback(this) ;
}

//---------------------------- TILE UPDATE ------------------------------

public void update(double latitude, double longitude)
{
   String tile_name = m_building_db.tileCalForLatLong(latitude, longitude) ;
   int tile_id = (Integer) m_tiles_resources_map.get(tile_name) ;
   m_tile_view.setImageResource(tile_id) ;
}

public void gpsUpdated(Location L)
{
   update(L.getLatitude(), L.getLongitude()) ;
}

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
