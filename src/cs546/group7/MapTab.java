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

// Android graphics support
import android.graphics.BitmapFactory ;
import android.graphics.Bitmap ;

// Android application and OS support
import android.app.Activity ;
import android.os.Bundle ;

// Android utilities
import android.util.Log ;

//-------------------- DISPLAY SCREEN PICTURE TAB -----------------------

/**
   This class implements the map tab of the turn-by-turn walking
   directions application for the USC campus. It shows the correct tile
   of the campus map corresponding to the current location.
*/
public class MapTab extends Activity {

//-------------------------- INITIALIZATION -----------------------------

/// This method is called when the activity is first created. It is akin
/// to the "main" function in normal desktop applications.
@Override protected void onCreate(Bundle saved_state)
{
   super.onCreate(saved_state) ;
   setContentView(R.layout.map_tab) ;
}

//-------------------------- PICTURE DISPLAY ----------------------------

private void display_map(long id)
{
}

//-----------------------------------------------------------------------

} // end of class cs546.group7.MapTab