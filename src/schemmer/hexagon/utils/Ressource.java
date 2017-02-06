package schemmer.hexagon.utils;

public class Ressource {
	public int money;
	public int stock;
	
	public Ressource(int money, int stock){
		this.money = money;
		this.stock = stock;
	}
	
	public void add(Ressource r){
		this.money += r.money;
		this.stock += r.stock;
	}
}
