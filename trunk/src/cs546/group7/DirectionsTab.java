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
   * This file contains the code for the directions tab of a         *
   * turn-by-turn walking directions application. This application   *
   * is meant to be used on the USC University Park Campus.          *
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
import csci561_hw1_search.Problems ;
import csci561_hw1_search.Registry ;

// Android UI support
import android.widget.ListView ;
import android.widget.ArrayAdapter ;
import android.widget.EditText ;
import android.widget.Button ;

import android.view.View ;

// Android application and OS support
import android.app.Activity ;
import android.location.Location;
import android.os.Bundle ;

// Android utilities
import android.util.Log ;

//-------------------- DISPLAY SCREEN PICTURE TAB -----------------------

/**
   This class implements the directions tab of the turn-by-turn walking
   directions application for the USC campus. It shows the results of the
   graph search, i.e., the walking directions, in a list view and an edit
   box + button where users can type in destination building codes.
*/
public class DirectionsTab extends Activity implements GPSRecorder.RefreshCB {

/// The walking directions are displayed on a list view
private ListView m_directions_view ;

/// We use a graph to get the walking directions from the current
/// location to some destination. This graph is represented as a problem
/// space.
private Problems m_directions_graph ;

//-------------------------- INITIALIZATION -----------------------------

/// This method is called when the activity is first created. It is akin
/// to the "main" function in normal desktop applications.
@Override protected void onCreate(Bundle saved_state)
{
   super.onCreate(saved_state) ;
   setContentView(R.layout.directions_tab) ;

   m_directions_view = (ListView) findViewById(R.id.directions) ;

   Button go_btn = (Button) findViewById(R.id.search_btn) ;
   go_btn.setOnClickListener(new View.OnClickListener() {
      public void onClick(View V) {
         EditText bldg_code = (EditText) findViewById(R.id.search_editbox) ;
         String dest = bldg_code.getText().toString() ;
         show_path_to(dest) ;
      }}) ;
   EditText info_box = (EditText) findViewById(R.id.search_infobox) ;
   info_box.setText("Reading GPS...Please Wait");
   // Read the directions graph
   m_directions_graph =
      new Problems(Utils.fullname(this, "code_dist_road_dir.txt")) ;
   GPSRecorder.instance().addCallback(this);
}
public void gpsUpdated(Location L) {
	EditText info_box = (EditText) findViewById(R.id.search_infobox) ;
	BuildingMap buildm = new BuildingMap();
	String tile = buildm.codeCalForLatLong(L.getLatitude(), L.getLongitude());
	info_box.setText("Currently near "+tile);
}
//------------------------ WALKING DIRECTIONS ---------------------------

private void show_path_to(String destination)
{
   if (destination.length() == 0) {
      Utils.alert(this, getString(R.string.no_destination)) ;
      return ;
   }

   destination = destination.toUpperCase() ;
   BuildingMap db = new BuildingMap() ;
   if (! db.codeCheckInDB(destination)) {
      Utils.alert(this,
                  destination + ": " + getString(R.string.no_such_building)) ;
      return ;
   }

  // TileDisplay.dest = destination;
   String src = get_source(db);
   Log.e(null, " src = " + src + " dest : " + destination);
   m_directions_view.setAdapter(new ArrayAdapter<String>(this,
      android.R.layout.simple_list_item_1,
      get_directions(get_source(db), destination))) ;
}

// This helper returns the 3-letter code of the building nearest to the
// phone's current location.
private String get_source(BuildingMap db)
{
   GPSRecorder R = GPSRecorder.instance() ;
   return db.codeCalForLatLong(R.latitude(), R.longitude()) ;
}

private String[] get_directions(String from, String to)
{
   m_directions_graph.setInitial_state(from);
   m_directions_graph.setGoal_state(to);
   m_directions_graph.Search(Registry.SEARCH_UNI_COST, null);
   return m_directions_graph.getDirection() ;
}

//-----------------------------------------------------------------------

} // end of class cs546.group7.DirectionsTab
