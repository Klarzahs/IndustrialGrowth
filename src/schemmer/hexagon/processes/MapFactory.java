package schemmer.hexagon.processes;

import schemmer.hexagon.game.Main;
import schemmer.hexagon.type.HexTypeInt;
import schemmer.hexagon.type.Hexagon;
import schemmer.hexagon.utils.Cube;

public class MapFactory {
	
	private static String SEED_MAP = "77223344";
	
	private static float[][] noise;
	
	public static void createTypes(Main m, Hexagon[][] map, int radius){
		createMap(m, map, radius);
//		SimplexNoise.updateGradients(SEED_MAP);
//		noise = MapFactory.generateSimplexNoise(SimplexNoise.size*2, SimplexNoise.size*2, 0.006f);
//		int pixelPerHex = (SimplexNoise.size*2) / (map.length);
//		for(int x = 0; x < map.length; x++){
//			for (int y = 0; y < map[x].length; y++){
//				if(map[x][y] != null){
//					float hexNoise = hexToNoise(x, y, pixelPerHex);
	}
	
	//Simpson-rule integration at .25, .5, .75
	private static float hexToNoise(int x, int y, int pixelPerHex){
		float sumNoise = 0;
		x *= pixelPerHex;
		y *= pixelPerHex;
		sumNoise += 1*noise[(int)(x + 0.25f*pixelPerHex)][(int)(y + 0.25f*pixelPerHex)];
		sumNoise += 4*noise[(int)(x + 0.5f*pixelPerHex)][(int)(y + 0.5f*pixelPerHex)];
		sumNoise += 1*noise[(int)(x + 0.75f*pixelPerHex)][(int)(y + 0.75f*pixelPerHex)];
		sumNoise /= 6;
		return sumNoise;
	}
	
	private static void createMap(Main m, Hexagon[][] map, int radius){
		for (int q = -radius; q <= radius; q++) {
			int r1 = Math.max(-radius, -q - radius);
		    int r2 = Math.min(radius, -q + radius);
		    for (int r = r1; r <= r2; r++) {
		    	int x = r + radius;
		    	int y = q + radius + Math.min(0, r);
		    	map[x][y] = new Hexagon(m, new Cube(q, -q-r, r), x, y);
		    }
		}
	}
	
	public static float[][] generateSimplexNoise(int width, int height, float scale){
	    float[][] simplexnoise = new float[width][height];
	    
	    for(int x = 0; x < width; x++){
	       for(int y = 0; y < height; y++){
	          simplexnoise[x][y] = SimplexNoise.octavedNoise(x, y, scale); 	//(x * frequency,y * frequency);
	          simplexnoise[x][y] = (simplexnoise[x][y] + 1) / 2;   //generate values between 0 and 1
	       }
	    }
	    
	    return simplexnoise;
	}
	
	public static String getMapSeed(){
		return SEED_MAP;
	}
	
	public static void setMapSeed(String s){
		SEED_MAP = s;
	}
}
