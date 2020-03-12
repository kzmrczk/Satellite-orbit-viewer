package fr.isae.mae.ss;

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
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

public class viewer extends ApplicationTemplate {
	static double a = 1;
	static double[] in = new double[6];
	static ArrayList<Position> pathPositions = new ArrayList<Position>();
	
	@SuppressWarnings("serial")
	public static class DisplayPaths extends ApplicationTemplate.AppFrame
    {
        public DisplayPaths()
        {
            super(false, false, false);
            // Add a dragger to enable shape dragging
            this.getWwd().addSelectListener(new BasicDragger(this.getWwd()));
            RenderableLayer layer = new RenderableLayer();

            // Create a path with more than two positions and closed.
            ArrayList<Position> pathPositions = OrbitGenerator.pathPositions(Dialogging.main());
            Path path = new Path(pathPositions);
            path.setAltitudeMode(WorldWind.ABSOLUTE);
            path.setExtrude(false);
            path.setPathType(AVKey.LINEAR);

            // Create and set an attribute bundle.
            ShapeAttributes attrs = new BasicShapeAttributes();
            attrs.setOutlineMaterial(new Material(WWUtil.makeRandomColor(null)));
            attrs.setInteriorMaterial(new Material(WWUtil.makeRandomColor(null)));
            attrs.setOutlineWidth(2);
            path.setAttributes(attrs);

            layer.addRenderable(path);

            // Add the layer to the model.
            insertBeforeCompass(getWwd(), layer);
        }
        
        
    }
	
	public static void main(String[] args) {
		//in = Dialogging.main();
		
		ApplicationTemplate.start("World Wind Paths", DisplayPaths.class);
	}
}