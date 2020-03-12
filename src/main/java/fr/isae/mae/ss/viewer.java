package fr.isae.mae.ss;

import java.util.ArrayList;

import gov.nasa.worldwind.geom.Position;

public class viewer {
	static double a = 1;
	static double[] in = new double[6];
	static ArrayList<Position> pathPositions = new ArrayList<Position>();
	
	public viewer() {
		
	}
	
	public static void main(String[] args) {
		in = Dialogging.main();
		pathPositions = OrbitGenerator.pathPositions(in);
		
	}

}