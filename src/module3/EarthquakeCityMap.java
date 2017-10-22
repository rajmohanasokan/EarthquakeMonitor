package module3;

//Java utilities libraries
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;
import processing.core.PShape;
//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.EsriProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.providers.Yahoo;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Rajmohan Asokan
 * Date: August 20, 2017
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;
	
	public final int YELLOW = color(255, 255, 0);
	public final int BLUE = color(0, 0, 255);
	public final int RED = color(255, 0, 0);
	

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new EsriProvider.WorldStreetMap());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    /*if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }*/
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    
	    
	    
	    //TODO: Add code here as appropriate
	    for(PointFeature feature : earthquakes) {
	    	markers.add(createMarker(feature));
	    }
	    map.addMarkers(markers);
	    
	}
	
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.
		
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		float magnitude = Float.parseFloat(feature.getProperty("magnitude").toString());
		if(magnitude > THRESHOLD_MODERATE) {
			marker.setColor(RED);
			marker.setRadius(15);
		}
		else if(magnitude < THRESHOLD_MODERATE && magnitude > THRESHOLD_LIGHT) {
			marker.setColor(YELLOW);
			marker.setRadius(13);
		}
		else {
			marker.setColor(BLUE);
			marker.setRadius(10);
		}
		return marker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		PShape s;
		s = createShape();
		s.beginShape();
		s.fill(211,211,211);
		s.noStroke();
		s.vertex(10, 50);
		s.vertex(10, 350);
		s.vertex(170, 350);
		s.vertex(170, 50);
		s.endShape(CLOSE);
		shape(s, 10, 10);
		textSize(17);
		fill(10);
		text("Earthquake key", 50, 100); 
		fill(255, 0, 0);
		ellipse(50, 150, 15, 15);
		fill(10);
		textSize(12);
		text("5.0+ Magnitude", 70, 155);
		fill(YELLOW);
		ellipse(50, 200, 13, 13);
		fill(10);
		textSize(12);
		text("4.0+ Magnitude", 70, 205);
		fill(BLUE);
		ellipse(50, 250, 10, 10);
		fill(10);
		textSize(12);
		text("Below 4.0", 70, 255);
	
	}
}
