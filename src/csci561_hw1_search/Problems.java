package csci561_hw1_search;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jgrapht.graph.SimpleWeightedGraph;

import android.util.Log;

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

 *    @file: Problems.java
 *  @author: Chin-Kai Chang <chinkaic@usc.edu> 
 * @Purpose: Defines the search problem

 */
/*
 * Here we are using graph library named JGraphT.
 * To have more information about JGraphT,
 * please visited http://jgrapht.sourceforge.net/
 */

public class Problems {

    private String initial_state;
    private String goal_state;
    private String map;
    private SimpleWeightedGraph<String, RoadEdge> g;  //
    private Hashtable heuristic;
    private ArrayList<String> direction;
    public String solution;

    public Problems(String map_file_name) {
    	Log.v(getClass().getSimpleName(), "Create Problem with map="+map_file_name);
        map = map_file_name;
        g = new SimpleWeightedGraph<String, RoadEdge>(RoadEdge.class);
        //heuristic = new Hashtable();
        heuristic = null;
        solution = "";
        createMap(map);
        direction =new ArrayList<String>();
        //System.out.println("Map created");
    }

    public void Search(String algorithm, String heuristic_file_name) {
        //System.out.println("Search start");
        if (heuristic != null && heuristic.isEmpty()) {
            System.out.println("heuristic table created");
            createHeuristicTable(heuristic_file_name);            
        }
        
        //System.out.println("================= Using " + algorithm + " Search =================");

        Node n = search_proxy(algorithm, initial_state, goal_state);
        solution = "";
        if (n != null) {
            findSolution(n);
        } else {
            solution = " No Solution ";
        }
        System.out.println("Final solution:[\n" + solution.substring(0, solution.length() - 1) + "\n]");
        
        
    }

    public String [] getDirection() {
    	String[] dirs = new String[direction.size()];
    	reverseList(direction).toArray(dirs);
		return dirs;
	}

	public void setDirection(ArrayList<String> direction) {
		this.direction = direction;
	}

	public Node search_proxy(String algo, String ini, String goal) {

        PriorityQueue queue = new PriorityQueue();
        Node root = new Node(ini, g, heuristic);// with heuristic data
        //Node root = new Node(ini, g, null);// without heuristic data
        queue.addElement(root);
        Vector<String> visitedNodes = new Vector<String>();
        while (queue.hasMoreElements()) {

            //System.out.println(queue.toString(Registry.isHeuristic(algo))); //output result
            Node node = queue.removeElement();
            visitedNodes.add(node.getCityName());
            //System.out.println("Search city="+node.getCityName());

            if (node.getCityName().equals(goal)) {
                return node;//Found the target
            }
            node.expand(algo);
            for (int i = 0; i < node.getNumChildren(); ++i) {
                Node child = node.getChild(i);
                //Prevent loop search
                if (!visitedNodes.contains(child.getCityName())) {
                    //System.out.print("("+child.getNodeCost()+" "+child.getPathCost()+
                    //"["+node.getCityName()+" "+child.getCityName()+"])");
                    queue.addElement(child);
                }
            //System.out.println();
            }
        }//end while
        return null;
    }

    private void findSolution(Node node) {
        if (node.getParent() == null) {
        	String start = "Start From "+node.getCityName();
            solution = start + " \n" + solution;
            direction.add(start);
        }else{
        	String turn = "Turn "+node.getCity_parent_direction()+". on " + node.getCity_parent_road()+" to "+node.getCityName();
            solution = turn + " \n" + solution;
            direction.add(turn);
            
        }
        if (node.getParent() != null) {
            findSolution(node.getParent());
        }
    }
    private ArrayList<String> reverseList(ArrayList<String> dir){
    	ArrayList<String> redir = new ArrayList<String>();
    	for(int i=1;i<=dir.size();i++){
    		redir.add(dir.get(dir.size()-i));
    		System.out.println(dir.get(dir.size()-i));
    	}
    	return redir;    	
    }
    public String getGoal_state() {
        return goal_state;
    }

    public String getInitial_state() {
        return initial_state;
    }

    public void setGoal_state(String goal_state) {
        this.goal_state = goal_state;
    }

    public void setInitial_state(String initial_state) {
        this.initial_state = initial_state;
    }
    public void saveMap(String map_file_name){
    	
		try {
			//FileWriter fw = new FileWriter(map_file_name);
			//FileInputStream android.content.ContextWrapper.openFileInput
			FileOutputStream fOut = new FileOutputStream(map_file_name);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write("");
			osw.flush();
			osw.close();
		} catch (IOException e) {
			// debug processing can be placed here
		}
    }
    public void createMap(String map_file_name) {
        RoadEdge edge = new RoadEdge();
        String thisLine;
        try {
        	//FileInputStream fIn = openFileInput(map_file_name);
        	//FileInputStream android.content.ContextWrapper.openFileInput fIn = 
            FileReader frs = new FileReader(map_file_name);
            StringTokenizer in;

            BufferedReader inline = new BufferedReader(frs);
            thisLine = inline.readLine();
            while (thisLine != null) { // while loop begins here                        
                in = new StringTokenizer(thisLine);
                int edgeWeight = Integer.parseInt(in.nextToken());

                String v1 = in.nextToken();
                String v2 = in.nextToken();
                String roadName = in.nextToken();
                String direction = in.nextToken();
                
                //System.out.println(v1+" "+v2);
                g.addVertex(v2);
                g.addVertex(v1);
                edge = g.addEdge(v2, v1);
                edge.setRoadName(roadName);
                edge.setDirection(direction);
                edge.setFrom(v1);
                edge.setTo(v2);
                
                g.setEdgeWeight(edge, edgeWeight);

                thisLine = inline.readLine();
            }
            frs.close();


        //  } catch (FileNotFoundException fnfe) {

        //    } catch (NullPointerException npe) {
        //npe.printStackTrace();
        } catch (Exception e) {
        	Log.e(getClass().getSimpleName(), e.getMessage(), e);
            //Main.help();
            System.err.println("Please enter correct map file name");
            System.exit(0);
        //e.printStackTrace();
        }


    //System.out.println(g.toString());
    }
    
    public void createHeuristicTable(String heuristic_file_name) {
        try {
            FileReader frs = new FileReader(heuristic_file_name);
            StringTokenizer in;
            String thisLine;

            BufferedReader inline = new BufferedReader(frs);
            thisLine = inline.readLine();
            while (thisLine != null) { // while loop begins here                        
                in = new StringTokenizer(thisLine);
                String city = in.nextToken();
                String hs = in.nextToken();
                int h;
                if (hs.equals("-")) {
                    h = 0;
                } else {
                    h = Integer.parseInt(hs);
                }
                heuristic.put(city, h);
                thisLine = inline.readLine();
            }
            frs.close();


        //   } catch (FileNotFoundException fnfe) {

        //   } catch (NullPointerException npe) {
        //npe.printStackTrace();
        } catch (Exception e) {
            //Main.help();
            System.err.println("Please enter correct heuristic file name");
            System.exit(0);
        //e.printStackTrace();
        }
    // System.out.println(heuristic.toString());

    }
}
