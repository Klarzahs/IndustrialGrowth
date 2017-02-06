package schemmer.hexagon.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import schemmer.hexagon.addition.AdditionFactory;
import schemmer.hexagon.game.Main;
import schemmer.hexagon.game.Screen;
import schemmer.hexagon.loader.FontLoader;
import schemmer.hexagon.loader.Image;
import schemmer.hexagon.loader.ImageLoader;
import schemmer.hexagon.loader.ImageNumber;
import schemmer.hexagon.player.Player;
import schemmer.hexagon.utils.Log;

public class PlayerRessourceUI {
	@ImageNumber(number = 1)	
	private static BufferedImage[] images = new BufferedImage[1];
	
	@Image
	public static void loadImages(GraphicsConfiguration gc){
		if(gc != null){
			images[0] = ImageLoader.loadImage("/png/images/playerinfo.png");
		}
	}
	
	private int h = 0;
	
	public void draw(Player p, Graphics2D g2d){
		if(Main.PHASE == 2){
			h = CurrentHexagonIcon.posY - images[0].getHeight() + 80;
			g2d.drawImage(images[0], 0, h, images[0].getWidth(), images[0].getHeight(), null);
			g2d.setColor(Color.black);
			g2d.setFont(FontLoader.YELLOWSUN.deriveFont(Font.BOLD, 30f)); 
			g2d.drawString("Rounds left: "+Main.instance.getRH().getRoundDiff(), 5, h + 40);
			g2d.setFont(FontLoader.YELLOWSUN.deriveFont(40f)); 
			int size = 25;
			BufferedImage img = AdditionFactory.getImages()[0];
			g2d.drawImage(img, 15, h + 45, size, size, null);
			g2d.drawString(p.getMoneyCount()+" ("+p.getMoneyPR()+")", 45, h + 80);
			img = AdditionFactory.getImages()[1];
			g2d.drawImage(img, 15, h + 85, size, size, null);
			g2d.drawString(p.getStockCount()+" ("+p.getStockPR()+")", 45, h + 120);
		}
	}
	
	public Rectangle getBoundingRect(){
		if(images[0] != null){
			int h = CurrentHexagonIcon.posY - images[0].getHeight() + 80;
			Rectangle r = new Rectangle(0, h, images[0].getWidth(), images[0].getHeight());
			return r;
		}
		return new Rectangle(0,0,0,0);
	}
}
