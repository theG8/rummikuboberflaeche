package de.g8keeper.rummikuboberflaeche;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Tile;

public class ColorDemo {

    public static void main(String[] args) {
	for (Color c:Color.values()) {
	    System.out.println("prev -> " + Tile.previousColor(c) + "\t " + c + "\t next -> " + Tile.nextColor(c));
	}
	
	
    }

}
