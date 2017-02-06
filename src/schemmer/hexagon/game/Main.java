package schemmer.hexagon.game;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import schemmer.hexagon.handler.EntityHandler;
import schemmer.hexagon.handler.MapHandler;
import schemmer.hexagon.handler.RoundHandler;
import schemmer.hexagon.handler.UIHandler;
import schemmer.hexagon.loader.FontLoader;
import schemmer.hexagon.loader.Image;
import schemmer.hexagon.loader.ImageLoader;
import schemmer.hexagon.loader.ImageNumber;
import schemmer.hexagon.player.Player;
import schemmer.hexagon.type.Hexagon;

public class Main implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, WindowStateListener {

	public static Main instance;

	private GUI gui;
	protected GameLoop gl;
	protected EntityHandler eh;
	protected MapHandler mh;
	protected RoundHandler rh;
	private ImageLoader il;
	protected final int HEIGHT = 1080;
	protected final int WIDTH = 1920;

	public static volatile int PHASE = 0;

	// ------ UI -------
	private Cursor rightClick, normalClick, leftClick;
	BufferedImage rightClickImage, normalClickImage, leftClickImage;
	private UIHandler uih;
	private boolean hasFocus = true;

	public Main(boolean isLocal, int player, int ai) {
		instance = this;
		eh = new EntityHandler();
		mh = new MapHandler(this);

		gui = new GUI(WIDTH, HEIGHT, true, this);
		gui.addMouseListener(this);
		gui.addMouseMotionListener(this);
		gui.addKeyListener(this);
		gui.addMouseWheelListener(this);

		mh.addScreen();

		rh = new RoundHandler(mh);
		rh.createAllPlayers(player, ai);
		rh.startRound();

		createUI();

		Main.PHASE = 1;
		FontLoader.load();
		il = new ImageLoader(this, Image.class, ImageNumber.class);
		while(Main.PHASE != 2){}
		rh.initHexs();
		try {
			gl = new GameLoop(this);
			gl.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createUI() {
		try {
			uih = new UIHandler(this);
			gui.getScreen().setUIH(uih);

			// ---- cursor ----
			rightClickImage = ImageIO.read(this.getClass().getResourceAsStream("/png/etc/cursorSword_bronze.png"));
			normalClickImage = ImageIO.read(this.getClass().getResourceAsStream("/png/etc/cursorGauntlet_blue.png"));
			leftClickImage = ImageIO.read(this.getClass().getResourceAsStream("/png/etc/cursorHand_beige.png"));
			rightClick = Toolkit.getDefaultToolkit().createCustomCursor(rightClickImage, new Point(0, 0),
					"Right Click");
			normalClick = Toolkit.getDefaultToolkit().createCustomCursor(normalClickImage, new Point(0, 0),
					"Normal cursor");
			leftClick = Toolkit.getDefaultToolkit().createCustomCursor(leftClickImage, new Point(0, 0), "Left Click");

			this.gui.getRootPane().setCursor(normalClick);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public EntityHandler getEH() {
		return eh;
	}

	public MapHandler getMH() {
		if (this instanceof Main)
			return this.mh;
		return mh;
	}

	public GUI getGUI() {
		return gui;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// handleLeftClick(e);
		// handleRightClick(e);

		this.gui.getRootPane().setCursor(normalClick);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (inputAllowed()) {
			handleLeftClick(e);
			handleRightClick(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (inputAllowed())
			this.gui.getRootPane().setCursor(normalClick);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (inputAllowed()) {
			if (uih != null) {
				if(this.uih.cursorInIconArea(e)){
					uih.handleHovering(e);
				}else{
					mh.handleHovering(e);
					uih.resetHoveringInformation();
				}
			}
			mh.setHovered(e);
			UIHandler.hideAnnotation();
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int rot = e.getWheelRotation() * 5;
		if(rot < 0){
			for(int i = rot; i < 0; i++){
				Hexagon.zoomIn();
			}
		}else{
			for(int i = 0; i < rot; i++){
				Hexagon.zoomOut();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (inputAllowed()) {
			gui.getScreen().setDebug("" + e.getKeyCode());
			if (e.getModifiers() == InputEvent.CTRL_MASK) {
				switch (e.getKeyCode()) {
				case 521: // "+"
					Hexagon.zoomIn();
					break;
				case 45: // "-"
					Hexagon.zoomOut();
					break;
				case 32: // " "
					gui.getScreen().setDebug(rh.getCurrentRound() + ": " + rh.getCurrentPlayer());
					break;
				case 116: // "F5" - Quicksave
					gl.pause();
					rh.quicksave();
					gl.unpause();
					break;
				case 120: // "F9" - Quickload
					gl.pause();
					rh.quickload();
					gl.unpause();
					break;
				default:
					gui.getScreen().setDebug("" + e.getKeyCode());
					break;
				}
				if (e.getKeyCode() <= 57 && e.getKeyCode() >= 49)
					gui.getScreen().recreate((e.getKeyCode() - 48) * 2);
			}
		}
	}

	private void handleLeftClick(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			UIHandler.hideAnnotation();
			this.gui.getRootPane().setCursor(leftClick);
			if(this.uih.cursorInIconArea(e)){
				uih.handleLeftClick(e);
			}else{
				mh.handleLeftClick(e);
				uih.resetMarkedInformation();
			}
		}
	}

	private void handleRightClick(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			UIHandler.hideAnnotation();
			this.gui.getRootPane().setCursor(rightClick);
		}
	}

	public RoundHandler getRH() {
		return rh;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public Player getCurrentPlayer() {
		return rh.getCurrentPlayer();
	}

	public UIHandler getUIH() {
		return uih;
	}

	public ImageLoader getIL() {
		return il;
	}

	public int getPhase() {
		return Main.PHASE;
	}

	public Main() {
	} // dead constructor for Server

	private boolean inputAllowed() {
		return true;
	}

	@Override
	public void windowStateChanged(WindowEvent e) {
		if (e.getNewState() == WindowEvent.WINDOW_LOST_FOCUS) {
			hasFocus = false;
		} else if (e.getNewState() == WindowEvent.WINDOW_GAINED_FOCUS) {
			hasFocus = true;
		}
		e.getWindow().setVisible(hasFocus);
	}

	public boolean hasFocus() {
		return hasFocus;
	}

	public void setMH(MapHandler m) {
		this.mh = m;
	}
}
