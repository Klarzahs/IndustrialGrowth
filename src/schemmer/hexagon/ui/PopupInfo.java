package schemmer.hexagon.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

import schemmer.hexagon.game.Main;
import schemmer.hexagon.loader.FontLoader;

public class PopupInfo {

	private String text;
	private int x, y;
	private boolean visible = false;
	
	public void displayAnnotation(String text, int x, int y){
		this.text = text;
		this.x = x;
		this.y = y;
		this.visible = true;
	}
	
	public void draw(Graphics2D g2d){
		if(Main.PHASE == 2){
			if(visible){
				g2d.setFont(FontLoader.YELLOWSUN.deriveFont(30f));
				FontRenderContext frc = g2d.getFontRenderContext();
		        GlyphVector gv = g2d.getFont().createGlyphVector(frc, text);
		        Rectangle r = gv.getPixelBounds(null, x, y);
				g2d.setColor(Color.lightGray);
				int offset = 30;
				int margin = 10;
				float scale = 1;
				g2d.fillRect(x + offset, y, (int) ((r.width + margin) * scale), (int)((r.height + margin) * scale));
				g2d.setColor(Color.black);
				g2d.drawString(text, x + offset + margin / 2, y + r.height + margin / 2);
				g2d.setColor(Color.darkGray);
				g2d.drawRect(x + offset, y, (int) ((r.width + margin) * scale), (int)((r.height + margin) * scale));
			}
		}
	}
	
	public void hideAnnotation(){
		this.visible = false;
	}
	
}
