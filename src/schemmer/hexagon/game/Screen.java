package schemmer.hexagon.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import schemmer.hexagon.handler.EntityHandler;
import schemmer.hexagon.handler.MapHandler;
import schemmer.hexagon.handler.UIHandler;
import schemmer.hexagon.loader.FontLoader;
import schemmer.hexagon.loader.Image;
import schemmer.hexagon.loader.ImageLoader;
import schemmer.hexagon.loader.ImageNumber;
import schemmer.hexagon.type.Hexagon;
import schemmer.hexagon.utils.Log;

public class Screen extends JPanel{
	private static final long serialVersionUID = 101L;
	private final GraphicsConfiguration gfxConf = GraphicsEnvironment
						.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	
	public final static int WIDTH = 1920;
	public final static int HEIGHT = 1080;

	private Main main;
	private BufferedImage offImg;
	private EntityHandler eh;
	private MapHandler mh;
	private UIHandler uih;
	private String debug;
	private String fps = "";
	
	private int offX = 0, offY = 0;
	private int maxOffX, maxOffY;
	
	@ImageNumber(number = 1)	
	private static BufferedImage[] images = new BufferedImage[1];
	
	public Screen(Main main){
		super();
		this.main = main;
		eh = main.getEH();
		mh = main.getMH();
		debug = "";
		calculateOffsets();
	}
	
	@Override
	public void paint(Graphics g) {
		if(main.hasFocus()){
			Graphics2D g2d = (Graphics2D) g;
			if ( offImg == null || offImg.getWidth() != getWidth() || offImg.getHeight() != getHeight() ){
				offImg = gfxConf.createCompatibleImage( getWidth(), getHeight() );
				paint( offImg.createGraphics() );
			}
		
			g.drawImage( offImg, 0, 0, this );
			g2d.setColor( Color.LIGHT_GRAY ); g.fillRect( 0, 0, getWidth(), getHeight() );
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			if(main.getPhase() == 0){
				g2d.setColor(Color.BLUE);
				g2d.drawRect(WIDTH/2-100, HEIGHT/2-25, 200, 50);
			}else if(main.getPhase() == 1 || (main.getPhase() == 2 && !ImageLoader.isFinishedLoading())){
				g2d.setColor(Color.BLUE);
				g2d.fillRect(WIDTH/2-100, HEIGHT/2-25, (int) ((ImageLoader.progress) * (200f / ImageLoader.maxProgress)), 50);
				g2d.drawRect(WIDTH/2-100, HEIGHT/2-25, 200, 50);
			}else if(main.getPhase() == 2){
				drawBackground(g2d);
				mh.draw(g2d, offX, offY);
				eh.draw(g2d, offX, offY);
				uih.draw(g2d);
				
				g2d.setColor(Color.YELLOW);
				g2d.setFont(FontLoader.YELLOWSUN.deriveFont(20f));
				g2d.drawString(debug, WIDTH/2, HEIGHT-100);
				g2d.drawString(fps, WIDTH-100, 20);
			}else{
				Log.d("somethings wrong!"+(main.getRH() == null)+" "+(main.getIL() == null));
			}
		}
	}
	
	private void drawBackground(Graphics2D g2d) {
//		g2d.drawImage(image, (int) (center.x-offX-SIZE+7*OSF), (int)center.y-offY-SIZE, (int) (SIZE*Math.sqrt(3) +1*OSF), (int)(SIZE*2 + 1 * OSF), null);
		int w = images[0].getWidth();
		int h = images[0].getHeight();
		for(int y = -maxOffY/h * 2; y < maxOffY/h * 2; y++){
			for(int x = -maxOffX / w * 2; x < maxOffX/w * 2; x++){
				double ws = Math.ceil(w * Hexagon.OSF);
				double hs = Math.ceil(h * Hexagon.OSF);
				int tx = (int) Math.ceil(x*ws-offX + WIDTH/2);
				int ty = (int)Math.ceil(y*hs-offY + HEIGHT / 2);
				//range check
				if(tx >= - w * 2 && tx <= WIDTH + w * 2 && ty >= - h * 2 && ty <= HEIGHT + h * 2){
					g2d.drawImage(images[0], tx, ty, (int)ws, (int)hs, null);
				}
			}
		}
	}

	public void setDebug(String d){
		debug = d;
	}
	
	public void appendDebug(String d){
		debug = debug + "   " + d;
	}
	
	public void recreate(int newRadius){
		mh.recreate(newRadius);
	}
	
	public void moveDown(){
		if(offY <= maxOffY*Hexagon.OSF) offY += 10;
	}
	
	public void moveUp(){
		if(offY >= -maxOffY*Hexagon.OSF) offY -= 10;
	}
	public void moveLeft(){
		if(offX >= -maxOffX*Hexagon.OSF) offX -= 10;
	}
	public void moveRight(){
		if(offX <= maxOffX*Hexagon.OSF) offX += 10;
	}
	
	public int getOffX(){
		return offX;
	}
	
	public int getOffY(){
		return offY;
	}
	
	public void calculateOffsets(){
//		double diffX = WIDTH / 2 - ((mh.RADIUS*2 + 1) * Math.sqrt(3)/2 * Hexagon.getSize());
//		double diffY = HEIGHT / 2 - ((mh.RADIUS*2 + 1) * Hexagon.getSize());
		maxOffX = 25*mh.RADIUS;
		maxOffY = 25*mh.RADIUS;
	}
	
	public void setUIH(UIHandler myUIH){
		uih = myUIH;
	}
	
	public void setFPS(String f){
		this.fps = f;
	}
	
	public boolean isCursorInIconArea(double x, double y){
		return uih.cursorInIconArea(x, y);
	}
	
	@Image
	public static void loadImages(GraphicsConfiguration gc){
		if(gc != null){
			images[0] = ImageLoader.loadImage("/png/images/background_small2.jpg");
		}
	}
}
