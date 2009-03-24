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

// Custom package for Dijkstra related stuff
import csci561_hw1_search.Problems;
import csci561_hw1_search.Registry;

// Android UI support
import android.widget.ArrayAdapter;
import android.widget.ImageView ;
import android.widget.TextView ;

// Android application and OS support
import android.app.Activity ;
import android.os.Bundle ;

// Java I/O
import java.io.File;

//--------------------- APPLICATION'S MAIN SCREEN -----------------------

/**
   This class implements the main screen of the turn-by-turn walking
   directions application. The main screen consists of a "status" bar on
   top that shows the current location and a search textbox at the bottom
   for users to type in destination building codes. In the middle is a
   multitab area with two tabs: one for the walking directions and
   another displaying the campus map.
*/
public class AssignmentFour extends Activity {

//-------------------------- INITIALIZATION -----------------------------

/// This method is called when the activity is first created. It is akin
/// to the "main" function in normal desktop applications.
@Override public void onCreate(Bundle saved_state)
{
   super.onCreate(saved_state) ;
   setContentView(R.layout.main) ;

   // Setup the GPS listener
   GPSRecorder G = GPSRecorder.create(this) ;
   G.use_ui((TextView) findViewById(R.id.lat_view),
            (TextView) findViewById(R.id.lon_view)) ;

   // Create the app database(s) and display objects
   BuildingMap db = new BuildingMap() ;
   TileDisplay td =
      new TileDisplay(this, db, (ImageView) findViewById(R.id.map_tile)) ;
}

/// Called when the activity is resumed. In our case, we reconnect to the
/// audio tags database.
@Override protected void onResume()
{
   super.onResume() ;
}

//----------------------------- CLEAN-UP --------------------------------

/// Called when the activity ends. In our app, we should close the
/// connection to the database.
@Override protected void onPause()
{
   super.onPause() ;
}

/// On final application close, we have to shutdown the GPS listener that
/// we created during application start-up.
@Override protected void onDestroy()
{
   GPSRecorder.instance(this).shutdown() ;
   super.onDestroy() ;
}

//-----------------------------------------------------------------------

private Problems p;
private void show_direction(String[] direction)
{  
  // direction_list.setAdapter(
  //    new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,direction));
}
public void init_map(String map) {
	p = new Problems(fullname(map));// "data/data/android.four/files/map.txt"
}
public void search(String from, String to) {
	p.setInitial_state(from);
	p.setGoal_state(to);
	p.Search(Registry.SEARCH_UNI_COST, null);
	show_direction(p.getDirection());
}

// / Quick helper to extract just the file name from a given path name (in
// / case the user entered a full path, which Android does not allow).
private String basename(String path_name) {
	return new File(path_name).getName();
}

// / Although Android does not allow full path names, we still need those
// / to check for file existence, etc.
private String fullname(String file_name) {
	return getFilesDir().getPath() + File.separator + basename(file_name);
}


} // end of class cs546.group7.AssignmentFour
