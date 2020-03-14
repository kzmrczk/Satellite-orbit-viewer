package fr.isae.mae.ss;

import javax.swing.JOptionPane;

import org.hipparchus.util.FastMath;

/**
 * 
 * @author Hamish McPhee
 *
 */
public class Dialogging {
	
	// Method for obtaining numerical inputs from users through a popup dialog. 
	// The input is the string corresponding to which parameter is being asked for, output is the double representing the parameter
	public static double getNumInput(String param) {
		String s = (String) JOptionPane.showInputDialog(null, param + "\n", "Orbital parameters",
			JOptionPane.PLAIN_MESSAGE, null, null, "");
		double a = -1;
		// need to check if the user put an appropriate input
		try {
			a = Double.parseDouble(s);
		} 
		// in case user enters a non-number input, they are prompted to try again with the correct number format
		catch (NumberFormatException e) {
				String s2 = (String) JOptionPane.showInputDialog(null,
					"Please enter a number! " + param +"\n", "Orbital parameters",
					JOptionPane.PLAIN_MESSAGE, null, null, "");
				a = Integer.parseInt(s2);
				//if wrong input is entered again, exception is thrown and program terminates
		}
		//switch created to introduce specific limitations on each orbital paramter case e.g. no negative eccentricity
		switch (param) {
			case "Eccentricity:":
				if (a < 0) {
					System.out.println("Eccentricity should be positive. Circular orbits: e = 0, Elliptical orbits: 0 < e < 1, Parabolic orbits: e = 1, Hyberbolic orbits: e > 1 ");
					getNumInput("Eccentricity:");
				}
		}
	
		return a;
	}

	public static double[] main() {
		double[] out = new double[6];
		double a=0;
		double e=0;
		double i=0;
		double omega=0;
		double raan=0;
		double lM=0;
		
		//initial dialog box opens to allow user to choose a predefined orbit type or enter custom parameters
		Object[] options = { "LEO", "SSO", "GEO", "Custom" };
		int n = JOptionPane.showOptionDialog(null, "Which type of orbit you would like to visualise?", "Inputs",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (n == 0) {
			// LEO chosen
			a = 6378000+800000; // semi-major axis in meters for 800 km altitude
			e = 0; // eccentricity
			i = FastMath.toRadians(7); // inclination
			omega = FastMath.toRadians(180); // perigee argument
			raan = FastMath.toRadians(261); // right ascension of ascending node
			lM = 0; // mean anomaly

		} else if (n == 1) {
			// SSO chosen
			a = 6378000+800000; // semi major axis in meters for 800 km altitude
			e = 0; // eccentricity
			i = FastMath.toRadians(89); // inclination for approximately sun-synchronous
			omega = FastMath.toRadians(180); // perigee argument
			raan = FastMath.toRadians(261); // right ascension of ascending node
			lM = 0; // mean anomaly

		} else if (n == 2) {
			// GEO chosen
			a = 42241080; // semi major axis in meters (calculated for an orbital period of 24 hours
			e = 0; // eccentricity
			i = FastMath.toRadians(0); // inclination
			omega = FastMath.toRadians(0); // perigee argument
			raan = FastMath.toRadians(0); // right ascension of ascending node
			lM = 0; // mean anomaly

		} else if (n == 3) {
			// Custom chosen
			// Method getNumInput is called for each of the 6 orbital parameters to get custom inputs
			a = Dialogging.getNumInput("Semi-major axis (km):")*1000; //semi-major axis (m)
			e = Dialogging.getNumInput("Eccentricity:"); //eccentricity
			i = FastMath.toRadians(Dialogging.getNumInput("Inclination (degrees):")); // inclination
			omega = FastMath.toRadians(Dialogging.getNumInput("Argument of perigee (degrees):")); // perigee argument
			raan = FastMath.toRadians(Dialogging.getNumInput("Right Ascension of Ascending Node (degrees):")); // right ascension of ascending node
			lM = FastMath.toRadians(Dialogging.getNumInput("Mean anomaly (degrees):")); // mean anomaly
		}
		
		//assign chosen orbital parameters to the output of main, for use in viewer.java
		out[0] = a;
		out[1] = e;
		out[2] = i;
		out[3] = omega;
		out[4] = raan;
		out[5] = lM;
		
		return out;
	}

}
