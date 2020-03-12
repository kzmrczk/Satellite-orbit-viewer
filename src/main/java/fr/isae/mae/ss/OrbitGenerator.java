package fr.isae.mae.ss;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
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

import gov.nasa.worldwind.geom.Position;

public class OrbitGenerator {
	
	public static ArrayList<Position> pathPositions(double[] in) {
		Frame inertialFrame = FramesFactory.getEME2000();
	    TimeScale utc = TimeScalesFactory.getTAI();
	    
	    AbsoluteDate initialDate = new AbsoluteDate(2020, 01, 01, 23, 30, 00.000, utc);

	    double mu =  3.986004415e+14;

	    double a = in[0];                     // semi major axis in meters
	    double e = in[1];                   // eccentricity
	    double i = in[2];        // inclination
	    double omega = in[3];  // perigee argument
	    double raan = in[4];   // right ascension of ascending node
	    double lM = in[5];                           // mean anomaly
	    
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
}
