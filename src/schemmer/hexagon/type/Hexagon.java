package schemmer.hexagon.type;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import schemmer.hexagon.addition.Addition;
import schemmer.hexagon.addition.AdditionFactory;
import schemmer.hexagon.game.Main;
import schemmer.hexagon.game.Screen;
import schemmer.hexagon.loader.Image;
import schemmer.hexagon.loader.ImageLoader;
import schemmer.hexagon.loader.ImageNumber;
import schemmer.hexagon.utils.Conv;
import schemmer.hexagon.utils.Cube;
import schemmer.hexagon.utils.Log;
import schemmer.hexagon.utils.Point;
import schemmer.hexagon.utils.Ressource;

public class Hexagon {
	@ImageNumber(number = 5)	
	private static BufferedImage[] images = new BufferedImage[5];
	private BufferedImage image;
	
	public final static double CORNERS = 6;	// # Corners
	private static int SIZE = 60;			// in Pixel
	private static float widthFactor = (float) (Math.sqrt(3)/2f);
	
	private HexType type;
	private Addition addition;
	
	private Point center;			// pixel
	private Cube coords;			// cube coords
	private int posX, posY;			// coords in map
	
	public static float OSF = SIZE/50f; 	//Offset Scaling Factor
	
	private Main main;
	
	
	// sorting
	public int priority = 999;
	
	public Hexagon(Main m, Cube c, int x, int y){
		this.coords = c;
		this.center = Conv.cubeToPixel(c);
		center = new Point(center.x + Screen.WIDTH/2, center.y + Screen.HEIGHT/2);
		this.posX = x;
		this.posY = y;
		main = m;
		createPicture();
	}
	
	
	public Hexagon() {
		//only invoke this for the blank hexagons!
	}


	@Image
	public static void loadImages(GraphicsConfiguration gc){
		/*	TYPE_NONE(0),
			TYPE_SALES(1),
			TYPE_PRODUCTION(2),
			TYPE_MEDIA(3),
			TYPE_OFFICE(4),
		 */
		if(gc != null){
			images[0] = ImageLoader.loadImage("/png/images/empty_tile.png");
			images[1] = ImageLoader.loadImage("/png/images/sales_tile.png");
			images[2] = ImageLoader.loadImage("/png/images/production_tile.png");
			images[3] = ImageLoader.loadImage("/png/images/media_tile.png");
			images[4] = ImageLoader.loadImage("/png/images/office_tile.png");
		}
	}
	
	public Point hexCorner(double i){
		double angleDeg = 360/CORNERS * i + 30;
		double angleRad = Math.PI /180 * angleDeg;
		return new Point(center.x + SIZE * Math.cos(angleRad), center.y + SIZE * Math.sin(angleRad));
	}
	
	public void draw(Graphics2D g2d, int offX, int offY){
		recalculateCenter();
		g2d.setColor(type.getColor());
		for (int i = 0; i < CORNERS; i++){
			g2d.drawLine((int)(hexCorner(i).x)-offX, (int)(hexCorner(i).y)-offY, (int)(hexCorner((i+1)%CORNERS).x)-offX, (int)(hexCorner((i+1)%CORNERS).y)-offY);
		}
		g2d.drawString(""+this.coords.getV()[0] + "|" + this.coords.getV()[1]+"|"+this.coords.getV()[2], (int)center.x-offX, (int)center.y-offY);
	}
	
	public void drawPicture(Graphics2D g2d, int offX, int offY){
		recalculateCenter();
//		g2d.drawImage(image, (int) (center.x-offX-SIZE+7*OSF), (int)center.y-offY-SIZE, (int) (SIZE*Math.sqrt(3) +1*OSF), (int)(SIZE*2 + 1 * OSF), null);
		drawAddition(g2d, offX, offY);
	}
	
