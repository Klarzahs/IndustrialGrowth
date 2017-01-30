package schemmer.hexagon.type;

import java.awt.Color;

public class HexType {
	private HexTypeInt index;
	private HexTypeColor color;
	
	public HexType(int i){
		index = HexTypeInt.values()[i];
		color = HexTypeColor.values()[i];
	}
	
	public Color getColor(){
		return color.getColor();
	}
	
	public int getIndex(){
		return index.getValue();
	}
	
	public int getImage(){
		switch (index){
		default:
			return 0;
		}
	}
	
}