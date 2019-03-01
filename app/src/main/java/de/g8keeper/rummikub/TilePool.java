package de.g8keeper.rummikub;

import java.util.ArrayList;
import java.util.Collections;


// PoolOfTiles is the pool from which the players get their tiles
// it contains twice the values Tile.MAX_VALUE (=1) to Tile.MIN_VALUE (=13) of each
// color (RED, YELLOW, BLUE, BLACK) plus 2 joker, which means 106 tiles 

public class TilePool {

    private ArrayList<Tile> pool;

    
      
    
    public TilePool() {
	
	ArrayList<Tile> tiles = new ArrayList<Tile>();
	
	System.out.print("Building pool...");
	
	// farben iterieren (rot, gelb, blau, schwarz)
	for (Color color : Color.values()) {
	    // werte von 1 bis 13 iterieren
	    for (int value = Tile.MIN_VALUE; value <= Tile.MAX_VALUE; value++) {
		// jeweils 2 Steine erzeugen und in spielsteine hinzufügen
		tiles.add(new Tile(color, value));
		tiles.add(new Tile(color, value));
	    }
	}
	// 2 joker erzeugen
	tiles.add(new Tile(true));
	tiles.add(new Tile(true));

	Collections.shuffle(tiles);
			
	this.pool = tiles;
	
	System.out.println("complete");
    }
    
    public int Size() {
	return this.pool.size();
    }
    
    public Tile getTile() throws RuntimeException {

	if (pool.isEmpty()) {
	    throw new RuntimeException("Der Pool ist leer.");
	}
		
	return pool.remove((int) ((Math.random() * (pool.size()-1)) + 1));

    }
    
    @Override
    public String toString() {
	return pool.toString();
    }

}
