package csci561_hw1_search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
 * (C) Copyright 2008, by Chin-Kai Chang
 * 
 * This file is part of Search.
 * 
 * Search is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * Search is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * search; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 * 
 * @file: Problems.java
 * @author: Chin-Kai Chang <chinkaic@usc.edu>
 * @Purpose: Defines the search problem
 */
/*
 * Here we are using graph library named JGraphT. To have more information about
 * JGraphT, please visited http://jgrapht.sourceforge.net/
 */

public class Problems {

	private String initial_state;
	private String goal_state;
	private String map;
	private SimpleWeightedGraph<String, RoadEdge> g; //
	private Hashtable heuristic;
	private ArrayList<String> direction;
	public String solution;

	public Problems(String map_file_name) {
		Log.v(getClass().getSimpleName(), "Create Problem with map="
				+ map_file_name);
		map = map_file_name;
		g = new SimpleWeightedGraph<String, RoadEdge>(RoadEdge.class);
		// heuristic = new Hashtable();
		heuristic = null;
		solution = "";
		//createMap(map);
                createMap() ; // use hard-coded graph
		direction = new ArrayList<String>();
		// System.out.println("Map created");
	}

	public void Search(String algorithm, String heuristic_file_name) {
		// System.out.println("Search start");
		if (heuristic != null && heuristic.isEmpty()) {
			System.out.println("heuristic table created");
			createHeuristicTable(heuristic_file_name);
		}

		// System.out.println("================= Using " + algorithm +
		// " Search =================");

		Node n = search_proxy(algorithm, initial_state, goal_state);
		solution = "";
		if (n != null) {
			findSolution(n);
		} else {
			solution = " No Solution ";
			direction.add(solution);
		}
		System.out.println("Final solution:[\n"
				+ solution.substring(0, solution.length() - 1) + "\n]");

	}

	public String[] getDirection() {
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
		// Node root = new Node(ini, g, null);// without heuristic data
		queue.addElement(root);
		Vector<String> visitedNodes = new Vector<String>();
		while (queue.hasMoreElements()) {

			// System.out.println(queue.toString(Registry.isHeuristic(algo)));
			// //output result
			Node node = queue.removeElement();
			visitedNodes.add(node.getCityName());
			// System.out.println("Search city="+node.getCityName());

			if (node.getCityName().equals(goal)) {
				return node;// Found the target
			}
			try{
				node.expand(algo);
			}catch(Exception e){
				Log.e("","Can't Find Source Info");
			}
			for (int i = 0; i < node.getNumChildren(); ++i) {
				Node child = node.getChild(i);
				// Prevent loop search
				if (!visitedNodes.contains(child.getCityName())) {
					// System.out.print("("+child.getNodeCost()+" "+child.getPathCost()+
					// "["+node.getCityName()+" "+child.getCityName()+"])");
					queue.addElement(child);
				}
				// System.out.println();
			}
		}// end while
		return null;
	}

	private void findSolution(Node node) {
		if (node.getParent() == null) {
			String start = "Start From " + node.getCityName();
			solution = start + " \n" + solution;
			direction.add(start);
		} else {
			String turn = "Turn " + node.getCity_parent_direction() + ". on "
					+ node.getCity_parent_road() + " to " + node.getCityName();
			solution = turn + " \n" + solution;
			direction.add(turn);

		}
		if (node.getParent() != null) {
			findSolution(node.getParent());
		}
	}

