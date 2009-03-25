package csci561_hw1_search;

/**

(C) Copyright 2008, by Chin-Kai Chang

This file is part of Node.

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

 *    @file: Node.java
 *  @author: Chin-Kai Chang <chinkaic@usc.edu> 
 * @Purpose: A node structure

 */
import org.jgrapht.*;
import org.jgrapht.graph.*;
import java.util.*;

public class Node {

    private String cityName;
    private Node city_parent;
    private String city_parent_road;
    private String city_parent_direction;
    private Node[] city_children;
    private int city_numChildren;
    private int city_number;
    private int city_pathCost;
    private int city_nodeCost;
    private int city_hCost;
    private UndirectedGraph<String, RoadEdge> map;
    private Hashtable heuristic;

    public Node(String p, UndirectedGraph<String, RoadEdge> g, Hashtable h) {
        if(h != null){
            heuristic = h;
            city_hCost = (Integer) (heuristic.get(cityName));
        }else{
            heuristic = null;
            city_hCost = 0;
        }              
        cityName = p;
        //System.out.println("Node "+ cityName);
        
        //System.out.println("Node again "+ cityName);
        city_numChildren = 0;
        city_pathCost = 0;
        city_children = new Node[100];//Max 10 neighbor city
        map = g;
        city_nodeCost = city_pathCost + city_hCost;
        //System.out.println("Node again "+ cityName);
    }

    public String getCity_parent_direction() {
        return city_parent_direction;
    }

    public void setCity_parent_direction(String city_parent_direction) {
        this.city_parent_direction = city_parent_direction;
    }

    public String getCity_parent_road() {
        return city_parent_road;
    }

    public void setCity_parent_road(String city_parent_road) {
        this.city_parent_road = city_parent_road;
    }
    public void expand(String algo) {
    	try{
        Set<RoadEdge> cityset = map.edgesOf(cityName);
        Iterator<RoadEdge> cityIter = cityset.iterator();
        //System.out.println("NodeName is"+cityName+" childs="+city_numChildren);
        while (cityIter.hasNext()) {
            RoadEdge neighborEdge = cityIter.next();
            String neighborCity = org.jgrapht.Graphs.getOppositeVertex(map, neighborEdge, cityName);
            
            //System.out.println("NodeName is "+cityName+" childs="+city_numChildren+" has neiborCity " +  neighborCity);
            //String neighborCity2 = map.getEdgeSource(neighborEdge);
            //String neighborCity = map.getEdgeTarget(neighborEdge);
            //System.out.println("Node"+cityName+" has neighborT="+neighborCity);
            //String city = new String(cities.number);

            // if(!visitedNodes.contains(neighborCity)){
            Node child = new Node(neighborCity, map, heuristic);

            child.city_parent = this;
            child.city_pathCost = (int) map.getEdgeWeight(neighborEdge) + city_pathCost;
            child.city_parent_road = neighborEdge.getRoadName();
            child.city_parent_direction = neighborEdge.getDirection(child.cityName);
            // Using BFS 
            if (algo.equals(Registry.SEARCH_BREADTH_FIRST)) {
                child.city_nodeCost = city_nodeCost + 1;//BFS node depth
            } else if (algo.equals(Registry.SEARCH_DEPTH_FIRST)) {
                child.city_nodeCost = city_numChildren;//DFS the order of the same level node
            } else if (algo.equals(Registry.SEARCH_A_STAR)) {
                child.city_nodeCost = child.city_pathCost + child.city_hCost;// Using A* search				
            } else if (algo.equals(Registry.SEARCH_GRDEEY)) {
                child.city_nodeCost = child.city_hCost;// Using Greedy Search
            } else if (algo.equals(Registry.SEARCH_UNI_COST)) {
                child.city_nodeCost = child.city_pathCost;//Using uniform-cost search
            } else {
                child.city_nodeCost = child.city_pathCost + child.city_hCost;// Default Using A* search				
            }
            city_children[city_numChildren++] = child;
        }
    	}catch(Exception e){
    		System.err.print("Can't find location");    		
    	}
    }

    public String getCityName() {
        return cityName;
    }

    public Node getParent() {
        return city_parent;
    }

    public int getNumChildren() {
        return city_numChildren;
    }

    public Node getChild(int i) {
        return city_children[i];
    }

    public int getCityNumber() {
        return city_number;
    }

    public int getCost() {
        //System.out.println("In Child Cost = "+city_nodeCost);
        return city_nodeCost;
    }

    public int getNodeCost() {
        return city_nodeCost;
    }

    public int getPathCost() {
        return city_pathCost;
    }
/*
 For the un-inform searches such as BFS and DFS, the output will looks like
following form:

{(total-distance [path])(total-distance [path]) ... (total-distance [path])}

For Example, the first two line of breadth-first search is like:

    {(0 [A])}
    {(75 [A Z]) (118 [A T]) (140 [A S])}

The heuristic searches such as A* search, the output is of the form:

{(estimated-distance total-distance [path]) ... (estimated-distance total-distance)}

For Example, the first two line of breadth-first search is like:

    {(366 0 [A])}
    {(393 140 [A S]) (449 75 [A Z]) (447 118 [A T])}
 
 */
    public String toString(boolean heuristic) {
        if (heuristic) {
            return "(" + city_nodeCost + " " + city_pathCost + " [" + getAllParents() + "])";
        } else {
            return "(" + city_pathCost + " [" + getAllParents() + "])";
        }
    }
    String allParents = "";
    Vector<String>  ap = new Vector<String>();
    public String getAllParents() {  
        
        traceBack(this);        
        allParents = "";
        for(int i = 0; i< ap.size();i++)
            allParents=ap.elementAt(i)+" "+allParents;
        //return allParents.substring(0, allParents.length()-1);//Remove last space
        //System.out.println("All parents "+allParents);
        return allParents;//Remove last space
    }

    private void traceBack(Node n) {
        if(!ap.contains(n.getCityName()))
            ap.add(n.getCityName());        
        if (n.getParent() != null) {
            traceBack(n.getParent());
        }
    }
}
        
        

