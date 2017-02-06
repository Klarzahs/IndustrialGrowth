package schemmer.hexagon.loader;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

import schemmer.hexagon.utils.Log;

public class FontLoader {
	public static Font YELLOWSUN;
	public static void load(){
		try {
		     GraphicsEnvironment ge =  GraphicsEnvironment.getLocalGraphicsEnvironment();
		     InputStream is = FontLoader.class.getResourceAsStream("/font/lemon_yellow_sun.otf");
		     YELLOWSUN = Font.createFont(Font.TRUETYPE_FONT, is);
		     ge.registerFont(YELLOWSUN);
		     Log.d("Fonts loaded");
		} catch (IOException|FontFormatException e) {
		     Log.e("Fontloader Exc: "+e.getLocalizedMessage());
		     System.exit(1);
		}
	}
}