	private ArrayList<String> reverseList(ArrayList<String> dir) {
		ArrayList<String> redir = new ArrayList<String>();
		for (int i = 1; i <= dir.size(); i++) {
			redir.add(dir.get(dir.size() - i));
			System.out.println(dir.get(dir.size() - i));
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

	public void saveMap(String map_file_name) {

		try {
			// FileWriter fw = new FileWriter(map_file_name);
			// FileInputStream android.content.ContextWrapper.openFileInput
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
		FileReader frs = null;
		try {
			// FileInputStream fIn = openFileInput(map_file_name);
			// FileInputStream android.content.ContextWrapper.openFileInput fIn
			// =
			frs = new FileReader(map_file_name);
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

				// System.out.println(v1+" "+v2);
				g.addVertex(v2);
				g.addVertex(v1);
				edge = g.addEdge(v2, v1);
				edge.setRoadName(roadName);
				edge.setDirection(direction);
				edge.setFrom(v1);
				edge.setTo(v2);

				g.setEdgeWeight(edge, edgeWeight);

				thisLine = inline.readLine();
			}// end while
			frs.close();

			// } catch (FileNotFoundException fnfe) {
			// } catch (NullPointerException npe) {
			// npe.printStackTrace();
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), e.getMessage(), e);
			// Main.help();
			System.err.println("Please enter correct map file name");
			// System.exit(0);
		} finally {
			try {
				frs.close();
			} catch (Exception e) {
			}
		}
	}

//------------------------- HARD-CODED GRAPH ----------------------------

// This hard-coded version of createMap() creates the graph directly
// without resorting to the external file of node-edge info because then
// we don't have to worry about submitting an external file that needs to
// be pushed into the phone.
//
// DEVNOTE: This is a HUGE function and an excellent example of what not
// to do in ordinary software engineering terms. However, it works for
// now and makes life a little less complicated for the purposes of
// grading this assignment.
public void createMap() {
   RoadEdge edge = new RoadEdge() ;
   try {
      g.addVertex("GER") ;
      g.addVertex("IRC") ;
      edge = g.addEdge("GER", "IRC") ;
      edge.setRoadName("W_37Th") ;
      edge.setDirection("N") ;
      edge.setFrom("IRC") ;
      edge.setTo("GER") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("PKS") ;
      g.addVertex("IRC") ;
      edge = g.addEdge("PKS", "IRC") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("IRC") ;
      edge.setTo("PKS") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("EEB") ;
      g.addVertex("PKS") ;
      edge = g.addEdge("EEB", "PKS") ;
      edge.setRoadName("W_37Th") ;
      edge.setDirection("N") ;
      edge.setFrom("PKS") ;
      edge.setTo("EEB") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("PRB") ;
      g.addVertex("PKS") ;
      edge = g.addEdge("PRB", "PKS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("PKS") ;
      edge.setTo("PRB") ;
      g.setEdgeWeight(edge, 10) ;

      g.addVertex("SAL") ;
      g.addVertex("PRB") ;
      edge = g.addEdge("SAL", "PRB") ;
      edge.setRoadName("W_37Th") ;
      edge.setDirection("N") ;
      edge.setFrom("PRB") ;
      edge.setTo("SAL") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("PHE") ;
      g.addVertex("PRB") ;
      edge = g.addEdge("PHE", "PRB") ;
      edge.setRoadName("W_37Th") ;
      edge.setDirection("N") ;
      edge.setFrom("PRB") ;
      edge.setTo("PHE") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("GER") ;
      g.addVertex("SCD") ;
      edge = g.addEdge("GER", "SCD") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("SCD") ;
      edge.setTo("GER") ;
      g.setEdgeWeight(edge, 9) ;

      g.addVertex("PSA") ;
      g.addVertex("SCD") ;
      edge = g.addEdge("PSA", "SCD") ;
      edge.setRoadName("Bloom_Walk") ;
      edge.setDirection("N") ;
      edge.setFrom("SCD") ;
      edge.setTo("PSA") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("PSA") ;
      g.addVertex("GER") ;
      edge = g.addEdge("PSA", "GER") ;
      edge.setRoadName("Bloom_Walk") ;
      edge.setDirection("N") ;
      edge.setFrom("GER") ;
      edge.setTo("PSA") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("RTH") ;
      g.addVertex("GER") ;
      edge = g.addEdge("RTH", "GER") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("GER") ;
      edge.setTo("RTH") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("EEB") ;
      g.addVertex("GER") ;
      edge = g.addEdge("EEB", "GER") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("GER") ;
      edge.setTo("EEB") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("RTH") ;
      g.addVertex("EEB") ;
      edge = g.addEdge("RTH", "EEB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("EEB") ;
      edge.setTo("RTH") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("SAL") ;
      g.addVertex("EEB") ;
      edge = g.addEdge("SAL", "EEB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("EEB") ;
      edge.setTo("SAL") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("OHE") ;
      g.addVertex("RTH") ;
      edge = g.addEdge("OHE", "RTH") ;
      edge.setRoadName("Bloom_Walk") ;
      edge.setDirection("N") ;
      edge.setFrom("RTH") ;
      edge.setTo("OHE") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("SSC") ;
      g.addVertex("RTH") ;
      edge = g.addEdge("SSC", "RTH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("RTH") ;
      edge.setTo("SSC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("SSC") ;
      g.addVertex("SAL") ;
      edge = g.addEdge("SSC", "SAL") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("SAL") ;
      edge.setTo("SSC") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("PHE") ;
      g.addVertex("SAL") ;
      edge = g.addEdge("PHE", "SAL") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("SAL") ;
      edge.setTo("PHE") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("HED") ;
      g.addVertex("SSC") ;
      edge = g.addEdge("HED", "SSC") ;
      edge.setRoadName("Bloom_Walk") ;
      edge.setDirection("N") ;
      edge.setFrom("SSC") ;
      edge.setTo("HED") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("PCE") ;
      g.addVertex("SSC") ;
      edge = g.addEdge("PCE", "SSC") ;
      edge.setRoadName("Bloom_Walk") ;
      edge.setDirection("N") ;
      edge.setFrom("SSC") ;
      edge.setTo("PCE") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("SSL") ;
      g.addVertex("SSC") ;
      edge = g.addEdge("SSL", "SSC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("SSC") ;
      edge.setTo("SSL") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("SSL") ;
      g.addVertex("PHE") ;
      edge = g.addEdge("SSL", "PHE") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("PHE") ;
      edge.setTo("SSL") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("WAH") ;
      g.addVertex("PHE") ;
      edge = g.addEdge("WAH", "PHE") ;
      edge.setRoadName("Watt_Way") ;
      edge.setDirection("E") ;
      edge.setFrom("PHE") ;
      edge.setTo("WAH") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("VHE") ;
      g.addVertex("SSL") ;
      edge = g.addEdge("VHE", "SSL") ;
      edge.setRoadName("Bloom_Walk") ;
      edge.setDirection("N") ;
      edge.setFrom("SSL") ;
      edge.setTo("VHE") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("WAH") ;
      g.addVertex("SSL") ;
      edge = g.addEdge("WAH", "SSL") ;
      edge.setRoadName("Watt_Way") ;
      edge.setDirection("E") ;
      edge.setFrom("SSL") ;
      edge.setTo("WAH") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("LHI") ;
      g.addVertex("WAH") ;
      edge = g.addEdge("LHI", "WAH") ;
      edge.setRoadName("Bloom_Walk") ;
      edge.setDirection("N") ;
      edge.setFrom("WAH") ;
      edge.setTo("LHI") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("HAR") ;
      g.addVertex("WAH") ;
      edge = g.addEdge("HAR", "WAH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("WAH") ;
      edge.setTo("HAR") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("SLH") ;
      g.addVertex("HAR") ;
      edge = g.addEdge("SLH", "HAR") ;
      edge.setRoadName("Bloom_Walk") ;
      edge.setDirection("N") ;
      edge.setFrom("HAR") ;
      edge.setTo("SLH") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("ACB") ;
      g.addVertex("HAR") ;
      edge = g.addEdge("ACB", "HAR") ;
      edge.setRoadName("Bloom_Walk") ;
      edge.setDirection("N") ;
      edge.setFrom("HAR") ;
      edge.setTo("ACB") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("MHP") ;
      g.addVertex("HAR") ;
      edge = g.addEdge("MHP", "HAR") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("HAR") ;
      edge.setTo("MHP") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("ZHS") ;
      g.addVertex("MHP") ;
      edge = g.addEdge("ZHS", "MHP") ;
      edge.setRoadName("Bloom_Walk") ;
      edge.setDirection("N") ;
      edge.setFrom("MHP") ;
      edge.setTo("ZHS") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("KAP") ;
      g.addVertex("DRB") ;
      edge = g.addEdge("KAP", "DRB") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("DRB") ;
      edge.setTo("KAP") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("PSA") ;
      g.addVertex("DRB") ;
      edge = g.addEdge("PSA", "DRB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("DRB") ;
      edge.setTo("PSA") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("RRI") ;
      g.addVertex("PSA") ;
      edge = g.addEdge("RRI", "PSA") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("PSA") ;
      edge.setTo("RRI") ;
      g.setEdgeWeight(edge, 10) ;

      g.addVertex("CWT") ;
      g.addVertex("PSA") ;
      edge = g.addEdge("CWT", "PSA") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("PSA") ;
      edge.setTo("CWT") ;
      g.setEdgeWeight(edge, 9) ;

      g.addVertex("CWO") ;
      g.addVertex("PSA") ;
      edge = g.addEdge("CWO", "PSA") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("PSA") ;
      edge.setTo("CWO") ;
      g.setEdgeWeight(edge, 9) ;

      g.addVertex("HRC") ;
      g.addVertex("PSA") ;
      edge = g.addEdge("HRC", "PSA") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("PSA") ;
      edge.setTo("HRC") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("OHE") ;
      g.addVertex("PSA") ;
      edge = g.addEdge("OHE", "PSA") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("PSA") ;
      edge.setTo("OHE") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("SGM") ;
      g.addVertex("OHE") ;
      edge = g.addEdge("SGM", "OHE") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("OHE") ;
      edge.setTo("SGM") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("HED") ;
      g.addVertex("OHE") ;
      edge = g.addEdge("HED", "OHE") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("OHE") ;
      edge.setTo("HED") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("BHE") ;
      g.addVertex("OHE") ;
      edge = g.addEdge("BHE", "OHE") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("OHE") ;
      edge.setTo("BHE") ;
      g.setEdgeWeight(edge, 10) ;

      g.addVertex("VHE") ;
      g.addVertex("OHE") ;
      edge = g.addEdge("VHE", "OHE") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("OHE") ;
      edge.setTo("VHE") ;
      g.setEdgeWeight(edge, 20) ;

      g.addVertex("PCE") ;
      g.addVertex("HED") ;
      edge = g.addEdge("PCE", "HED") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("HED") ;
      edge.setTo("PCE") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("BHE") ;
      g.addVertex("PCE") ;
      edge = g.addEdge("BHE", "PCE") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("PCE") ;
      edge.setTo("BHE") ;
      g.setEdgeWeight(edge, 10) ;

      g.addVertex("VHE") ;
      g.addVertex("PCE") ;
      edge = g.addEdge("VHE", "PCE") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("PCE") ;
      edge.setTo("VHE") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("801") ;
      g.addVertex("BHE") ;
      edge = g.addEdge("801", "BHE") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("BHE") ;
      edge.setTo("801") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("VHE") ;
      g.addVertex("BHE") ;
      edge = g.addEdge("VHE", "BHE") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("BHE") ;
      edge.setTo("VHE") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("HNB") ;
      g.addVertex("VHE") ;
      edge = g.addEdge("HNB", "VHE") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("VHE") ;
      edge.setTo("HNB") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("LHI") ;
      g.addVertex("VHE") ;
      edge = g.addEdge("LHI", "VHE") ;
      edge.setRoadName("Watt_Way") ;
      edge.setDirection("E") ;
      edge.setFrom("VHE") ;
      edge.setTo("LHI") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("PRB") ;
      g.addVertex("LHI") ;
      edge = g.addEdge("PRB", "LHI") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("LHI") ;
      edge.setTo("PRB") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("SLH") ;
      g.addVertex("LHI") ;
      edge = g.addEdge("SLH", "LHI") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("LHI") ;
      edge.setTo("SLH") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("HSH") ;
      g.addVertex("PRB") ;
      edge = g.addEdge("HSH", "PRB") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("PRB") ;
      edge.setTo("HSH") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("LJS") ;
      g.addVertex("PRB") ;
      edge = g.addEdge("LJS", "PRB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("PRB") ;
      edge.setTo("LJS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("SHS") ;
      g.addVertex("PRB") ;
      edge = g.addEdge("SHS", "PRB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("PRB") ;
      edge.setTo("SHS") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("LJS") ;
      g.addVertex("SLH") ;
      edge = g.addEdge("LJS", "SLH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("SLH") ;
      edge.setTo("LJS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("ACB") ;
      g.addVertex("SLH") ;
      edge = g.addEdge("ACB", "SLH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("SLH") ;
      edge.setTo("ACB") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("SHS") ;
      g.addVertex("ACB") ;
      edge = g.addEdge("SHS", "ACB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("ACB") ;
      edge.setTo("SHS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("CEM") ;
      g.addVertex("ACB") ;
      edge = g.addEdge("CEM", "ACB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("ACB") ;
      edge.setTo("CEM") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("ZHS") ;
      g.addVertex("ACB") ;
      edge = g.addEdge("ZHS", "ACB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("ACB") ;
      edge.setTo("ZHS") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("LJS") ;
      g.addVertex("SHS") ;
      edge = g.addEdge("LJS", "SHS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("SHS") ;
      edge.setTo("LJS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("CEM") ;
      g.addVertex("SHS") ;
      edge = g.addEdge("CEM", "SHS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("SHS") ;
      edge.setTo("CEM") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("STO") ;
      g.addVertex("LJS") ;
      edge = g.addEdge("STO", "LJS") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("LJS") ;
      edge.setTo("STO") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("CEM") ;
      g.addVertex("LJS") ;
      edge = g.addEdge("CEM", "LJS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("LJS") ;
      edge.setTo("CEM") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("TCC") ;
      g.addVertex("CEM") ;
      edge = g.addEdge("TCC", "CEM") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("CEM") ;
      edge.setTo("TCC") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("OCW") ;
      g.addVertex("CEM") ;
      edge = g.addEdge("OCW", "CEM") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("CEM") ;
      edge.setTo("OCW") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("ZHS") ;
      g.addVertex("CEM") ;
      edge = g.addEdge("ZHS", "CEM") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("CEM") ;
      edge.setTo("ZHS") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("OCW") ;
      g.addVertex("ZHS") ;
      edge = g.addEdge("OCW", "ZHS") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("ZHS") ;
      edge.setTo("OCW") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("ACC") ;
      g.addVertex("ZHS") ;
      edge = g.addEdge("ACC", "ZHS") ;
      edge.setRoadName("Trousdale_Pkwy") ;
      edge.setDirection("E") ;
      edge.setFrom("ZHS") ;
      edge.setTo("ACC") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("BRI") ;
      g.addVertex("ZHS") ;
      edge = g.addEdge("BRI", "ZHS") ;
      edge.setRoadName("Trousdale_Pkwy") ;
      edge.setDirection("E") ;
      edge.setFrom("ZHS") ;
      edge.setTo("BRI") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("TCC") ;
      g.addVertex("OCW") ;
      edge = g.addEdge("TCC", "OCW") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("OCW") ;
      edge.setTo("TCC") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("ACC") ;
      g.addVertex("OCW") ;
      edge = g.addEdge("ACC", "OCW") ;
      edge.setRoadName("Trousdale_Pkwy") ;
      edge.setDirection("E") ;
      edge.setFrom("OCW") ;
      edge.setTo("ACC") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("ACC") ;
      g.addVertex("BRI") ;
      edge = g.addEdge("ACC", "BRI") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("BRI") ;
      edge.setTo("ACC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("HOH") ;
      g.addVertex("BRI") ;
      edge = g.addEdge("HOH", "BRI") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("BRI") ;
      edge.setTo("HOH") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("AHF") ;
      g.addVertex("ACC") ;
      edge = g.addEdge("AHF", "ACC") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("ACC") ;
      edge.setTo("AHF") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("HOH") ;
      g.addVertex("ACC") ;
      edge = g.addEdge("HOH", "ACC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("ACC") ;
      edge.setTo("HOH") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("AHF") ;
      g.addVertex("HOH") ;
      edge = g.addEdge("AHF", "HOH") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("HOH") ;
      edge.setTo("AHF") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("LAW") ;
      g.addVertex("HOH") ;
      edge = g.addEdge("LAW", "HOH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("HOH") ;
      edge.setTo("LAW") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("TGF") ;
      g.addVertex("LAW") ;
      edge = g.addEdge("TGF", "LAW") ;
      edge.setRoadName("Downey_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("LAW") ;
      edge.setTo("TGF") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("FAC") ;
      g.addVertex("LAW") ;
      edge = g.addEdge("FAC", "LAW") ;
      edge.setRoadName("Downey_way") ;
      edge.setDirection("N") ;
      edge.setFrom("LAW") ;
      edge.setTo("FAC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("MTS") ;
      g.addVertex("KAP") ;
      edge = g.addEdge("MTS", "KAP") ;
      edge.setRoadName("Childs_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("KAP") ;
      edge.setTo("MTS") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("RRI") ;
      g.addVertex("KAP") ;
      edge = g.addEdge("RRI", "KAP") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("KAP") ;
      edge.setTo("RRI") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("DRC") ;
      g.addVertex("RRI") ;
      edge = g.addEdge("DRC", "RRI") ;
      edge.setRoadName("Childs_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("RRI") ;
      edge.setTo("DRC") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("CWT") ;
      g.addVertex("RRI") ;
      edge = g.addEdge("CWT", "RRI") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("RRI") ;
      edge.setTo("CWT") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("CWO") ;
      g.addVertex("CWT") ;
      edge = g.addEdge("CWO", "CWT") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("CWT") ;
      edge.setTo("CWO") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("HRC") ;
      g.addVertex("CWO") ;
      edge = g.addEdge("HRC", "CWO") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("CWO") ;
      edge.setTo("HRC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("DNI") ;
      g.addVertex("HRC") ;
      edge = g.addEdge("DNI", "HRC") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("HRC") ;
      edge.setTo("DNI") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("DNI") ;
      g.addVertex("SGM") ;
      edge = g.addEdge("DNI", "SGM") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("SGM") ;
      edge.setTo("DNI") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("801") ;
      g.addVertex("SGM") ;
      edge = g.addEdge("801", "SGM") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("SGM") ;
      edge.setTo("801") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("GFS") ;
      g.addVertex("801") ;
      edge = g.addEdge("GFS", "801") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("801") ;
      edge.setTo("GFS") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("HNB") ;
      g.addVertex("801") ;
      edge = g.addEdge("HNB", "801") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("801") ;
      edge.setTo("HNB") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("GFS") ;
      g.addVertex("HNB") ;
      edge = g.addEdge("GFS", "HNB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("HNB") ;
      edge.setTo("GFS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("HSH") ;
      g.addVertex("HNB") ;
      edge = g.addEdge("HSH", "HNB") ;
      edge.setRoadName("Watt_Way") ;
      edge.setDirection("E") ;
      edge.setFrom("HNB") ;
      edge.setTo("HSH") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("STO") ;
      g.addVertex("HSH") ;
      edge = g.addEdge("STO", "HSH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("HSH") ;
      edge.setTo("STO") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("PED") ;
      g.addVertex("HSH") ;
      edge = g.addEdge("PED", "HSH") ;
      edge.setRoadName("Childs_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("HSH") ;
      edge.setTo("PED") ;
      g.setEdgeWeight(edge, 13) ;

      g.addVertex("BKS") ;
      g.addVertex("STO") ;
      edge = g.addEdge("BKS", "STO") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("STO") ;
      edge.setTo("BKS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("TCC") ;
      g.addVertex("STO") ;
      edge = g.addEdge("TCC", "STO") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("STO") ;
      edge.setTo("TCC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("TCC") ;
      g.addVertex("BKS") ;
      edge = g.addEdge("TCC", "BKS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("BKS") ;
      edge.setTo("TCC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("PED") ;
      g.addVertex("BKS") ;
      edge = g.addEdge("PED", "BKS") ;
      edge.setRoadName("Childs_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("BKS") ;
      edge.setTo("PED") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("STU") ;
      g.addVertex("TCC") ;
      edge = g.addEdge("STU", "TCC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("TCC") ;
      edge.setTo("STU") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("ADM") ;
      g.addVertex("TCC") ;
      edge = g.addEdge("ADM", "TCC") ;
      edge.setRoadName("Childs_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("TCC") ;
      edge.setTo("ADM") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("AHF") ;
      g.addVertex("STU") ;
      edge = g.addEdge("AHF", "STU") ;
      edge.setRoadName("Trousdale_Pkwy") ;
      edge.setDirection("E") ;
      edge.setFrom("STU") ;
      edge.setTo("AHF") ;
      g.setEdgeWeight(edge, 9) ;

      g.addVertex("ADM") ;
      g.addVertex("STU") ;
      edge = g.addEdge("ADM", "STU") ;
      edge.setRoadName("Childs_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("STU") ;
      edge.setTo("ADM") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("TGF") ;
      g.addVertex("AHF") ;
      edge = g.addEdge("TGF", "AHF") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("AHF") ;
      edge.setTo("TGF") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("JHH") ;
      g.addVertex("AHF") ;
      edge = g.addEdge("JHH", "AHF") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("AHF") ;
      edge.setTo("JHH") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("VKC") ;
      g.addVertex("AHF") ;
      edge = g.addEdge("VKC", "AHF") ;
      edge.setRoadName("Alumni_Park") ;
      edge.setDirection("N") ;
      edge.setFrom("AHF") ;
      edge.setTo("VKC") ;
      g.setEdgeWeight(edge, 25) ;

      g.addVertex("JHH") ;
      g.addVertex("TGF") ;
      edge = g.addEdge("JHH", "TGF") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("TGF") ;
      edge.setTo("JHH") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("FAC") ;
      g.addVertex("TGF") ;
      edge = g.addEdge("FAC", "TGF") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("TGF") ;
      edge.setTo("FAC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("DML") ;
      g.addVertex("JHH") ;
      edge = g.addEdge("DML", "JHH") ;
      edge.setRoadName("Childs_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("JHH") ;
      edge.setTo("DML") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("RGL") ;
      g.addVertex("JHH") ;
      edge = g.addEdge("RGL", "JHH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("JHH") ;
      edge.setTo("RGL") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("RGL") ;
      g.addVertex("FAC") ;
      edge = g.addEdge("RGL", "FAC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("FAC") ;
      edge.setTo("RGL") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("JKP") ;
      g.addVertex("FAC") ;
      edge = g.addEdge("JKP", "FAC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("FAC") ;
      edge.setTo("JKP") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("PTD") ;
      g.addVertex("RGL") ;
      edge = g.addEdge("PTD", "RGL") ;
      edge.setRoadName("Childs_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("RGL") ;
      edge.setTo("PTD") ;
      g.setEdgeWeight(edge, 23) ;

      g.addVertex("JKP") ;
      g.addVertex("RGL") ;
      edge = g.addEdge("JKP", "RGL") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("RGL") ;
      edge.setTo("JKP") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("DXM") ;
      g.addVertex("JKP") ;
      edge = g.addEdge("DXM", "JKP") ;
      edge.setRoadName("Childs_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("JKP") ;
      edge.setTo("DXM") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("REG") ;
      g.addVertex("JKP") ;
      edge = g.addEdge("REG", "JKP") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("JKP") ;
      edge.setTo("REG") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("TRO") ;
      g.addVertex("REG") ;
      edge = g.addEdge("TRO", "REG") ;
      edge.setRoadName("Childs_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("REG") ;
      edge.setTo("TRO") ;
      g.setEdgeWeight(edge, 9) ;

      g.addVertex("TYL") ;
      g.addVertex("REG") ;
      edge = g.addEdge("TYL", "REG") ;
      edge.setRoadName("Figueroa_St") ;
      edge.setDirection("E") ;
      edge.setFrom("REG") ;
      edge.setTo("TYL") ;
      g.setEdgeWeight(edge, 17) ;

      g.addVertex("RMH") ;
      g.addVertex("TYL") ;
      edge = g.addEdge("RMH", "TYL") ;
      edge.setRoadName("Exposition_Blvd") ;
      edge.setDirection("N") ;
      edge.setFrom("TYL") ;
      edge.setTo("RMH") ;
      g.setEdgeWeight(edge, 14) ;

      g.addVertex("PS2") ;
      g.addVertex("TYL") ;
      edge = g.addEdge("PS2", "TYL") ;
      edge.setRoadName("Exposition_Blvd") ;
      edge.setDirection("N") ;
      edge.setFrom("TYL") ;
      edge.setTo("PS2") ;
      g.setEdgeWeight(edge, 13) ;

      g.addVertex("BDX") ;
      g.addVertex("MTS") ;
      edge = g.addEdge("BDX", "MTS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("MTS") ;
      edge.setTo("BDX") ;
      g.setEdgeWeight(edge, 10) ;

      g.addVertex("BDF") ;
      g.addVertex("MTS") ;
      edge = g.addEdge("BDF", "MTS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("MTS") ;
      edge.setTo("BDF") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("DRC") ;
      g.addVertex("MTS") ;
      edge = g.addEdge("DRC", "MTS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("MTS") ;
      edge.setTo("DRC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("BDF") ;
      g.addVertex("DRC") ;
      edge = g.addEdge("BDF", "DRC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("DRC") ;
      edge.setTo("BDF") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("CFH") ;
      g.addVertex("DRC") ;
      edge = g.addEdge("CFH", "DRC") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("DRC") ;
      edge.setTo("CFH") ;
      g.setEdgeWeight(edge, 20) ;

      g.addVertex("GPC") ;
      g.addVertex("BDX") ;
      edge = g.addEdge("GPC", "BDX") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("BDX") ;
      edge.setTo("GPC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("PSB") ;
      g.addVertex("BDX") ;
      edge = g.addEdge("PSB", "BDX") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("BDX") ;
      edge.setTo("PSB") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("BDF") ;
      g.addVertex("BDX") ;
      edge = g.addEdge("BDF", "BDX") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("BDX") ;
      edge.setTo("BDF") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("LTS") ;
      g.addVertex("BDX") ;
      edge = g.addEdge("LTS", "BDX") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("BDX") ;
      edge.setTo("LTS") ;
      g.setEdgeWeight(edge, 25) ;

      g.addVertex("IMF") ;
      g.addVertex("BDX") ;
      edge = g.addEdge("IMF", "BDX") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("BDX") ;
      edge.setTo("IMF") ;
      g.setEdgeWeight(edge, 25) ;

      g.addVertex("PIC") ;
      g.addVertex("BDF") ;
      edge = g.addEdge("PIC", "BDF") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("BDF") ;
      edge.setTo("PIC") ;
      g.setEdgeWeight(edge, 25) ;

      g.addVertex("PSB") ;
      g.addVertex("GPC") ;
      edge = g.addEdge("PSB", "GPC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("GPC") ;
      edge.setTo("PSB") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("MAC") ;
      g.addVertex("KAB") ;
      edge = g.addEdge("MAC", "KAB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("KAB") ;
      edge.setTo("MAC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("CAP") ;
      g.addVertex("PSB") ;
      edge = g.addEdge("CAP", "PSB") ;
      edge.setRoadName("Jefferson_Blvd") ;
      edge.setDirection("N") ;
      edge.setFrom("PSB") ;
      edge.setTo("CAP") ;
      g.setEdgeWeight(edge, 20) ;

      g.addVertex("LAB") ;
      g.addVertex("PSB") ;
      edge = g.addEdge("LAB", "PSB") ;
      edge.setRoadName("Jefferson_Blvd") ;
      edge.setDirection("N") ;
      edge.setFrom("PSB") ;
      edge.setTo("LAB") ;
      g.setEdgeWeight(edge, 25) ;

      g.addVertex("LRC") ;
      g.addVertex("PSB") ;
      edge = g.addEdge("LRC", "PSB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("PSB") ;
      edge.setTo("LRC") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("MAC") ;
      g.addVertex("PSB") ;
      edge = g.addEdge("MAC", "PSB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("PSB") ;
      edge.setTo("MAC") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("LRC") ;
      g.addVertex("MAC") ;
      edge = g.addEdge("LRC", "MAC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("MAC") ;
      edge.setTo("LRC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("FMS") ;
      g.addVertex("MAC") ;
      edge = g.addEdge("FMS", "MAC") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("MAC") ;
      edge.setTo("FMS") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("FLT") ;
      g.addVertex("LRC") ;
      edge = g.addEdge("FLT", "LRC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("LRC") ;
      edge.setTo("FLT") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("KOH") ;
      g.addVertex("LRC") ;
      edge = g.addEdge("KOH", "LRC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("LRC") ;
      edge.setTo("KOH") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("WTO") ;
      g.addVertex("LRC") ;
      edge = g.addEdge("WTO", "LRC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("LRC") ;
      edge.setTo("WTO") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("FMS") ;
      g.addVertex("LRC") ;
      edge = g.addEdge("FMS", "LRC") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("LRC") ;
      edge.setTo("FMS") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("KOH") ;
      g.addVertex("FLT") ;
      edge = g.addEdge("KOH", "FLT") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("FLT") ;
      edge.setTo("KOH") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("CAR") ;
      g.addVertex("FLT") ;
      edge = g.addEdge("CAR", "FLT") ;
      edge.setRoadName("Jefferson_Blvd") ;
      edge.setDirection("N") ;
      edge.setFrom("FLT") ;
      edge.setTo("CAR") ;
      g.setEdgeWeight(edge, 14) ;

      g.addVertex("UCC") ;
      g.addVertex("KOH") ;
      edge = g.addEdge("UCC", "KOH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("KOH") ;
      edge.setTo("UCC") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("WTO") ;
      g.addVertex("KOH") ;
      edge = g.addEdge("WTO", "KOH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("KOH") ;
      edge.setTo("WTO") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("UCC") ;
      g.addVertex("WTO") ;
      edge = g.addEdge("UCC", "WTO") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("WTO") ;
      edge.setTo("UCC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("DEN") ;
      g.addVertex("WTO") ;
      edge = g.addEdge("DEN", "WTO") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("WTO") ;
      edge.setTo("DEN") ;
      g.setEdgeWeight(edge, 13) ;

      g.addVertex("JEP") ;
      g.addVertex("UCC") ;
      edge = g.addEdge("JEP", "UCC") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("UCC") ;
      edge.setTo("JEP") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("CAR") ;
      g.addVertex("UCC") ;
      edge = g.addEdge("CAR", "UCC") ;
      edge.setRoadName("Jefferson_Blvd") ;
      edge.setDirection("N") ;
      edge.setFrom("UCC") ;
      edge.setTo("CAR") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("LAB") ;
      g.addVertex("CAP") ;
      edge = g.addEdge("LAB", "CAP") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("CAP") ;
      edge.setTo("LAB") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("CAR") ;
      g.addVertex("LAB") ;
      edge = g.addEdge("CAR", "LAB") ;
      edge.setRoadName("Orchard_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("LAB") ;
      edge.setTo("CAR") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("OHC") ;
      g.addVertex("CAR") ;
      edge = g.addEdge("OHC", "CAR") ;
      edge.setRoadName("McClintock_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("CAR") ;
      edge.setTo("OHC") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("LTS") ;
      g.addVertex("CFH") ;
      edge = g.addEdge("LTS", "CFH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("CFH") ;
      edge.setTo("LTS") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("CFX") ;
      g.addVertex("CFH") ;
      edge = g.addEdge("CFX", "CFH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("CFH") ;
      edge.setTo("CFX") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("LTS") ;
      g.addVertex("CFX") ;
      edge = g.addEdge("LTS", "CFX") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("CFX") ;
      edge.setTo("LTS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("PIC") ;
      g.addVertex("LTS") ;
      edge = g.addEdge("PIC", "LTS") ;
      edge.setRoadName("Hellman_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("LTS") ;
      edge.setTo("PIC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("FED") ;
      g.addVertex("CFX") ;
      edge = g.addEdge("FED", "CFX") ;
      edge.setRoadName("Watt_Way") ;
      edge.setDirection("E") ;
      edge.setFrom("CFX") ;
      edge.setTo("FED") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("MAR") ;
      g.addVertex("CFX") ;
      edge = g.addEdge("MAR", "CFX") ;
      edge.setRoadName("Hellman_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("CFX") ;
      edge.setTo("MAR") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("HER") ;
      g.addVertex("CFX") ;
      edge = g.addEdge("HER", "CFX") ;
      edge.setRoadName("Hellman_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("CFX") ;
      edge.setTo("HER") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("IMF") ;
      g.addVertex("PIC") ;
      edge = g.addEdge("IMF", "PIC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("PIC") ;
      edge.setTo("IMF") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("MAR") ;
      g.addVertex("PIC") ;
      edge = g.addEdge("MAR", "PIC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("PIC") ;
      edge.setTo("MAR") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("HER") ;
      g.addVertex("PIC") ;
      edge = g.addEdge("HER", "PIC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("PIC") ;
      edge.setTo("HER") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("FMS") ;
      g.addVertex("IMF") ;
      edge = g.addEdge("FMS", "IMF") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("IMF") ;
      edge.setTo("FMS") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("POA") ;
      g.addVertex("IMF") ;
      edge = g.addEdge("POA", "IMF") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("IMF") ;
      edge.setTo("POA") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("POB") ;
      g.addVertex("IMF") ;
      edge = g.addEdge("POB", "IMF") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("IMF") ;
      edge.setTo("POB") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("HER") ;
      g.addVertex("IMF") ;
      edge = g.addEdge("HER", "IMF") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("IMF") ;
      edge.setTo("HER") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("IMF") ;
      g.addVertex("MAR") ;
      edge = g.addEdge("IMF", "MAR") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("MAR") ;
      edge.setTo("IMF") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("ASI") ;
      g.addVertex("HER") ;
      edge = g.addEdge("ASI", "HER") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("HER") ;
      edge.setTo("ASI") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("BIT") ;
      g.addVertex("HER") ;
      edge = g.addEdge("BIT", "HER") ;
      edge.setRoadName("Watt_Way") ;
      edge.setDirection("E") ;
      edge.setFrom("HER") ;
      edge.setTo("BIT") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("ASC") ;
      g.addVertex("HER") ;
      edge = g.addEdge("ASC", "HER") ;
      edge.setRoadName("Watt_Way") ;
      edge.setDirection("E") ;
      edge.setFrom("HER") ;
      edge.setTo("ASC") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("JEP") ;
      g.addVertex("FMS") ;
      edge = g.addEdge("JEP", "FMS") ;
      edge.setRoadName("W_34St") ;
      edge.setDirection("N") ;
      edge.setFrom("FMS") ;
      edge.setTo("JEP") ;
      g.setEdgeWeight(edge, 9) ;

      g.addVertex("POA") ;
      g.addVertex("FMS") ;
      edge = g.addEdge("POA", "FMS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("FMS") ;
      edge.setTo("POA") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("POB") ;
      g.addVertex("POA") ;
      edge = g.addEdge("POB", "POA") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("POA") ;
      edge.setTo("POB") ;
      g.setEdgeWeight(edge, 1) ;

      g.addVertex("HSS") ;
      g.addVertex("POB") ;
      edge = g.addEdge("HSS", "POB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("POB") ;
      edge.setTo("HSS") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("ASI") ;
      g.addVertex("POB") ;
      edge = g.addEdge("ASI", "POB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("POB") ;
      edge.setTo("ASI") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("DEN") ;
      g.addVertex("HSS") ;
      edge = g.addEdge("DEN", "HSS") ;
      edge.setRoadName("W_34St") ;
      edge.setDirection("N") ;
      edge.setFrom("HSS") ;
      edge.setTo("DEN") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("SCA") ;
      g.addVertex("HSS") ;
      edge = g.addEdge("SCA", "HSS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("HSS") ;
      edge.setTo("SCA") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("SCA") ;
      g.addVertex("ASI") ;
      edge = g.addEdge("SCA", "ASI") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("ASI") ;
      edge.setTo("SCA") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("LFE") ;
      g.addVertex("ASI") ;
      edge = g.addEdge("LFE", "ASI") ;
      edge.setRoadName("Watt_Way") ;
      edge.setDirection("E") ;
      edge.setFrom("ASI") ;
      edge.setTo("LFE") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("DEN") ;
      g.addVertex("SCA") ;
      edge = g.addEdge("DEN", "SCA") ;
      edge.setRoadName("W_34St") ;
      edge.setDirection("N") ;
      edge.setFrom("SCA") ;
      edge.setTo("DEN") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("IUC") ;
      g.addVertex("SCA") ;
      edge = g.addEdge("IUC", "SCA") ;
      edge.setRoadName("Watt_Way") ;
      edge.setDirection("E") ;
      edge.setFrom("SCA") ;
      edge.setTo("IUC") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("IMS") ;
      g.addVertex("JEP") ;
      edge = g.addEdge("IMS", "JEP") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("JEP") ;
      edge.setTo("IMS") ;
      g.setEdgeWeight(edge, 20) ;

      g.addVertex("IMS") ;
      g.addVertex("DEN") ;
      edge = g.addEdge("IMS", "DEN") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("DEN") ;
      edge.setTo("IMS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("SHC") ;
      g.addVertex("DEN") ;
      edge = g.addEdge("SHC", "DEN") ;
      edge.setRoadName("Watt_Way") ;
      edge.setDirection("E") ;
      edge.setFrom("DEN") ;
      edge.setTo("SHC") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("URC") ;
      g.addVertex("IMS") ;
      edge = g.addEdge("URC", "IMS") ;
      edge.setRoadName("Watt_Way") ;
      edge.setDirection("E") ;
      edge.setFrom("IMS") ;
      edge.setTo("URC") ;
      g.setEdgeWeight(edge, 15) ;

      g.addVertex("ASC") ;
      g.addVertex("FED") ;
      edge = g.addEdge("ASC", "FED") ;
      edge.setRoadName("Hellman_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("FED") ;
      edge.setTo("ASC") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("ADM") ;
      g.addVertex("FED") ;
      edge = g.addEdge("ADM", "FED") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("FED") ;
      edge.setTo("ADM") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("NCT") ;
      g.addVertex("ADM") ;
      edge = g.addEdge("NCT", "ADM") ;
      edge.setRoadName("Hellman_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("ADM") ;
      edge.setTo("NCT") ;
      g.setEdgeWeight(edge, 18) ;

      g.addVertex("THH") ;
      g.addVertex("ADM") ;
      edge = g.addEdge("THH", "ADM") ;
      edge.setRoadName("Hellman_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("ADM") ;
      edge.setTo("THH") ;
      g.setEdgeWeight(edge, 20) ;

      g.addVertex("DML") ;
      g.addVertex("ADM") ;
      edge = g.addEdge("DML", "ADM") ;
      edge.setRoadName("Alumni_Park") ;
      edge.setDirection("E") ;
      edge.setFrom("ADM") ;
      edge.setTo("DML") ;
      g.setEdgeWeight(edge, 30) ;

      g.addVertex("BIT") ;
      g.addVertex("ASC") ;
      edge = g.addEdge("BIT", "ASC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("ASC") ;
      edge.setTo("BIT") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("VKC") ;
      g.addVertex("ASC") ;
      edge = g.addEdge("VKC", "ASC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("ASC") ;
      edge.setTo("VKC") ;
      g.setEdgeWeight(edge, 18) ;

      g.addVertex("LFB") ;
      g.addVertex("BIT") ;
      edge = g.addEdge("LFB", "BIT") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("BIT") ;
      edge.setTo("LFB") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("NCT") ;
      g.addVertex("BIT") ;
      edge = g.addEdge("NCT", "BIT") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("BIT") ;
      edge.setTo("NCT") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("BMH") ;
      g.addVertex("NCT") ;
      edge = g.addEdge("BMH", "NCT") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("NCT") ;
      edge.setTo("BMH") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("THH") ;
      g.addVertex("NCT") ;
      edge = g.addEdge("THH", "NCT") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("NCT") ;
      edge.setTo("THH") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("LUC") ;
      g.addVertex("LFB") ;
      edge = g.addEdge("LUC", "LFB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("LFB") ;
      edge.setTo("LUC") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("SSS") ;
      g.addVertex("LFB") ;
      edge = g.addEdge("SSS", "LFB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("LFB") ;
      edge.setTo("SSS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("CTV") ;
      g.addVertex("SSS") ;
      edge = g.addEdge("CTV", "SSS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("SSS") ;
      edge.setTo("CTV") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("RHM") ;
      g.addVertex("SSS") ;
      edge = g.addEdge("RHM", "SSS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("SSS") ;
      edge.setTo("RHM") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("CSS") ;
      g.addVertex("LUC") ;
      edge = g.addEdge("CSS", "LUC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("LUC") ;
      edge.setTo("CSS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("CTV") ;
      g.addVertex("LUC") ;
      edge = g.addEdge("CTV", "LUC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("LUC") ;
      edge.setTo("CTV") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("CSS") ;
      g.addVertex("CTV") ;
      edge = g.addEdge("CSS", "CTV") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("CTV") ;
      edge.setTo("CSS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("MUS") ;
      g.addVertex("CTV") ;
      edge = g.addEdge("MUS", "CTV") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("CTV") ;
      edge.setTo("MUS") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("SHC") ;
      g.addVertex("CSS") ;
      edge = g.addEdge("SHC", "CSS") ;
      edge.setRoadName("W_34St") ;
      edge.setDirection("N") ;
      edge.setFrom("CSS") ;
      edge.setTo("SHC") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("MUS") ;
      g.addVertex("CSS") ;
      edge = g.addEdge("MUS", "CSS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("CSS") ;
      edge.setTo("MUS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("MUS") ;
      g.addVertex("RHM") ;
      edge = g.addEdge("MUS", "RHM") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("RHM") ;
      edge.setTo("MUS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("BMH") ;
      g.addVertex("RHM") ;
      edge = g.addEdge("BMH", "RHM") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("RHM") ;
      edge.setTo("BMH") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("URC") ;
      g.addVertex("MUS") ;
      edge = g.addEdge("URC", "MUS") ;
      edge.setRoadName("W_34St") ;
      edge.setDirection("N") ;
      edge.setFrom("MUS") ;
      edge.setTo("URC") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("BMH") ;
      g.addVertex("MUS") ;
      edge = g.addEdge("BMH", "MUS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("MUS") ;
      edge.setTo("BMH") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("THH") ;
      g.addVertex("BMH") ;
      edge = g.addEdge("THH", "BMH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("BMH") ;
      edge.setTo("THH") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("UUC") ;
      g.addVertex("THH") ;
      edge = g.addEdge("UUC", "THH") ;
      edge.setRoadName("W_34St") ;
      edge.setDirection("N") ;
      edge.setFrom("THH") ;
      edge.setTo("UUC") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("AHN") ;
      g.addVertex("THH") ;
      edge = g.addEdge("AHN", "THH") ;
      edge.setRoadName("W_34St") ;
      edge.setDirection("N") ;
      edge.setFrom("THH") ;
      edge.setTo("AHN") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("JEP") ;
      g.addVertex("THH") ;
      edge = g.addEdge("JEP", "THH") ;
      edge.setRoadName("W_34St") ;
      edge.setDirection("N") ;
      edge.setFrom("THH") ;
      edge.setTo("JEP") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("SOS") ;
      g.addVertex("THH") ;
      edge = g.addEdge("SOS", "THH") ;
      edge.setRoadName("Trousdale_Parkway") ;
      edge.setDirection("E") ;
      edge.setFrom("THH") ;
      edge.setTo("SOS") ;
      g.setEdgeWeight(edge, 10) ;

      g.addVertex("WPH") ;
      g.addVertex("THH") ;
      edge = g.addEdge("WPH", "THH") ;
      edge.setRoadName("Trousdale_Parkway") ;
      edge.setDirection("E") ;
      edge.setFrom("THH") ;
      edge.setTo("WPH") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("CAS") ;
      g.addVertex("THH") ;
      edge = g.addEdge("CAS", "THH") ;
      edge.setRoadName("Trousdale_Parkway") ;
      edge.setDirection("E") ;
      edge.setFrom("THH") ;
      edge.setTo("CAS") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("URC") ;
      g.addVertex("SHC") ;
      edge = g.addEdge("URC", "SHC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("SHC") ;
      edge.setTo("URC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("AHN") ;
      g.addVertex("URC") ;
      edge = g.addEdge("AHN", "URC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("URC") ;
      edge.setTo("AHN") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("JEP") ;
      g.addVertex("AHN") ;
      edge = g.addEdge("JEP", "AHN") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("AHN") ;
      edge.setTo("JEP") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("EDL") ;
      g.addVertex("JEP") ;
      edge = g.addEdge("EDL", "JEP") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("JEP") ;
      edge.setTo("EDL") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("WPH") ;
      g.addVertex("VKC") ;
      edge = g.addEdge("WPH", "VKC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("VKC") ;
      edge.setTo("WPH") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("SOS") ;
      g.addVertex("VKC") ;
      edge = g.addEdge("SOS", "VKC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("VKC") ;
      edge.setTo("SOS") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("LVL") ;
      g.addVertex("VKC") ;
      edge = g.addEdge("LVL", "VKC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("VKC") ;
      edge.setTo("LVL") ;
      g.setEdgeWeight(edge, 10) ;

      g.addVertex("PSX") ;
      g.addVertex("VKC") ;
      edge = g.addEdge("PSX", "VKC") ;
      edge.setRoadName("McCarthy_Quad") ;
      edge.setDirection("E") ;
      edge.setFrom("VKC") ;
      edge.setTo("PSX") ;
      g.setEdgeWeight(edge, 20) ;

      g.addVertex("EDL") ;
      g.addVertex("SOS") ;
      edge = g.addEdge("EDL", "SOS") ;
      edge.setRoadName("W_34St") ;
      edge.setDirection("N") ;
      edge.setFrom("SOS") ;
      edge.setTo("EDL") ;
      g.setEdgeWeight(edge, 17) ;

      g.addVertex("PSX") ;
      g.addVertex("SOS") ;
      edge = g.addEdge("PSX", "SOS") ;
      edge.setRoadName("McCarthy_Quad") ;
      edge.setDirection("E") ;
      edge.setFrom("SOS") ;
      edge.setTo("PSX") ;
      g.setEdgeWeight(edge, 23) ;

      g.addVertex("CAS") ;
      g.addVertex("WPH") ;
      edge = g.addEdge("CAS", "WPH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("WPH") ;
      edge.setTo("CAS") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("LVL") ;
      g.addVertex("WPH") ;
      edge = g.addEdge("LVL", "WPH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("WPH") ;
      edge.setTo("LVL") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("LVL") ;
      g.addVertex("CAS") ;
      edge = g.addEdge("LVL", "CAS") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("CAS") ;
      edge.setTo("LVL") ;
      g.setEdgeWeight(edge, 9) ;

      g.addVertex("MRF") ;
      g.addVertex("EDL") ;
      edge = g.addEdge("MRF", "EDL") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("EDL") ;
      edge.setTo("MRF") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("LVL") ;
      g.addVertex("DML") ;
      edge = g.addEdge("LVL", "DML") ;
      edge.setRoadName("McCarthy_Quad") ;
      edge.setDirection("N") ;
      edge.setFrom("DML") ;
      edge.setTo("LVL") ;
      g.setEdgeWeight(edge, 25) ;

      g.addVertex("ALM") ;
      g.addVertex("DML") ;
      edge = g.addEdge("ALM", "DML") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("DML") ;
      edge.setTo("ALM") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("DXM") ;
      g.addVertex("DML") ;
      edge = g.addEdge("DXM", "DML") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("DML") ;
      edge.setTo("DXM") ;
      g.setEdgeWeight(edge, 14) ;

      g.addVertex("MRF") ;
      g.addVertex("LVL") ;
      edge = g.addEdge("MRF", "LVL") ;
      edge.setRoadName("W_34St") ;
      edge.setDirection("N") ;
      edge.setFrom("LVL") ;
      edge.setTo("MRF") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("SWC") ;
      g.addVertex("LVL") ;
      edge = g.addEdge("SWC", "LVL") ;
      edge.setRoadName("W_34St") ;
      edge.setDirection("N") ;
      edge.setFrom("LVL") ;
      edge.setTo("SWC") ;
      g.setEdgeWeight(edge, 10) ;

      g.addVertex("BSR") ;
      g.addVertex("LVL") ;
      edge = g.addEdge("BSR", "LVL") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("LVL") ;
      edge.setTo("BSR") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("SWC") ;
      g.addVertex("MRF") ;
      edge = g.addEdge("SWC", "MRF") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("MRF") ;
      edge.setTo("SWC") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("PSD") ;
      g.addVertex("MRF") ;
      edge = g.addEdge("PSD", "MRF") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("MRF") ;
      edge.setTo("PSD") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("PSD") ;
      g.addVertex("SWC") ;
      edge = g.addEdge("PSD", "SWC") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("SWC") ;
      edge.setTo("PSD") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("PTD") ;
      g.addVertex("ALM") ;
      edge = g.addEdge("PTD", "ALM") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("ALM") ;
      edge.setTo("PTD") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("DXM") ;
      g.addVertex("ALM") ;
      edge = g.addEdge("DXM", "ALM") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("ALM") ;
      edge.setTo("DXM") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("PSX") ;
      g.addVertex("PTD") ;
      edge = g.addEdge("PSX", "PTD") ;
      edge.setRoadName("Hellman_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("PTD") ;
      edge.setTo("PSX") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("DXM") ;
      g.addVertex("PTD") ;
      edge = g.addEdge("DXM", "PTD") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("PTD") ;
      edge.setTo("DXM") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("PSX") ;
      g.addVertex("DXM") ;
      edge = g.addEdge("PSX", "DXM") ;
      edge.setRoadName("Hellman_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("DXM") ;
      edge.setTo("PSX") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("DMT") ;
      g.addVertex("DXM") ;
      edge = g.addEdge("DMT", "DXM") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("DXM") ;
      edge.setTo("DMT") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("TRO") ;
      g.addVertex("DXM") ;
      edge = g.addEdge("TRO", "DXM") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("DXM") ;
      edge.setTo("TRO") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("DMT") ;
      g.addVertex("TRO") ;
      edge = g.addEdge("DMT", "TRO") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("TRO") ;
      edge.setTo("DMT") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("RMH") ;
      g.addVertex("TRO") ;
      edge = g.addEdge("RMH", "TRO") ;
      edge.setRoadName("S_Figueroa_St") ;
      edge.setDirection("E") ;
      edge.setFrom("TRO") ;
      edge.setTo("RMH") ;
      g.setEdgeWeight(edge, 9) ;

      g.addVertex("FIG") ;
      g.addVertex("TRO") ;
      edge = g.addEdge("FIG", "TRO") ;
      edge.setRoadName("Hellman_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("TRO") ;
      edge.setTo("FIG") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("PSX") ;
      g.addVertex("DMT") ;
      edge = g.addEdge("PSX", "DMT") ;
      edge.setRoadName("Hellman_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("DMT") ;
      edge.setTo("PSX") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("RMH") ;
      g.addVertex("DMT") ;
      edge = g.addEdge("RMH", "DMT") ;
      edge.setRoadName("S_Figueroa_St") ;
      edge.setDirection("E") ;
      edge.setFrom("DMT") ;
      edge.setTo("RMH") ;
      g.setEdgeWeight(edge, 10) ;

      g.addVertex("NEW") ;
      g.addVertex("PSX") ;
      edge = g.addEdge("NEW", "PSX") ;
      edge.setRoadName("McCarthy_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("PSX") ;
      edge.setTo("NEW") ;
      g.setEdgeWeight(edge, 3) ;

      g.addVertex("NRC") ;
      g.addVertex("PSX") ;
      edge = g.addEdge("NRC", "PSX") ;
      edge.setRoadName("McCarthy_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("PSX") ;
      edge.setTo("NRC") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("FIG") ;
      g.addVertex("PSX") ;
      edge = g.addEdge("FIG", "PSX") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("PSX") ;
      edge.setTo("FIG") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("UGB") ;
      g.addVertex("PSX") ;
      edge = g.addEdge("UGB", "PSX") ;
      edge.setRoadName("S_Figueroa_St") ;
      edge.setDirection("E") ;
      edge.setFrom("PSX") ;
      edge.setTo("UGB") ;
      g.setEdgeWeight(edge, 10) ;

      g.addVertex("NRC") ;
      g.addVertex("FIG") ;
      edge = g.addEdge("NRC", "FIG") ;
      edge.setRoadName("McCarthy_Way") ;
      edge.setDirection("N") ;
      edge.setFrom("FIG") ;
      edge.setTo("NRC") ;
      g.setEdgeWeight(edge, 8) ;

      g.addVertex("PSD") ;
      g.addVertex("BSR") ;
      edge = g.addEdge("PSD", "BSR") ;
      edge.setRoadName("W_34Th") ;
      edge.setDirection("N") ;
      edge.setFrom("BSR") ;
      edge.setTo("PSD") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("NEW") ;
      g.addVertex("BSR") ;
      edge = g.addEdge("NEW", "BSR") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("BSR") ;
      edge.setTo("NEW") ;
      g.setEdgeWeight(edge, 4) ;

      g.addVertex("PSD") ;
      g.addVertex("NEW") ;
      edge = g.addEdge("PSD", "NEW") ;
      edge.setRoadName("W_34Th") ;
      edge.setDirection("N") ;
      edge.setFrom("NEW") ;
      edge.setTo("PSD") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("NRC") ;
      g.addVertex("NEW") ;
      edge = g.addEdge("NRC", "NEW") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("NEW") ;
      edge.setTo("NRC") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("DCC") ;
      g.addVertex("NRC") ;
      edge = g.addEdge("DCC", "NRC") ;
      edge.setRoadName("W_34Th") ;
      edge.setDirection("N") ;
      edge.setFrom("NRC") ;
      edge.setTo("DCC") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("GEC") ;
      g.addVertex("NRC") ;
      edge = g.addEdge("GEC", "NRC") ;
      edge.setRoadName("S_Figueroa_St") ;
      edge.setDirection("E") ;
      edge.setFrom("NRC") ;
      edge.setTo("GEC") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("DCC") ;
      g.addVertex("PSD") ;
      edge = g.addEdge("DCC", "PSD") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("PSD") ;
      edge.setTo("DCC") ;
      g.setEdgeWeight(edge, 5) ;

      g.addVertex("GEC") ;
      g.addVertex("DCC") ;
      edge = g.addEdge("GEC", "DCC") ;
      edge.setRoadName("S_Figueroa_St") ;
      edge.setDirection("E") ;
      edge.setFrom("DCC") ;
      edge.setTo("GEC") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("RMH") ;
      g.addVertex("TYL") ;
      edge = g.addEdge("RMH", "TYL") ;
      edge.setRoadName("Exposition_Blvd") ;
      edge.setDirection("N") ;
      edge.setFrom("TYL") ;
      edge.setTo("RMH") ;
      g.setEdgeWeight(edge, 7) ;

      g.addVertex("PS2") ;
      g.addVertex("TYL") ;
      edge = g.addEdge("PS2", "TYL") ;
      edge.setRoadName("Exposition_Blvd") ;
      edge.setDirection("N") ;
      edge.setFrom("TYL") ;
      edge.setTo("PS2") ;
      g.setEdgeWeight(edge, 6) ;

      g.addVertex("PS2") ;
      g.addVertex("RMH") ;
      edge = g.addEdge("PS2", "RMH") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("RMH") ;
      edge.setTo("PS2") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("UGB") ;
      g.addVertex("PS2") ;
      edge = g.addEdge("UGB", "PS2") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("PS2") ;
      edge.setTo("UGB") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("UPC") ;
      g.addVertex("GEC") ;
      edge = g.addEdge("UPC", "GEC") ;
      edge.setRoadName("101_Free_Way") ;
      edge.setDirection("E") ;
      edge.setFrom("GEC") ;
      edge.setTo("UPC") ;
      g.setEdgeWeight(edge, 24) ;

      g.addVertex("UPX") ;
      g.addVertex("ELB") ;
      edge = g.addEdge("UPX", "ELB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("N") ;
      edge.setFrom("ELB") ;
      edge.setTo("UPX") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("UPX") ;
      g.addVertex("ELB") ;
      edge = g.addEdge("UPX", "ELB") ;
      edge.setRoadName("Across") ;
      edge.setDirection("E") ;
      edge.setFrom("ELB") ;
      edge.setTo("UPX") ;
      g.setEdgeWeight(edge, 2) ;

      g.addVertex("CAL") ;
      g.addVertex("UPX") ;
      edge = g.addEdge("CAL", "UPX") ;
      edge.setRoadName("S_Grand_Ave") ;
      edge.setDirection("E") ;
      edge.setFrom("UPX") ;
      edge.setTo("CAL") ;
      g.setEdgeWeight(edge, 8) ;

   } catch (Exception e) {
      Log.e(getClass().getSimpleName(), e.getMessage(), e) ;
   }
}

//-------------------- END OF HARD-CODED createMap ----------------------

	// System.out.println(g.toString());

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

			// } catch (FileNotFoundException fnfe) {

			// } catch (NullPointerException npe) {
			// npe.printStackTrace();
		} catch (Exception e) {
			// Main.help();
			System.err.println("Please enter correct heuristic file name");
			// System.exit(0);
			// e.printStackTrace();
		}
		// System.out.println(heuristic.toString());

	}
}
