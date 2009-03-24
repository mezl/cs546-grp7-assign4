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
   * This file contains an assortment of utility functions used by   *
   * different parts of the walking directions application.          *
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
import android.widget.Toast ;
import android.app.AlertDialog ;

// Android application and OS support
import android.app.Activity ;
import android.content.Context ;

// Android utilities
import android.util.Log ;

// Java I/O support
import java.io.FileInputStream ;
import java.io.InputStream ;
import java.io.OutputStream ;
import java.io.File ;
import java.io.FilenameFilter ;

//------------------------- CLASS DEFINITION ----------------------------

/**
   This class provides several handy utility functions.
*/
class Utils {

//-------------------------- GPS COORDINATES ----------------------------

/// A simple pair to hold latitude and longitude
public static final class LatLong {
   public double latitude ;
   public double longitude ;

   public LatLong(double latitude, double longitude) {
      this.latitude  = latitude ;
      this.longitude = longitude ;
   }
}

//------------------------- UI NOTIFICATIONS ----------------------------

/// A short notification message that doesn't steal focus or require any
/// specific interaction on the user's part to dismiss. It simply appears
/// briefly and fades away.
public final static void notify(Context C, String msg)
{
   Toast.makeText(C, msg, Toast.LENGTH_SHORT).show() ;
}

/// Long notification message
public final static void notify_long(Context C, String msg)
{
   Toast.makeText(C, msg, Toast.LENGTH_LONG).show() ;
}

/// Show an error box
public final static void alert(Context C, String msg)
{
   AlertDialog.Builder alert = new AlertDialog.Builder(C) ;
   alert.setMessage(msg) ;
   alert.setPositiveButton(R.string.alert_okay_label, null) ;
   alert.show() ;
}

//----------------------- FILE SYSTEM FUNCTIONS -------------------------

/// This function returns true if the specified file exists, is readable
/// and actually has some data in it; false otherwise.
public final static boolean exists(String file_name)
{
   File f = new File(file_name) ;
   return f.exists() && f.canRead() && f.length() > 0 ;
}

/// Quick helper to extract just the file name from a given path name (in
/// case the user entered a full path, which Android does not allow).
public final static String basename(String path_name)
{
   return new File(path_name).getName();
}

/// Although Android does not allow full path names, we still need those
/// to check for file existence, etc.
public final static String fullname(Activity A, String file_name)
{
   return A.getFilesDir().getPath() + File.separator + basename(file_name);
}

/// This function removes the specified file
public final static void unlink(String file_name)
{
   new File(file_name).delete() ;
}

/// Remove all files matching specified pattern
public final static void unlink_all(final String glob)
{
   final int last_sep = glob.lastIndexOf(File.separatorChar) ;

   File dir = new File(glob.substring(0, last_sep)) ;
   String[] list = dir.list(new FilenameFilter() {
         private String pattern = new String(glob.substring(last_sep + 1));
         public  boolean accept(File dir, String file_name) {
            return file_name.matches(pattern) ;
         }
      }) ;

   for (int i = 0; i < list.length; ++i)
      unlink(dir.getPath() + File.separator + list[i]) ;
}

/// Copy the named file byte-by-byte to the supplied output stream
public final static
void copy(String file_name, OutputStream out) throws Exception
{
   InputStream in = new FileInputStream(file_name) ;
   byte[] buf = new byte[512] ;
   int len ;
   while ((len = in.read(buf)) > 0)
      out.write(buf, 0, len) ;
   in.close() ;
}

//-----------------------------------------------------------------------

} // end of class cs546.group7.Utils
