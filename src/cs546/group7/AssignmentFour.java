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
   * This file contains the main code for a turn-by-turn walking     *
   * directions application. This application is meant to be used on *
   * the USC University Park Campus.
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
import android.widget.TabHost ;

// Android application and OS support
import android.app.TabActivity ;
import android.content.Intent ;
import android.os.Bundle ;

//--------------------- APPLICATION'S MAIN SCREEN -----------------------

/**
   This class implements the main screen of the turn-by-turn walking
   directions application. The main screen consists of two tabs: one for
   showing the turn-by-turn directions and the other for showing the map
   tile corresponding to the current location.
*/
public class AssignmentFour extends TabActivity {

//-------------------------- INITIALIZATION -----------------------------

/// This method is called when the activity is first created. It is akin
/// to the "main" function in normal desktop applications.
@Override protected void onCreate(Bundle saved_state)
{
   super.onCreate(saved_state) ;

   // Setup the GPS listener
   GPSRecorder.create(this) ;

   // Create the walking directions and map display tabs
   final TabHost H = getTabHost() ;
   setup_directions_tab(H) ;
   setup_map_tab(H) ;
}

private void setup_directions_tab(final TabHost H)
{
   TabHost.TabSpec T = H.newTabSpec("directions_tab") ;
   T.setIndicator(getString(R.string.directions_tab_label),
                  getResources().getDrawable(R.drawable.directions_tab_icon));
   T.setContent(new Intent(this, DirectionsTab.class)) ;
   H.addTab(T) ;
}

private void setup_map_tab(final TabHost H)
{
   TabHost.TabSpec T = H.newTabSpec("map_tab") ;
   T.setIndicator(getString(R.string.map_tab_label),
                  getResources().getDrawable(R.drawable.map_tab_icon)) ;
   T.setContent(new Intent(this, MapTab.class)) ;
   H.addTab(T) ;
}

//----------------------------- CLEAN-UP --------------------------------

/// On final application close, we have to shutdown the GPS listener that
/// we created during application start-up.
@Override protected void onDestroy()
{
   GPSRecorder.instance().shutdown() ;
   super.onDestroy() ;
}

//-----------------------------------------------------------------------

} // end of class cs546.group7.AssignmentFour
