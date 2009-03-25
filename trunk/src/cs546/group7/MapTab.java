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
   * This file contains the code for the map tab of a turn-by-turn   *
   * walking directions application. This application is meant to be *
   * used on the USC University Park Campus.                         *
   *                                                                 *
   *******************************************************************
*/

/*
   AssignmentFour -- turn-by-turn walking directions for USC campus on gPhone

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

// Android application and OS support
import android.app.Activity ;
import android.os.Bundle ;

// Android utilities
import android.util.Log ;

// Java containers
import java.util.HashMap;

//-------------------- DISPLAY SCREEN PICTURE TAB -----------------------

/**
   This class implements the map tab of the turn-by-turn walking
   directions application for the USC campus. It shows the correct tile
   of the campus map corresponding to the current location.
*/
public class MapTab extends Activity {

/// The map tab simply uses the tile display to do its thing.
private TileDisplay m_tile_display ;

//-------------------------- INITIALIZATION -----------------------------

/// This method is called when the activity is first created. It is akin
/// to the "main" function in normal desktop applications.
@Override protected void onCreate(Bundle saved_state)
{
   super.onCreate(saved_state) ;
   setContentView(R.layout.tile_display);//map_tab) ;

   m_tile_display =
      new TileDisplay(this, (ImageView) findViewById(R.id.full_tile));//map_tile)) ;
   GPSRecorder r = GPSRecorder.instance();
   m_tile_display.update(r.latitude(), r.longitude());
}

//-----------------------------------------------------------------------

} // end of class cs546.group7.MapTab
