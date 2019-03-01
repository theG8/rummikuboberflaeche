package de.g8keeper.rummikub;

import java.util.ArrayList;
import java.util.List;

public class Rummikub {

    private TilePool pool;
    private List<Lane> lanes;
    private List<Player> players;

    public Rummikub() {
	this.pool = new TilePool();
	this.lanes = new ArrayList<>();
	this.players = new ArrayList<>();
    }

    public static void main(String[] args) {

	Rummikub game = new Rummikub();
	Player player;
	
	
	System.out.println(game.pool);
	
	
	player = new Player("Horst");

	for (int i = 0; i < 14; i++) {
	    player.getTileSet().addTile(game.pool.getTile());
	}
	
	player.getTileSet().sort();
	
	game.players.add(player);

	System.out.println(game.players);
	
	

    }

}
