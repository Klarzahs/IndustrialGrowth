package schemmer.hexagon.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;

import java.awt.Rectangle;
import schemmer.hexagon.game.Main;
import schemmer.hexagon.game.Screen;
import schemmer.hexagon.loader.Image;
import schemmer.hexagon.loader.ImageLoader;
import schemmer.hexagon.loader.ImageNumber;
import schemmer.hexagon.player.Player;
import schemmer.hexagon.utils.Log;

public class CurrentHexagonIcon extends HoverableIcon {

	@ImageNumber(number = 1)
	private static BufferedImage[] images = new BufferedImage[1];

	@Image
	public static void loadImages(GraphicsConfiguration gc) {
		if (gc != null) {
			images[0] = ImageLoader.loadImage("/png/images/folder.png");
		}
	}

	private BufferedImage imageLeft, imageRight;

	public CurrentHexagonIcon(int px, int py, int x, int y, int rectCount, int offsetX, int offsetY, int tableSize) {
		super(px, py, x, y, rectCount, offsetX, offsetY, tableSize);
	}

	@Override
	public void drawIcons(Graphics2D g2d) {
		if (Main.PHASE == 2) {
			g2d.drawImage(images[0], 0, Screen.HEIGHT-images[0].getHeight(), images[0].getWidth(), images[0].getHeight(), null);
			g2d.drawImage(imageLeft, this.rects[0].x, this.rects[0].y, this.rects[0].width, this.rects[0].height, null);
			g2d.drawImage(imageRight, this.rects[1].x, this.rects[1].y, this.rects[1].width, this.rects[1].height,
					null);
//			g2d.drawImage(imageNextLeft, this.rects[2].x, this.rects[2].y, this.rects[2].width, this.rects[2].height, null);
//			g2d.drawImage(imageNextRight, this.rects[1].x, this.rects[1].y, this.rects[1].width, this.rects[1].height,
//					null);
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
		imageLeft = player.getCurrentHexagon(0).getImage();
		imageRight = player.getCurrentHexagon(1).getImage();
		this.drawIcons(g2d);
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
