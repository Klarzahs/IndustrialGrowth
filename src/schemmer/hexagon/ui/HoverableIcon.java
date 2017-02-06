package schemmer.hexagon.ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import org.w3c.dom.css.Rect;

import schemmer.hexagon.game.Main;
import schemmer.hexagon.player.Player;
import schemmer.hexagon.utils.Log;

public abstract class HoverableIcon {
	protected int middleX, middleY;
	protected Rectangle[] rects;
	protected int selectedHoverableNr = -1;
	protected int selectedMarkableNr = -1;
	protected Rectangle boundingRect;
	
	protected String[] messages;
	
	public HoverableIcon(int posX, int posY, int sizeX, int sizeY, int rectCount, int offsetX, int offsetY, int tableSize){
		middleX = Main.instance.getGUI().getWidth()/2;
		middleY = Main.instance.getGUI().getHeight()/2;
		
		rects = new Rectangle[rectCount];
		for(int i = 0; i < rectCount; i++){
			rects[i] = new Rectangle(posX + (i % tableSize) * (sizeX+offsetX), posY + (i / tableSize) * (sizeY+offsetY), sizeX, sizeY);
		}
		
		boundingRect = new Rectangle(posX, posY, tableSize*(sizeX+offsetX), (tableSize/rectCount) * (sizeY + offsetY));
	}
	
	public void handleHovering(MouseEvent e){
		boolean contained = false;
		for(int i = 0; i < rects.length; i++){
			if(rects[i].contains(e.getX(), e.getY())){
				selectedHoverableNr = i;
				contained = true;
				break;
			}
		}
		if(!contained){
			this.resetHoveringNr();
		}
	}
	
	public void handleMarking(MouseEvent e){
		for(int i = 0; i < rects.length; i++){
			if(rects[i].contains(e.getX(), e.getY())){
				selectedMarkableNr = i;
			}
		}
	}
	
	public String getHoveringMessage(){
		if(selectedHoverableNr == -1 || messages == null) return null;
		return messages[selectedHoverableNr];
	}
	
	public void resetHoveringNr(){
		selectedHoverableNr = -1;
	}
	
	public void resetMarkableNr(){
		selectedMarkableNr = -1;
	}
	
	public boolean isHovering(){
		return (selectedHoverableNr >= 0);
	}
	
	public boolean isMarked(){
		return (selectedMarkableNr >= 0);
	}
	
	public int getHoveringNr(){
		return selectedHoverableNr;
	}
	
	public int getMarkedNr(){
		return selectedMarkableNr;
	}
	
	protected void setMessages(String[] strings){
		messages = strings;
	}
	
	public abstract void drawIcons(Player p, Graphics2D g2d);
	
	public Rectangle getBoundingRect(){
		return this.boundingRect;
	}

}
