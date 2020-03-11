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

	public static void main(String[] args) {
		Object[] options = { "LEO", "SSO", "GEO", "Custom" };
		int n = JOptionPane.showOptionDialog(null, "Which type of orbit you would like to visualise?", "Inputs",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (n == 0) {
			// LEO chosen
			double a = 6378000+800000; // semi major axis in meters
			double e = 0.72831215; // eccentricity
			double i = FastMath.toRadians(7); // inclination
			double omega = FastMath.toRadians(180); // perigee argument
			double raan = FastMath.toRadians(261); // right ascension of ascending node
			double lM = 0; // mean anomaly

		} else if (n == 1) {
			// SSO chosen
			double a = 6378000+800000; // semi major axis in meters
			double e = 0.72831215; // eccentricity
			double i = FastMath.toRadians(89); // inclination
			double omega = FastMath.toRadians(180); // perigee argument
			double raan = FastMath.toRadians(261); // right ascension of ascending node
			double lM = 0; // mean anomaly

		} else if (n == 2) {
			// GEO chosen
			double a = 6378000+32000000; // semi major axis in meters
			double e = 0.72831215; // eccentricity
			double i = FastMath.toRadians(7); // inclination
			double omega = FastMath.toRadians(180); // perigee argument
			double raan = FastMath.toRadians(261); // right ascension of ascending node
			double lM = 0; // mean anomaly

		} else if (n == 3) {
			// Custom chosen
			double a = Dialogging.getNumInput("Semi-major axis (km):")*1000; //semi-major axis (m)
			double e = Dialogging.getNumInput("Eccentricity:"); //eccentricity
			double i = FastMath.toRadians(Dialogging.getNumInput("Inclination (degrees):")); // inclination
			double omega = FastMath.toRadians(Dialogging.getNumInput("Argument of perigee (degrees):")); // perigee argument
			double raan = FastMath.toRadians(Dialogging.getNumInput("Right Ascension of Ascending Node (degrees):")); // right ascension of ascending node
			double lM = FastMath.toRadians(Dialogging.getNumInput("Mean anomaly (degrees):")); // mean anomaly
			
			
			

		}
	}

}
