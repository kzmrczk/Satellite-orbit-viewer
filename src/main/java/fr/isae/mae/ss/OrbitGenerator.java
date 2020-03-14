package fr.isae.mae.ss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;

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

import gov.nasa.worldwind.geom.Position;

/**
 * @authors Hamish McPhee, Dawid Kazimierczak
 * 
 * Class OrbitGenerator is in charge of generating the orbit. It has one method - pathPositions.
 * The method takes an array of orbital parameters: semi-major axis, eccentricity, inclination,
 * perigee argument, right ascension of ascending node, mean anomaly. It converts the parameters
 * to points in 3D space. The method returns an array containing these points described
 * in latitude, longitude and altitude. The pathPositions throws an exception if file containg
 * the Orekit data is not found.
 * 
 */
public class OrbitGenerator {
	
	public static ArrayList<Position> pathPositions(double[] in) throws FileNotFoundException {
	    
		// Number of steps in a simulation/Fineness of the orbit
	    int n = 100;
	    // Standard gravitational parameter for Earth [m3.s-2]
	    double mu =  3.986004415e+14;
	    // Orbit's semi-major axis [m]
	    double a = in[0];
	    // Orbit's eccentricity
	    double e = in[1];
	    // Orbit's inclination [rad]
	    double i = in[2];
	    // Orbit's perigee argument [rad]
	    double omega = in[3];
	    // Orbit's right ascension of ascending node [rad]
	    double raan = in[4];
	    // Orbit's mean anomaly [rad]
	    double lM = in[5];
	    // Specify the time duration of the simulation. Default: one orbital period
	    double duration = 2*FastMath.PI*FastMath.sqrt(FastMath.pow(a,3)/mu);
	    // Time duration of a step
	    double stepT = duration/n;
	    
	    // Define the initial date
	    TimeScale utc = TimeScalesFactory.getTAI();
	    AbsoluteDate initialDate = new AbsoluteDate(2020, 01, 01, 23, 30, 00.000, utc);
	    // Define the final date
	    AbsoluteDate finalDate = initialDate.shiftedBy(duration);
	    
	    // Initializing reference frame EME 2000
	    Frame inertialFrame = FramesFactory.getEME2000();
	    // Define the initial orbit based on input orbital parameters
	    Orbit initialOrbit = new KeplerianOrbit(a, e, i, omega, raan, lM, PositionAngle.MEAN,
	        inertialFrame, initialDate, mu);
	    // Define the propagator. Default case: Keplerian
	    KeplerianPropagator kepler = new KeplerianPropagator(initialOrbit);
	    
	    // Loading Orekit data needed to compute the orbit. AbsoluteFilePath looks for the zip file in the system
	    File orekitData = new File(AbsoluteFilePath.getPath("orekit-data-master.zip"));
	    //File orekitData = new File("C:/Users/username/Documents/orekit-data-master");
	    if (!orekitData.exists()) {
	        System.err.format(Locale.US, "You need to specify the location and name of the orekit-data-master file in OrbitGenerator.java",
	                          "orekit-data-master.zip", "https://gitlab.orekit.org/orekit/orekit-data/-/archive/master/orekit-data-master.zip");
	        System.exit(1);
	    }
	    
	    // Initializing class DataProvidersManager which is a point of access to loading Orekit data
	    DataProvidersManager manager = DataProvidersManager.getInstance();
	    // Adding provider for storing data files like .zip
	    manager.addProvider(new DirectoryCrawler(orekitData));
	    
	    // Initializing the array holding positions of the path
	    ArrayList<Position> pathPositions = new ArrayList<Position>();
	    // Initializing the Earth's body as OneAxisEllipsoid
	    OneAxisEllipsoid earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                Constants.WGS84_EARTH_FLATTENING,
                FramesFactory.getITRF(IERSConventions.IERS_2010, false));
	    
	    // Opening a file to save data
    	FileOutputStream dataFile = new FileOutputStream("dataFile.txt");
    	// Creating a PrintWriter to print into file
		PrintWriter save = new PrintWriter(dataFile);
    	// Printing the first lines
		save.println("Initial orbital parameters:");
		save.format("%10s %10s %10s %10s %10s %10s\n","Semi-major axis[km]","Eccentricity","Inclination[deg]","Perigee argument[deg]","Right ascension of ascending node[deg]","Mean anomaly[deg]");
		save.format("%10.4f %10.4f %10.4f %10.4f %10.4f %10.4f\n",a/1000,e,Math.toDegrees(i),Math.toDegrees(omega),Math.toDegrees(raan),Math.toDegrees(lM));
		save.println("-----------------------------------------------------------------------------");
    	save.format("%10s %10s %10s %10s\n","Time","Latitude[deg]","Longitude[deg]","Altitude[km]");
    	
    	// Loop: propagation of the orbit
	    for (AbsoluteDate extrapDate = initialDate;
	        extrapDate.compareTo(finalDate) <= 0;
	        extrapDate = extrapDate.shiftedBy(stepT))  {
	    	
	    	// Propagation of the orbit by one time step
	        SpacecraftState currentState = kepler.propagate(extrapDate);
	        // Current time and date
	        AbsoluteDate date = currentState.getDate();
	        // Coordinates of position, velocity an attitude of spacecraft in cartesian space
	        PVCoordinates pvInert = currentState.getPVCoordinates();
	        // Transformation vector in 3D space between Earth's body frame and spacecraft's frame
	        Transform t = currentState.getFrame().getTransformTo(earth.getBodyFrame(), date);
	        // Transformation of spacecraft's position into vector
	        Vector3D p = t.transformPosition(pvInert.getPosition());
	        // Transformation of spacecraft position in Earth's frame to (lat, long, alt)
	        GeodeticPoint pos = earth.transform(p, earth.getBodyFrame(), date);
	        
	        // Adding position to the array
	        pathPositions.add(Position.fromRadians(pos.getLatitude(),pos.getLongitude(),pos.getAltitude()));
	        // Adding state data to the array
	        save.format("%10s %10.4f %10.4f %10.4f\n",currentState.getDate().toString(utc),(pos.getLatitude()*180/3.14),
	        		(pos.getLongitude()*180/3.14),pos.getAltitude()/1000);
	    }
	    
	    // Closing the file
	    save.close();
	    
	    return pathPositions;		
	}
}
