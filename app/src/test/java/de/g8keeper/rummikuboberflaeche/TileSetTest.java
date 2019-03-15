package de.g8keeper.rummikuboberflaeche;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Lane;
import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TileSet;

public class TileSetTest {

    public static void main(String[] args) {
        TileSet tiles = new TileSet();


        tiles.addTile(new Tile(Color.RED,5));
        tiles.addTile(new Tile(Color.BLUE,9));
        tiles.addTile(new Tile(Color.BLACK,11));
        tiles.addTile(new Tile(Color.YELLOW,2));
        tiles.addTile(new Tile(Color.RED,5));
        tiles.addTile(new Tile(true));


        System.out.println(tiles);

        System.out.println();
        byte[] ba = tiles.toBytearray();

        tiles = new TileSet(ba);

        System.out.println(tiles);


        System.out.println();
        System.out.println();

        Lane lane = new Lane();


        lane.addTile(new Tile(Color.RED,5));
        lane.addTile(new Tile(Color.BLUE,9));
        lane.addTile(new Tile(Color.BLACK,11));
        lane.addTile(new Tile(Color.YELLOW,2));
        lane.addTile(new Tile(Color.RED,5));
        lane.addTile(new Tile(true));


        System.out.println(lane);

        System.out.println();
        ba = lane.toBytearray();

        lane = new Lane(ba);

        System.out.println(lane);


    }
}
