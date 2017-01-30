package schemmer.hexagon.addition;

import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.util.Random;

import schemmer.hexagon.addition.MediaAddition.MediaAdditionInternet;
import schemmer.hexagon.addition.MediaAddition.MediaAdditionPaper;
import schemmer.hexagon.addition.OfficeAddition.OfficeAdditionCentre;
import schemmer.hexagon.addition.OfficeAddition.OfficeAdditionCubicle;
import schemmer.hexagon.addition.OfficeAddition.OfficeAdditionEmployeeHousing;
import schemmer.hexagon.addition.ProductionAddition.ProductionAdditionChemistryPlant;
import schemmer.hexagon.addition.ProductionAddition.ProductionAdditionPowerPlant;
import schemmer.hexagon.addition.ProductionAddition.ProductionAdditionSweatShop;
import schemmer.hexagon.addition.SalesAddition.SalesAdditionMall;
import schemmer.hexagon.addition.SalesAddition.SalesAdditionManagement;
import schemmer.hexagon.addition.SalesAddition.SalesAdditionStore;
import schemmer.hexagon.loader.Image;
import schemmer.hexagon.loader.ImageLoader;
import schemmer.hexagon.loader.ImageNumber;
import schemmer.hexagon.type.HexType;

public class AdditionFactory {
	@ImageNumber(number = 2)	
	private static BufferedImage[] images = new BufferedImage[2];
	
	@Image
	public static void loadImages(GraphicsConfiguration gc){
		if(gc != null){
			images[0] = ImageLoader.loadImage("/png/additions/money.png");
			images[1] = ImageLoader.loadImage("/png/additions/stock.png");
		}
	}
	
	public static BufferedImage[] getImages(){
		return images;
	}
	
	/*
		TYPE_NONE(0),
		TYPE_SALES(1),
		TYPE_PRODUCTION(2),
		TYPE_MEDIA(3),
		TYPE_OFFICE(4),
	 */
	
	public static Addition createAddition(HexType type){
		switch(type.getIndex()){
		case 0:
			break;
		case 1:
			return createSalesAddition();
		case 2:
			return createProductionAddition();
		case 3: 
			return createMediaAddition();
		case 4:
			return createOfficeAddition();
		default:
			break;
		}
		return null;
	}
	
	private static float THRESHOLD_CHEM_PLANT = 0.5f;
	private static float THRESHOLD_POW_PLANT = 0.2f + THRESHOLD_CHEM_PLANT;
	private static float THRESHOLD_SWEATSHOP = 0.3f + THRESHOLD_POW_PLANT;
	
	private static ProductionAddition createProductionAddition(){
		Random r = new Random();
		double rand = r.nextDouble();
		if(rand < THRESHOLD_CHEM_PLANT){
			return new ProductionAdditionChemistryPlant();
		}
		if(rand < THRESHOLD_POW_PLANT){
			return new ProductionAdditionPowerPlant();
		}
		if(rand < THRESHOLD_SWEATSHOP){
			return new ProductionAdditionSweatShop();
		}
		return null;
	}
	
	private static float THRESHOLD_INTERNET = 0.5f;
	private static float THRESHOLD_PAPER = 0.5f + THRESHOLD_INTERNET;
	
	private static MediaAddition createMediaAddition(){
		Random r = new Random();
		double rand = r.nextDouble();
		if(rand < THRESHOLD_INTERNET){
			return new MediaAdditionInternet();
		}
		if(rand < THRESHOLD_PAPER){
			return new MediaAdditionPaper();
		}
		return null;
	}
	
	private static float THRESHOLD_EMPL_HOUSING = 0.3f;
	private static float THRESHOLD_CUBICLE = 0.7f + THRESHOLD_EMPL_HOUSING;
	
	private static OfficeAddition createOfficeAddition(){
		Random r = new Random();
		double rand = r.nextDouble();
		if(rand < THRESHOLD_EMPL_HOUSING){
			return new OfficeAdditionEmployeeHousing();
		}
		if(rand < THRESHOLD_CUBICLE){
			return new OfficeAdditionCubicle();
		}
		return null;
	}
	
	private static float THRESHOLD_STORE = 0.6f;
	private static float THRESHOLD_Management = 0.2f + THRESHOLD_STORE;
	private static float THRESHOLD_MALL = 0.2f + THRESHOLD_Management;
	
	private static SalesAddition createSalesAddition(){
		Random r = new Random();
		double rand = r.nextDouble();
		if(rand < THRESHOLD_STORE){
			return new SalesAdditionStore();
		}
		if(rand < THRESHOLD_Management){
			return new SalesAdditionManagement();
		}
		if(rand < THRESHOLD_MALL){
			return new SalesAdditionMall();
		}
		return null;
	}
	
	public static OfficeAddition createStartingAddition(){
		return new OfficeAdditionCentre();
	}
}
