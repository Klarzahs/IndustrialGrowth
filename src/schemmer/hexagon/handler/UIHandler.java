package schemmer.hexagon.handler;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import schemmer.hexagon.game.Main;
import schemmer.hexagon.player.Player;
import schemmer.hexagon.ui.CurrentHexagonIcon;
import schemmer.hexagon.ui.NextHexagonIcon;
import schemmer.hexagon.ui.PlayerRessourceUI;
import schemmer.hexagon.ui.PopupInfo;

public class UIHandler {
	private CurrentHexagonIcon currentHexs;
	private NextHexagonIcon nextHexs;
	private PlayerRessourceUI presUI;
	private PopupInfo info;
	
	private static UIHandler instance;
	
	
	public UIHandler(Main m){
		instance = this;
		currentHexs = new CurrentHexagonIcon(25, Main.instance.getGUI().getHeight() - 100, 75, 75, 2, 15, 15, 2);
		nextHexs = new NextHexagonIcon(250, Main.instance.getGUI().getHeight() - 100, 75, 75, 2, 15, 15, 2);
		presUI = new PlayerRessourceUI();
		info = new PopupInfo();
	}
	
	public void draw(Graphics2D g2d){
		if(Main.PHASE >= 2){
			Player currentPlayer = Main.instance.getRH().getCurrentPlayer();
			presUI.draw(currentPlayer, g2d);
			currentHexs.draw(currentPlayer, g2d);
			nextHexs.draw(currentPlayer, g2d);
			info.draw(g2d);
		}
	}
	
	
	public boolean cursorInIconArea(MouseEvent e){
		return cursorInIconArea(e.getX(), e.getY());
	}
	
	public boolean cursorInIconArea(double x, double y){
		if(currentHexs.getBoundingRect().contains(x, y)){
			return true;
		}
		if(presUI.getBoundingRect().contains(x, y)){
			return true;
		}
		return false;
	}
	
	public void resetHoveringInformation() {
		currentHexs.resetHoveringNr();
		nextHexs.resetHoveringNr();
	}
	
	public void handleLeftClick(MouseEvent e) {
		currentHexs.handleMarking(e);
	}
	
	public void handleHovering(MouseEvent e){
		currentHexs.handleHovering(e);
		nextHexs.handleHovering(e);
	}

	public boolean isMarked() {
		return (currentHexs.isMarked() || nextHexs.isMarked());
	}

	public void resetMarkedInformation() {
		currentHexs.resetMarkableNr();
		nextHexs.resetMarkableNr();
	}
	
	public int getMarkedHexagon(){
		return currentHexs.getMarkedNr();
	}

	public static void displayAnnotation(String text, int x, int y){
		if(instance == null) return;
		instance.info.displayAnnotation(text, x, y);
	}
	
	public static void hideAnnotation(){
		if(instance == null) return;
		instance.info.hideAnnotation();
	}
	
}
