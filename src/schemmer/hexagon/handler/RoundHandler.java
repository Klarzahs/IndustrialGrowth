package schemmer.hexagon.handler;

import java.util.ArrayList;

import schemmer.hexagon.game.Main;
import schemmer.hexagon.player.Player;
import schemmer.hexagon.utils.Log;

public class RoundHandler {
	private ArrayList<Player> players = new ArrayList<Player>();
	private MapHandler mh;
	private volatile int playerCount = -1, currentPlayer, currentRound, AIcount;		// TODO: AIcount
	private Main main;
	
	public RoundHandler(MapHandler mh){
		this.mh = mh;
		main = mh.getMain();
	}
	
	public void createNPC(int i){
		Player pl = new Player(true, i);
		players.add(pl);
	}
	
	public void createPC(int i){
		Player pl = new Player(false, i);
		players.add(pl);
	}
	
	public void createAllPlayers(int playerCount, int AIcount){
		if(playerCount < 1)
			Log.e("RoundHandler", "Max Players below 1!");
		for (int i = 0; i < playerCount; i++){
			createPC(i);
		}
		for (int i = playerCount; i < AIcount + playerCount; i++){
			createNPC(i);
		}
		this.playerCount = playerCount;
	}
	
	public Player getCurrentPlayer(){
		if(players.size() == 0) return null;
		return getPlayer(currentPlayer);
	}
	
	public void startRound(){
		currentPlayer = 0;
		currentRound += 1;
	}
	
	public void nextPlayer(){
		nextPlayerLocal();
	}
	
	public void nextPlayerLocal(){
		mh.resetMarked();
		getCurrentPlayer().refreshAll();
		currentPlayer = (currentPlayer + 1) % (getPlayerCount() + getAICount());
		if(currentPlayer == playerCount)
			startRound();
	}

	public void quicksave(){
		
	}
	
	public void quickload(){
		
	}
	
	public int getCurrentRound(){
		return currentRound;
	}
	
	public int getPlayerCount(){							//if (!isLocal) => use only after setPlayerCount was invoked
		return playerCount;
	}
	
	public int getAICount(){								//if (!isLocal) => use only after setPlayerCount was invoked
		return AIcount;
	}
	
	public Player getPlayer(int i){
		if(players.size() == 0 || players.size() < i) return null;
		return players.get(i);
	}
	
	public int getCurrentPlayerIndex(){
		return currentPlayer;
	}
	
	public void setMaxPlayers(int max){
		if(!(main instanceof Main)) Log.e("RH: shouldn't set maxPlayers while server");
		this.playerCount = max;
	}
	
	public void setMaxAIs(int max){
		if(!(main instanceof Main)) Log.e("RH: shouldn't set maxAI while server");
		this.AIcount = max;
	}
	
	public void initHexs() {
		for(Player p : players){
			p.createStarterHex();
			p.refreshAll();
		}
		Log.d("Inited hexs");
	}
}
