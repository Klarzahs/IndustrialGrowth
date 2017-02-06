package schemmer.hexagon.handler;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import schemmer.hexagon.addition.AdditionFactory;
import schemmer.hexagon.game.Main;
import schemmer.hexagon.game.Screen;
import schemmer.hexagon.player.Player;
import schemmer.hexagon.processes.MapFactory;
import schemmer.hexagon.type.HexTypeInt;
import schemmer.hexagon.type.Hexagon;
import schemmer.hexagon.utils.Conv;
import schemmer.hexagon.utils.Cube;
import schemmer.hexagon.utils.Log;
import schemmer.hexagon.utils.Point;

public class MapHandler {
	private Main main;
	private Screen screen;
	
	public int RADIUS = 100;
	public boolean DEBUG = false;
	
	private static final boolean SHOW_ALL_HEX = false;
	
	private Hexagon[][] map;		//Range: 0..2*radius+1, but hex coords are range: -radius .. radius
	
	private Hexagon marked;
	private Hexagon hovered;
	private Point clicked;
	
	private boolean mapLoaded = false;
	
	public MapHandler(Main main){
		this.main = main;
		createHexagon(RADIUS);
	}
	
	public void update(double delta){
		
	}
	
	public void draw(Graphics2D g2d, int offX, int offY){
		g2d.setColor(new Color(150,150,150));
		g2d.setStroke(new BasicStroke(1));
		
		for (int q = map.length - 1 ; q >= 0 ; q--){		//draw backwards for overlapping images
			for (int r = map[q].length - 1; r >= 0; r--){
				if(map[q][r] != null){
					if(SHOW_ALL_HEX){
						map[q][r].drawOutline(g2d, offX, offY);
					}
					if(map[q][r].exists()){
						map[q][r].fill(g2d, offX, offY);
						map[q][r].fillWithTypeColor(g2d, offX, offY);
						map[q][r].drawPicture(g2d, offX, offY);
						map[q][r].drawOutline(g2d, offX, offY);
					}
				}
			}
		}
		
		if(hovered != null){
		}
		if(marked != null){
		}
		UIHandler uih = Main.instance.getUIH();
		if(uih.isMarked()){
			ArrayList<Hexagon> slots = Main.instance.getRH().getCurrentPlayer().getCurrentHexagons();
			Iterator<Hexagon> it = slots.iterator();
			while(it.hasNext()){
				Hexagon h = it.next();
				for(int i = 0; i < Hexagon.CORNERS; i++){
					if(h.neighbour(i).getType() == null){
						h.neighbour(i).drawSlot(g2d, offX, offY);
					}
				}
			}
		}
		
	}
	
	public Hexagon getInArray(Cube c){
		int q = c.getV()[2] + RADIUS;
		int r = c.getV()[0] + RADIUS + Math.min(c.getV()[2],  0);
		if(q < 0 || q > 2 * RADIUS || r < 0 || r > 2 * RADIUS) return null;
		return map[q][r];
	}
	
	public int[] getAsArray(Cube c){
		int q = c.getV()[2] + RADIUS;
		int r = c.getV()[0] + RADIUS + Math.min(c.getV()[2],  0);
		if(q < 0 || q > 2 * RADIUS || r < 0 || r > 2 * RADIUS) return null;
		int [] arr = new int[2];
		arr[0] = q;
		arr[1] = r;
		return arr;
	}
	
	public void createHexagon(int radius){
		map = new Hexagon[2*radius+1][2*radius+1];
		
		MapFactory.createTypes(main, map, radius);
		marked = null;
		hovered = null;
	}
	
	public void setMarked(Cube c){
		marked = this.getInArray(c);
	}
	
	public boolean isMarked(){
		return (marked != null);
	}
	
	public void setHovered(Cube c){
		hovered = this.getInArray(c);
	}
	
	public void setMarked(MouseEvent e){
		clicked = new Point(e.getX(), e.getY());
		screen.setDebug("Clicked @"+ clicked.getX()+" | "+clicked.getY());
		Cube c = Conv.pointToCube(clicked.getX()+screen.getOffX(), clicked.getY()+screen.getOffY(), screen);
		this.setMarked(c);
	}
	
	public void setHovered(MouseEvent e){
		clicked = new Point(e.getX(), e.getY());
		this.setHovered(Conv.pointToCube(clicked.getX()+screen.getOffX(), clicked.getY()+screen.getOffY()));
	}
	
	public void recreate(int newRadius){
		RADIUS = newRadius;
		createHexagon(RADIUS);
		screen.calculateOffsets();
	}
		
	public void addScreen(){
		screen = main.getGUI().getScreen();
	}
	
	public Hexagon getMarked(){
		return marked;
	}
	
	public boolean isHovered(){
		return (hovered != null);
	}
	
	public Hexagon getHovered(){
		return hovered;
	}
	
	public Hexagon[][] getMap(){
		return map;
	}
	
	public void resetMarked(){
		marked = null;
	}
	
	public Main getMain(){
		return main;
	}
	
	public String getMapAsChar(){
		String result = "";
		for(int r = 0; r < map.length; r++){ //row
			for(int c = 0; c < map[r].length; c++){ //column
				if(map[r][c] != null)
					result += map[r][c].getAsChar()+".";
				else
					result += " ";
			}
			result += ",";
		}
		return result;
	}
	
	public void printMap(){
		for(int r = 0; r < map.length; r++){
			for(int c = 0; c < map[r].length; c++){
				if(map[r][c] != null)
					System.out.print(map[r][c].getAsChar() + " ");
				else
					System.out.print("  ");
			}
			System.out.println();
		}
	}
	
	public void setMapLoaded(){
		Log.d("Map loaded = true");
		mapLoaded = true;
	}
	
	public boolean mapLoaded() {
		return mapLoaded;
	}
	
	public void handleLeftClick(MouseEvent e) {
		UIHandler uih = Main.instance.getUIH();
		if(uih.isMarked()){
			clicked = new Point(e.getX(), e.getY());
			Cube c = Conv.pointToCube(clicked.getX()+screen.getOffX(), clicked.getY()+screen.getOffY(), screen);
			Player p = Main.instance.getRH().getCurrentPlayer();
			Hexagon h = this.getInArray(c);
			if(h != null && h.getType() != null){
				UIHandler.displayAnnotation("Can't place that there!", e.getX(), e.getY());
				return;
			}
			int cost = p.getCurrentHexagon(uih.getMarkedHexagon()).getAddition().getCost();
			if(p.isHexagonPlacable(h)){
				if(p.enoughMoney(cost)){
					p.addHexagon(h, uih.getMarkedHexagon());
					Main.instance.getRH().nextPlayer();
				}else{
					UIHandler.displayAnnotation("Not enough money, costs: "+cost, e.getX(), e.getY());
				}
			}else{
				UIHandler.displayAnnotation("Can't place that there!", e.getX(), e.getY());
			}
			
		}
	}

	public void handleHovering(MouseEvent e) {
	}
	
	public void setStarterHex(Player p){
		map[RADIUS][RADIUS].setType(HexTypeInt.TYPE_OFFICE.getValue());
		map[RADIUS][RADIUS].setAddition(AdditionFactory.createStartingAddition());
		p.addHexagon(map[RADIUS][RADIUS], -1);
	}
}
