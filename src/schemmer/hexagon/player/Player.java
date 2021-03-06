package schemmer.hexagon.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import schemmer.hexagon.addition.AdditionFactory;
import schemmer.hexagon.game.Main;
import schemmer.hexagon.type.Hexagon;
import schemmer.hexagon.utils.Ressource;

public class Player {
	private int money = 9;
	private int stock = 0;
	private int stockPR = 0;
	private int moneyPR = 0;
	private boolean hasRessourcesChanged = true;

	private PlayerColor color;
	private Hexagon currentHexagonLeft;
	private Hexagon currentHexagonRight;
	private Hexagon nextHexagonLeft;
	private Hexagon nextHexagonRight;
	private boolean isFirstInit = true;
	
	private ArrayList<Hexagon> currentHexagons = new ArrayList<Hexagon>();

	public Player(boolean isAI, int i) {
		color = new PlayerColor(i);
	}

	public Player(boolean isAI, int i, int x, int y) {
		color = new PlayerColor(i);
	}

	public PlayerColor getPColor() {
		return color;
	}

	public void refreshAll() {
		updateRessources();
		nextHexagons();
	}

	private void updateRessources() {
		Ressource r = getPR();
		setMoneyCount(getMoneyCount() + r.money);
		setStockCount(getStockCount() + r.stock);
		hasRessourcesChanged = false;
	}

	public void setRessourcesChanged(boolean b) {
		hasRessourcesChanged = b;
	}

	public boolean getRessourceChanged() {
		return hasRessourcesChanged;
	}

	public void setCurrentHexagons(Hexagon l, Hexagon r){
		this.currentHexagonLeft = l;
		this.currentHexagonRight = r;
	}
	
	public Hexagon getCurrentHexagon(int i){
		if(i == 0) return this.currentHexagonLeft;
		return this.currentHexagonRight;
	}
	
	public Hexagon getNextHexagon(int i){
		if(i == 0) return this.nextHexagonLeft;
		return this.nextHexagonRight;
	}

	private void nextHexagons() {
		Random r = new Random();
		
		if(isFirstInit){
			isFirstInit = false;
			currentHexagonLeft = new Hexagon();
			currentHexagonLeft.setType(r.nextInt(4)+1);
			currentHexagonLeft.setAddition(AdditionFactory.createAddition(currentHexagonLeft.getType()));
			currentHexagonLeft.createPicture();
			
			currentHexagonRight = new Hexagon();
			currentHexagonRight.setType(r.nextInt(4)+1);
			currentHexagonRight.setAddition(AdditionFactory.createAddition(currentHexagonRight.getType()));
			currentHexagonRight.createPicture();
		}else{
			currentHexagonLeft = nextHexagonLeft;
			currentHexagonRight = nextHexagonRight;
		}

		nextHexagonLeft = new Hexagon();
		nextHexagonLeft.setType(r.nextInt(4)+1);
		nextHexagonLeft.setAddition(AdditionFactory.createAddition(nextHexagonLeft.getType()));
		nextHexagonLeft.createPicture();
		
		nextHexagonRight = new Hexagon();
		nextHexagonRight.setType(r.nextInt(4)+1);
		nextHexagonRight.setAddition(AdditionFactory.createAddition(nextHexagonRight.getType()));
		nextHexagonRight.createPicture();
	}
	
	public ArrayList<Hexagon> getCurrentHexagons(){
		return this.currentHexagons;
	}
	
	//gets called with "-1" if its the first hexagon
	public void addHexagon(Hexagon h, int isMarked){
		switch(isMarked){
		case 0: 
			h.setType(currentHexagonLeft.getType().getIndex());
			h.setAddition(currentHexagonLeft.getAddition());
			this.money -= currentHexagonLeft.getAddition().getCost();
			break;
		case 1:
			h.setType(currentHexagonRight.getType().getIndex());
			h.setAddition(currentHexagonRight.getAddition());
			this.money -= currentHexagonRight.getAddition().getCost();
			break;
		}
		h.createPicture();
		currentHexagons.add(h);
	}
	
	public boolean isHexagonPlacable(Hexagon placer){
		Iterator<Hexagon> it = currentHexagons.iterator();
		while(it.hasNext()){
			Hexagon h = it.next();
			for(int i = 0; i < Hexagon.CORNERS; i++){
				Hexagon neighbour = h.neighbour(i);
				if(neighbour.equals(placer)){
					return true;
				}
			}
		}
		return false;
	}
	
	public void createStarterHex(){
		Main.instance.getMH().setStarterHex(this);
	}
	
	public int getMoneyCount(){
		return money;
	}
	
	public int getStockCount(){
		return stock;
	}
	
	public int getMoneyPR(){
		return moneyPR;
	}
	
	public int getStockPR(){
		return stockPR;
	}
	
	private Ressource getPR(){
		Ressource res = new Ressource(0,0);
		Iterator<Hexagon> it = currentHexagons.iterator();
		while(it.hasNext()){
			Hexagon h = it.next();
			res.add(getAdjencyBonus(h));
			res.add(getRoundBonus(h));
		}
		moneyPR = res.money;
		stockPR = res.stock;
		return res;
	}
	
	private Ressource getAdjencyBonus(Hexagon h){
		Ressource res = new Ressource(0,0);
		Ressource r = h.getAddition().positionAddition();
		for(int i = 0; i < Hexagon.CORNERS; i++){
			Hexagon temp = h.neighbour(i);
			if(temp != null && temp.getType() != null){
				if(h.getType().getIndex() == temp.getType().getIndex()){
					res.add(r);
				}
			}
		}
		return res;
	}
	
	private Ressource getRoundBonus(Hexagon h){
		return h.getAddition().roundAddition();
	}
	
	private void setMoneyCount(int m){
		money = m;
	}
	
	private void setStockCount(int s){
		stock = s;
	}
	
	public boolean enoughMoney(int m){
		return m <= this.money;
	}
}
