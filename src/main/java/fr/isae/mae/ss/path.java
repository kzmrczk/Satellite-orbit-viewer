package fr.isae.mae.ss;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.render.markers.*;
import gov.nasa.worldwind.util.*;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.io.File;
import java.util.*;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.Transform;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinates;

/**
 * Example of {@link Path} usage. A Path is a line or curve between positions. The path may follow terrain, and may be
 * turned into a curtain by extruding the path to the ground.
 *
 * @author Dawid Kazimierczak
 * @version $Id: Paths.java 2292 2014-09-02 21:13:05Z tgaskins $
 */

public class path extends ApplicationTemplate
{
	//public static ArrayList<Position> pathPositions;

	public static ArrayList<Position> function()
	{
		Frame inertialFrame = FramesFactory.getEME2000();
	    TimeScale utc = TimeScalesFactory.getTAI();
	    
	    AbsoluteDate initialDate = new AbsoluteDate(2020, 01, 01, 23, 30, 00.000, utc);

	    double mu =  3.986004415e+14;

	    double a = 7000000;                     // semi major axis in meters
	    double e = 0.;                   // eccentricity
	    double i = FastMath.toRadians(0);        // inclination
	    double omega = FastMath.toRadians(180);  // perigee argument
	    double raan = FastMath.toRadians(261);   // right ascension of ascending node
	    double lM = 0;                           // mean anomaly
	    
	    Orbit initialOrbit = new KeplerianOrbit(a, e, i, omega, raan, lM, PositionAngle.MEAN,
	        inertialFrame, initialDate, mu);
	    
	    KeplerianPropagator kepler = new KeplerianPropagator(initialOrbit);
	    
	    kepler.setSlaveMode();
	    
	    File orekitData = new File("C:\\Users\\Dawid Kazimierczak\\Documents\\ISAE\\Space Project Tools for Simulation");
	    if (!orekitData.exists()) {
	        System.err.format(Locale.US, "You need to download the zip and rename it 'orekit-data', then specify the location in Propagation2LatLonAlt.java",
	                          "orekit-data-master.zip", "https://gitlab.orekit.org/orekit/orekit-data/-/archive/master/orekit-data-master.zip");
	        System.exit(1);
	    }
	    DataProvidersManager manager = DataProvidersManager.getInstance();
	    manager.addProvider(new DirectoryCrawler(orekitData));
	    
	    double duration = 86400.;
	    AbsoluteDate finalDate = initialDate.shiftedBy(duration); //time shift in seconds
	    double stepT = 100.;
	    int cpt = 1;
	    

	    ArrayList<Position> pathPositions = new ArrayList<Position>();

	    for (AbsoluteDate extrapDate = initialDate;
	        extrapDate.compareTo(finalDate) <= 0;
	        extrapDate = extrapDate.shiftedBy(stepT))  {
	        SpacecraftState currentState = kepler.propagate(extrapDate);
	        
	        OneAxisEllipsoid earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
	                Constants.WGS84_EARTH_FLATTENING,
	                FramesFactory.getITRF(IERSConventions.IERS_2010, false));
	        
	        AbsoluteDate  date    = currentState.getDate();
	        PVCoordinates pvInert = currentState.getPVCoordinates();
	        Transform     t       = currentState.getFrame().getTransformTo(earth.getBodyFrame(), date);
	        Vector3D      p       = t.transformPosition(pvInert.getPosition());
	        //Vector3D      v       = t.transformVector(pvInert.getVelocity()); not used currently but maybe useful for further applications
	        GeodeticPoint pos  = earth.transform(p, earth.getBodyFrame(), date);
	        
	        //Printing each step
	        /*System.out.println("step " + cpt++);
	        System.out.println(" time : " + currentState.getDate().toString(utc));
	        System.out.println(" " + currentState.getOrbit());
	        System.out.println(" LAT " + (pos.getLatitude()*180/3.14));
	        System.out.println(" LON " + (pos.getLongitude()*180/3.14));
	        System.out.println(" ALT " + pos.getAltitude());*/
	        
	        //Plotting the path
	        //pathPositions.add(pvInert);
	        pathPositions.add(Position.fromRadians(pos.getLatitude(),pos.getLongitude(),pos.getAltitude()));
	    }
	        return pathPositions;
	}
	@SuppressWarnings("serial")
	public static class paths extends ApplicationTemplate.AppFrame
    {
        public paths()
        {
            super(false, false, false);
            // Add a dragger to enable shape dragging
            //this.getWwd().addSelectListener(new BasicDragger(this.getWwd()));
            RenderableLayer layer = new RenderableLayer();

            // Create a path with more than two positions and closed.
            ArrayList<Position> pathPositions = function();
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

            /*List<Marker> markers = new ArrayList<Marker>(1);
            markers.add(new BasicMarker(Position.fromDegrees(90, 0), new BasicMarkerAttributes()));
            MarkerLayer markerLayer = new MarkerLayer();
            markerLayer.setMarkers(markers);
            insertBeforeCompass(getWwd(), markerLayer);*/
        }
    }
	
    public static void main(String[] args)
    {
        ApplicationTemplate.start("World Wind Paths", paths.class);
    }
}
