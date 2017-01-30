package schemmer.hexagon.handler;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import schemmer.hexagon.game.Main;
import schemmer.hexagon.player.Player;
import schemmer.hexagon.ui.CurrentHexagonIcon;
import schemmer.hexagon.ui.NextHexagonIcon;

public class UIHandler {
	private CurrentHexagonIcon currentHexs;
	private NextHexagonIcon nextHexs;
	
	private Main main;
	
	public UIHandler(Main m){
		main = m;
		
		currentHexs = new CurrentHexagonIcon(25, main.getGUI().getHeight() - 100, 75, 75, 2, 15, 15, 2);
		nextHexs = new NextHexagonIcon(250, main.getGUI().getHeight() - 100, 75, 75, 2, 15, 15, 2);
	}
	
	public void draw(Graphics2D g2d){
		Player currentPlayer = main.getRH().getCurrentPlayer();
		int maxPlayer = main.getRH().getPlayerCount();
		currentHexs.draw(currentPlayer, g2d);
		nextHexs.draw(currentPlayer, g2d);
	}
	
	
	public boolean cursorInIconArea(MouseEvent e){
		return cursorInIconArea(e.getX(), e.getY());
	}
	
	public boolean cursorInIconArea(double x, double y){
		if(currentHexs.getBoundingRect().contains(x, y)){
			return true;
		}
		return false;
	}
	
	public void resetAllIcons(){
//		getBuildingIcons().resetBuildingIconNr();
//		if(getBuildingMenu() != null)
//			getBuildingMenu().resetUnitIconNr();
//		stateIcons.resetStateIconNr();
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
	
}
