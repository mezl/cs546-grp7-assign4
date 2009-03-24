package csci561_hw1_search;
/**

   (C) Copyright 2008, by Chin-Kai Chang
 
   This file is part of Search.

   Search is free software; you can redistribute it and/or modify it
   under the terms of the GNU General Public License as published by the
   Free Software Foundation; either version 2 of the License, or (at your
   option) any later version.

   Search is distributed in the hope that it will be useful, but WITHOUT
   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
   for more details.

   You should have received a copy of the GNU General Public License
   along with search; if not, write to the Free Software Foundation,
   Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA. 

 *    @file: Registry.java
 *  @author: Chin-Kai Chang <chinkaic@usc.edu> 
 * @Purpose: Defines the search algorithm registry

 */
public class Registry {

    public static final String SEARCH_BREADTH_FIRST = "BFS";
    public static final String SEARCH_DEPTH_FIRST = "DFS";
    public static final String SEARCH_A_STAR = "A*";
    public static final String SEARCH_GRDEEY = "GRDEEY";
    public static final String SEARCH_UNI_COST = "UNI_COST";

    public static boolean isHeuristic(String algo) {
        if (algo.equals(SEARCH_A_STAR) || algo.equals(SEARCH_GRDEEY)) {
            return true;
        } else {
            return false;
        }
    }
}
