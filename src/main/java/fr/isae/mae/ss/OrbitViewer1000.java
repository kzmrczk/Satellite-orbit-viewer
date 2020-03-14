package fr.isae.mae.ss;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.util.BasicDragger;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

/**
 * 
 * @author Dawid Kazimierczak
 * 
 * SatelliteOrbitViewer is a class that extends ApplicationTemplate and it is the main
 * class of the software SatelliteOrbitViewer1000. It uses other classes.
 *
 */

public class OrbitViewer1000 extends ApplicationTemplate {
	
	// Initializing the array holding positions of the path
	static ArrayList<Position> pathPositions = new ArrayList<Position>();
	
	@SuppressWarnings("serial")
	public static class DisplayPaths extends ApplicationTemplate.AppFrame
    {
        public DisplayPaths() throws FileNotFoundException
        {
        	// Specify whether to include: (Status bar, Layer panel, Statistics panel)
            super(true, true, false);
            // Add a dragger to enable shape dragging
            this.getWwd().addSelectListener(new BasicDragger(this.getWwd()));
            RenderableLayer layer = new RenderableLayer();

            // Create a path with more than two positions and closed
            ArrayList<Position> pathPositions = OrbitGenerator.pathPositions(Dialogging.main());
            Path path = new Path(pathPositions);
            // Set altitude mode. Possible modes: ABSOLUTE, RELATIVE_TO_GROUND, CLAMP_TO_GROUND
            path.setAltitudeMode(WorldWind.ABSOLUTE);
            // Set path type. Possible types: LINEAR, GREAT_CIRCLE, RHUMB_LINE
            path.setPathType(AVKey.LINEAR);

            // Create and set an attribute bundle
            ShapeAttributes attrs = new BasicShapeAttributes();
            // Set color of the orbit
            attrs.setOutlineMaterial(new Material(Color.RED));
            // Set width of the orbit's line
            attrs.setOutlineWidth(5);
            // Assign attributes to the path
            path.setAttributes(attrs);
            // Add the path to the layer
            layer.addRenderable(path);
            // Add the layer to the model
            insertBeforeCompass(getWwd(), layer);
        }        
    }
	
	public static void main(String[] args) {
		// Display the orbit model
		start("OrbitViewer1000", DisplayPaths.class);
	}
}