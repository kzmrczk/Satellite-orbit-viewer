/**
 * 
 */
package fr.isae.mae.ss;

import org.hipparchus.util.FastMath;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;

/**
 * @author c.mayer
 * following https://www.orekit.org/site-orekit-10.0/tutorial/propagation.html
 */
public class PropagationTutorial {
  public static void main(String[] args) {
    Frame inertialFrame = FramesFactory.getEME2000();
    TimeScale utc = TimeScalesFactory.getTAI();
    
    AbsoluteDate initialDate = new AbsoluteDate(2020, 01, 01, 23, 30, 00.000, utc);

    double mu =  3.986004415e+14;

    double a = 24396159;                     // semi major axis in meters
    double e = 0.72831215;                   // eccentricity
    double i = FastMath.toRadians(7);        // inclination
    double omega = FastMath.toRadians(180);  // perigee argument
    double raan = FastMath.toRadians(261);   // right ascension of ascending node
    double lM = 0;                           // mean anomaly
    
    Orbit initialOrbit = new KeplerianOrbit(a, e, i, omega, raan, lM, PositionAngle.MEAN,
        inertialFrame, initialDate, mu);
    
    KeplerianPropagator kepler = new KeplerianPropagator(initialOrbit);
    
    kepler.setSlaveMode();
    
    
    double duration = 600.;
    AbsoluteDate finalDate = initialDate.shiftedBy(duration);
    double stepT = 60.;
    int cpt = 1;
    for (AbsoluteDate extrapDate = initialDate;
         extrapDate.compareTo(finalDate) <= 0;
         extrapDate = extrapDate.shiftedBy(stepT))  {
        SpacecraftState currentState = kepler.propagate(extrapDate);
        System.out.println("step " + cpt++);
        System.out.println(" time : " + currentState.getDate().toString(utc));
        System.out.println(" " + currentState.getOrbit());
    }
  }
}