	public void drawAddition(Graphics2D g2d, int offX, int offY){
		if(addition != null){
			int x = (int) (center.x - offX -SIZE + 12 * OSF);
			int y = (int)(center.y - offY - SIZE + 40 * OSF);
			int w = (int) (32 / 2 * OSF);
			int h = (int) (32 / 2 * OSF);
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, (int)(20 * OSF))); 
			g2d.setColor(Color.BLACK);
			g2d.drawString(addition.getName(), x, y);
			Ressource r = addition.roundAddition();
			drawRoundAddition(g2d, x, y, w, h, r);
			r = addition.positionAddition();
			drawPositionAddition(g2d, x, y, w, h, r);
		}
	}
	
	private void drawRoundAddition(Graphics2D g2d, int x, int y, int w, int h, Ressource r){
		if(r.money != 0){
			BufferedImage img = AdditionFactory.getImages()[0];
			g2d.drawImage(img, x, y, w, h, null);
			g2d.drawString(""+r.money, x + w + 5, (int)(y + 15 * OSF));
		}
		if(r.stock != 0){
			BufferedImage img = AdditionFactory.getImages()[1];
			
			if(r.money != 0){
				y += 16 * OSF;
			}
			g2d.drawImage(img, x, y, w, h, null);
			g2d.drawString(""+r.stock, x + w + 5, (int)(y + 15 * OSF));
		}
	}
	
	private void drawPositionAddition(Graphics2D g2d, int x, int y, int w, int h, Ressource r){
		if(r.money != 0){
			BufferedImage img = AdditionFactory.getImages()[0];
			g2d.drawImage(img, x, y, w, h, null);
			g2d.drawString(""+r.money + "p.A.", x + w + 5, (int)(y + 15 * OSF));
		}
		if(r.stock != 0){
			BufferedImage img = AdditionFactory.getImages()[1];
			if(r.money != 0){
				y += 16 * OSF;
			}
			g2d.drawImage(img, x, y, w, h, null);
			g2d.drawString(""+r.stock + "p.A.", x + w + 5, (int)(y + 15 * OSF));
		}
	}
	
	public void drawSlot(Graphics2D g2d, int offX, int offY){
		recalculateCenter();
		g2d.setColor(new Color(0, 255, 0, 50));
		int xs[] = new int [(int) CORNERS];
		int ys[] = new int [(int) CORNERS];
		
		for (int i = 0; i < xs.length; i++){
			xs[i] = (int) hexCorner(i).x - offX;
			ys[i] = (int) hexCorner(i).y - offY;
		}
		
		g2d.fillPolygon(xs, ys, (int) CORNERS);
	}
	
	public void createPicture(){
		if(this.type == null) return;
		image = images[this.type.getIndex()];
		if(image == null){
			Log.d("Image is zero, not loaded!");
		}
	}
	
	public void fill(Graphics2D g2d, int offX, int offY){
		recalculateCenter();
		g2d.setColor(new Color(225, 225, 225));
		int xs[] = new int [(int) CORNERS];
		int ys[] = new int [(int) CORNERS];
		
		for (int i = 0; i < xs.length; i++){
			xs[i] = (int) hexCorner(i).x - offX;
			ys[i] = (int) hexCorner(i).y - offY;
		}
		
		g2d.fillPolygon(xs, ys, (int) CORNERS);
	}
	
	public void fill(Graphics2D g2d, int offX, int offY, Color c){
		recalculateCenter();
		g2d.setColor(c);
		int xs[] = new int [(int) CORNERS];
		int ys[] = new int [(int) CORNERS];
		
		for (int i = 0; i < xs.length; i++){
			xs[i] = (int) hexCorner(i).x - offX;
			ys[i] = (int) hexCorner(i).y - offY;
		}
		
		g2d.fillPolygon(xs, ys, (int) CORNERS);
	}
	
	public void drawOutline(Graphics2D g2d, int offX, int offY){
		g2d.setColor(Color.BLACK);		
		//save old stroke and then thicken the line
		Stroke old = g2d.getStroke();	
		Stroke s = new BasicStroke((int)(3 * Hexagon.OSF));
		g2d.setStroke(s);
		for (int i = 0; i < CORNERS; i++){
			g2d.drawLine((int)(hexCorner(i).x)-offX, (int)(hexCorner(i).y)-offY, (int)(hexCorner((i+1)%CORNERS).x)-offX, (int)(hexCorner((i+1)%CORNERS).y)-offY);
		}
		g2d.setStroke(old);
	}
	
	public static double getSize(){
		return SIZE;
	}
	
	public String printCoords(){
		return coords.printCube()+"\n";
	}
	
	public String printCenter(){
		return "Center @"+center.x+" | "+center.y+"\n";
	}
	
	public void draw(Graphics2D g2d, Color c, Stroke s, int offX, int offY){
		g2d.setColor(c);
		g2d.setStroke(s);
		recalculateCenter();
		for (int i = 0; i < CORNERS; i++){
			g2d.drawLine((int)(hexCorner(i).x)-offX, (int)(hexCorner(i).y)-offY, (int)(hexCorner((i+1)%CORNERS).x)-offX, (int)(hexCorner((i+1)%CORNERS).y)-offY);
		}
	}
	
	public Cube getCoords(){
		return coords;
	}
	
	public Point getCenter(){
		return center;
	}
	
	public void recalculateCenter(){
		this.center = Conv.cubeToPixel(this.coords);
		center = new Point(center.x + Screen.WIDTH/2, center.y + Screen.HEIGHT/2);
	}
	
	public static void zoomIn(){
		if (SIZE < 80) SIZE ++;
		OSF = SIZE / 50f;
	}
	
	public static void zoomOut(){
		if (SIZE > 20) SIZE --;
		OSF = SIZE / 50f;
	}
	
	public HexType getType(){
		return type;
	}
	
	public void setType(int i){
		type = new HexType(i);
	}
	
	public void setAddition(Addition a){
		this.addition = a;
	}
	
	public boolean equals(Hexagon b){
		return this.getCoords().equals(b.getCoords());
	}
	
	public int getX(){
		return posX;
	}
	
	public int getY(){
		return posY;
	}
	
	public Hexagon neighbour(int i){
		Cube c = this.coords;
		Cube cnew = c.neighbour(i);
		if(cnew != null)
			return main.getMH().getInArray(cnew);
		return null;
	}
	
	public char getAsChar(){
		return (char)-1;
	}
	
	public boolean exists(){
		if(this.type == null) return false;
		return (this.type.getIndex() != HexTypeInt.TYPE_NONE.getValue());
	}
	
	public BufferedImage getImage(){
		return this.image;
	}
	
	public Addition getAddition(){
		return addition;
	}


	public void fillWithTypeColor(Graphics2D g2d, int offX, int offY) {
		Color typeC = type.getColor();
		Color c = new Color(typeC.getRed(), typeC.getGreen(), typeC.getBlue(), 128);
		this.fill(g2d, offX, offY, c);
	}
	
}

