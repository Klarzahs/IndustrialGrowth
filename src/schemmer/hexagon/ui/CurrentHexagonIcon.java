package schemmer.hexagon.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import schemmer.hexagon.game.Main;
import schemmer.hexagon.game.Screen;
import schemmer.hexagon.loader.Image;
import schemmer.hexagon.loader.ImageLoader;
import schemmer.hexagon.loader.ImageNumber;
import schemmer.hexagon.player.Player;
import schemmer.hexagon.type.Hexagon;

public class CurrentHexagonIcon extends HoverableIcon {

	@ImageNumber(number = 1)
	private static BufferedImage[] images = new BufferedImage[1];
	
	public static int posY = 0;

	@Image
	public static void loadImages(GraphicsConfiguration gc) {
		if (gc != null) {
			images[0] = ImageLoader.loadImage("/png/images/folder.png");
			posY = Screen.HEIGHT - images[0].getHeight();
		}
	}

	private Hexagon hexLeft, hexRight;

	public CurrentHexagonIcon(int px, int py, int x, int y, int rectCount, int offsetX, int offsetY, int tableSize) {
		super(px, py, x, y, rectCount, offsetX, offsetY, tableSize);
	}

	@Override
	public void drawIcons(Player p, Graphics2D g2d) {
		if (Main.PHASE == 2) {
			g2d.drawImage(images[0], 0, Screen.HEIGHT-images[0].getHeight(), images[0].getWidth(), images[0].getHeight(), null);
			if(hexLeft.getAddition().getCost() > p.getMoneyCount()){
				g2d.setColor(Color.red);
				g2d.setStroke(new BasicStroke(3));
				g2d.drawLine(this.rects[0].x, this.rects[0].y + this.rects[0].height,
						this.rects[0].x + this.rects[0].width, this.rects[0].y);
			}
			hexLeft.drawAdditionPreview(g2d, rects[0].x, rects[0].y + 20);
			if(hexRight.getAddition().getCost() > p.getMoneyCount()){
				g2d.setColor(Color.red);
				g2d.setStroke(new BasicStroke(3));
				g2d.drawLine(this.rects[1].x, this.rects[1].y + this.rects[1].height,
						this.rects[1].x + this.rects[1].width, this.rects[1].y);
			}
			hexRight.drawAdditionPreview(g2d, rects[1].x, rects[1].y + 20);
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
		hexLeft = player.getCurrentHexagon(0);
		hexRight = player.getCurrentHexagon(1);
		this.drawIcons(player, g2d);
	}
	
	@Override
	public Rectangle getBoundingRect(){
		if(images[0] != null){
			Rectangle r = new Rectangle(0, Screen.HEIGHT-images[0].getHeight(), images[0].getWidth(), images[0].getHeight());
			return r;
		}
		return new Rectangle(0,0,0,0);
	}
}
