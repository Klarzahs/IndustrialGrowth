package schemmer.hexagon.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import schemmer.hexagon.game.Main;
import schemmer.hexagon.player.Player;

public class NextHexagonIcon extends HoverableIcon{
	public NextHexagonIcon(int posX, int posY, int sizeX, int sizeY, int rectCount, int offsetX, int offsetY,
			int tableSize) {
		super(posX, posY, sizeX, sizeY, rectCount, offsetX, offsetY, tableSize);
	}

	private BufferedImage imageLeft, imageRight;

	@Override
	public void drawIcons(Graphics2D g2d) {
		if (Main.PHASE == 2) {
			g2d.drawImage(imageLeft, this.rects[0].x, this.rects[0].y, this.rects[0].width, this.rects[0].height, null);
			g2d.drawImage(imageRight, this.rects[1].x, this.rects[1].y, this.rects[1].width, this.rects[1].height,
					null);
			if (this.isHovering()) {
				g2d.setColor(Color.BLUE);
				int h = this.getHoveringNr();
				g2d.drawRect(this.rects[h].x, this.rects[h].y, this.rects[h].width, this.rects[h].height);
			}
			if (this.isMarked()) {
				g2d.setColor(Color.RED);
				int h = this.getMarkedNr();
				g2d.drawRect(this.rects[h].x, this.rects[h].y, this.rects[h].width, this.rects[h].height);
			}
		}
	}

	public void draw(Player player, Graphics2D g2d) {
		if (player.getRessourceChanged()) {
			player.refreshAll();
		}
		imageLeft = player.getNextHexagon(0).getImage();
		imageRight = player.getNextHexagon(1).getImage();
		this.drawIcons(g2d);
	}
}
