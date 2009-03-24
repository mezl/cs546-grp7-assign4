/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csci561_hw1_search;
import org.jgrapht.graph.*;
/**
 *
 * @author mezl
 */
public class RoadEdge extends DefaultWeightedEdge{
    private String roadName;
    private String direction;
    private String from;
    private String to;

    public RoadEdge(String roadName, String direction, String from, String to) {
        super();
        this.roadName = roadName;
        this.direction = direction;
        this.from = from;
        this.to = to;        
    }
    public RoadEdge(){
    super();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
    public String getDirection(String to_city){
        if(to_city.equals(to))
            return direction;
        else
            return reverse(direction);
    }
    private String reverse(String dir){
        if(dir.equals("W"))
            return "E";
        if(dir.equals("E"))
            return "W";        
        if(dir.equals("N"))
            return "S";        
        else
            return "N";        
    }
}
