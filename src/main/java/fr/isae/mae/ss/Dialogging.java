package fr.isae.mae.ss;

import javax.swing.JOptionPane;

import org.hipparchus.util.FastMath;

public class Dialogging {
	
	public static double getNumInput(String param) {
		String s = (String) JOptionPane.showInputDialog(null, param + "\n", "Customized Dialog",
			JOptionPane.PLAIN_MESSAGE, null, null, "");
		double a = -1;

		try {
			a = Double.parseDouble(s);
		} 
		catch (NumberFormatException e) {
			while (a < 0) {
				String s2 = (String) JOptionPane.showInputDialog(null,
					"Please enter a number! " + param +"\n", "Customized Dialog",
					JOptionPane.PLAIN_MESSAGE, null, null, "");
//			try {
				a = Integer.parseInt(s2);
//			} catch (NumberFormatException e2) {
////				System.out.println("Run the program again when you decide to enter the requested format");
//				break;
//			}
			}

		}

		switch (param) {
			case "Eccentricity:":
				if (a < 0) {
					System.out.println("Eccentricity should be positive. Circular orbits: e = 0, Elliptical orbits: 0 < e < 1, Parabolic orbits: e = 1, Hyberbolic orbits: e > 1 ");
					getNumInput("Eccentricity:");
				}
		}
	
		return a;
	}

	//public static void main(String[] args) {
	public static double[] main() {
		double[] out = new double[6];
		double a=0;
		double e=0;
		double i=0;
		double omega=0;
		double raan=0;
		double lM=0;
		
		Object[] options = { "LEO", "SSO", "GEO", "Custom" };
		int n = JOptionPane.showOptionDialog(null, "Which type of orbit you would like to visualise?", "Inputs",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (n == 0) {
			// LEO chosen
			a = 6378000+800000; // semi-major axis in meters
			e = 0.72831215; // eccentricity
			i = FastMath.toRadians(7); // inclination
			omega = FastMath.toRadians(180); // perigee argument
			raan = FastMath.toRadians(261); // right ascension of ascending node
			lM = 0; // mean anomaly

		} else if (n == 1) {
			// SSO chosen
			a = 6378000+800000; // semi major axis in meters
			e = 0.72831215; // eccentricity
			i = FastMath.toRadians(89); // inclination
			omega = FastMath.toRadians(180); // perigee argument
			raan = FastMath.toRadians(261); // right ascension of ascending node
			lM = 0; // mean anomaly

		} else if (n == 2) {
			// GEO chosen
			a = 6378000+32000000; // semi major axis in meters
			e = 0.72831215; // eccentricity
			i = FastMath.toRadians(7); // inclination
			omega = FastMath.toRadians(180); // perigee argument
			raan = FastMath.toRadians(261); // right ascension of ascending node
			lM = 0; // mean anomaly

		} else if (n == 3) {
			// Custom chosen
			a = Dialogging.getNumInput("Semi-major axis (km):")*1000; //semi-major axis (m)
			e = Dialogging.getNumInput("Eccentricity:"); //eccentricity
			i = FastMath.toRadians(Dialogging.getNumInput("Inclination (degrees):")); // inclination
			omega = FastMath.toRadians(Dialogging.getNumInput("Argument of perigee (degrees):")); // perigee argument
			raan = FastMath.toRadians(Dialogging.getNumInput("Right Ascension of Ascending Node (degrees):")); // right ascension of ascending node
			lM = FastMath.toRadians(Dialogging.getNumInput("Mean anomaly (degrees):")); // mean anomaly
		}
		
		out[0] = a;
		out[1] = e;
		out[2] = i;
		out[3] = omega;
		out[4] = raan;
		out[5] = lM;
		
		return out;
	}

}
