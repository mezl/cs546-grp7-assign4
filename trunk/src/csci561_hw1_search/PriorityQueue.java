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

 *    @file: PriorityQueue.java
 *  @author: Chin-Kai Chang <chinkaic@usc.edu> 
 * @Purpose: A simple priority queue impelemented by heap

 */
import java.util.*;

public class PriorityQueue {
	private Vector<Node> node_queue;
	public PriorityQueue() {
		node_queue = new Vector<Node>();
	}
	private void upHeap(int ci) {
		if (ci == 0) return;//reach root
		int pi = (ci - 1) / 2;

		Node child = node_queue.elementAt(ci);
		Node parent = node_queue.elementAt(pi);
                try{
//                 //   System.out.println("Child Cost = "+child.getCost());
		if (child.getCost()<parent.getCost()) {
			node_queue.setElementAt(parent, ci);
			node_queue.setElementAt(child, pi);
			upHeap(pi);
		}
                }catch(Exception e){
                    e.printStackTrace();
                }
	}
	private void downHeap(int pi) {
		int ci = (2 * pi) + 1;
		if (ci >= node_queue.size()) return;//reach root
		Node child = node_queue.elementAt(ci);
		if (ci + 1 < node_queue.size()) {
			Node right = node_queue.elementAt(ci + 1);
			if (right.getCost()< child.getCost() ) {
				child = right;
				ci++;
			}
		}
		Node parent = node_queue.elementAt(pi);
		if (child.getCost()<parent.getCost() ) {
			node_queue.setElementAt(parent, ci);
			node_queue.setElementAt(child, pi);
			downHeap(ci);
		}
	}

	public void addElement(Node n) {
		node_queue.addElement(n);
		upHeap(node_queue.size() - 1);
	}
	public boolean hasMoreElements() {
		return (node_queue.size() > 0);
	}
	public Node removeElement() throws ArrayIndexOutOfBoundsException {
		Node topNode = node_queue.elementAt(0);
		Node lastNode = node_queue.elementAt(node_queue.size() - 1);
		node_queue.removeElementAt(node_queue.size() - 1);

		if (node_queue.size() > 0) {
			node_queue.setElementAt(lastNode, 0);
			downHeap(0);
		}
		return topNode;
	}
        public String toString(boolean heuristic){
            String output = "";
            for(int i = 0;i<node_queue.size();i++)
                output += node_queue.elementAt(i).toString(heuristic)+" ";
            return "{"+output+"}";        
            //return "{"+output.substring(0, output.length()-1)+"}";        
        }        
}

