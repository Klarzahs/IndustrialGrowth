package schemmer.hexagon.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import schemmer.hexagon.game.Main;
import schemmer.hexagon.player.Player;
import schemmer.hexagon.type.Hexagon;

public class NextHexagonIcon extends HoverableIcon{
	public NextHexagonIcon(int posX, int posY, int sizeX, int sizeY, int rectCount, int offsetX, int offsetY,
			int tableSize) {
		super(posX, posY, sizeX, sizeY, rectCount, offsetX, offsetY, tableSize);
	}

	private Hexagon hexLeft, hexRight;

	@Override
	public void drawIcons(Player p, Graphics2D g2d) {
		if (Main.PHASE == 2) {
			hexLeft.drawAdditionPreview(g2d, rects[0].x, rects[0].y + 20);
			hexRight.drawAdditionPreview(g2d, rects[1].x, rects[1].y + 20);
			if (this.isHovering()) {
				g2d.setColor(Color.BLUE);
				int h = this.getHoveringNr();
				g2d.drawRect(this.rects[h].x, this.rects[h].y, this.rects[h].width, this.rects[h].height);
			}
		}
	}

	public void draw(Player player, Graphics2D g2d) {
		if (player.getRessourceChanged()) {
			player.refreshAll();
		}
		hexLeft = player.getNextHexagon(0);
		hexRight = player.getNextHexagon(1);
		this.drawIcons(player, g2d);
	}
}
