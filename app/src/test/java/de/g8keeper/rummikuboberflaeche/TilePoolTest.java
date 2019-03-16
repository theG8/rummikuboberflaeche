package de.g8keeper.rummikuboberflaeche;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TilePool;

public class TilePoolTest {

    public static void main(String[] args) {

        TilePool pool = new TilePool();



        Tile t;
        int bevore = pool.size();
        int count = 4;

        System.out.println("pool size before remove: " + bevore);

        for (int i = 0; i < count; i++) {
            t = new Tile();
            System.out.println("removing " + t);

            pool.removeTile(t);
        }

        System.out.println("pool size after removing " + count + " tiles: " + pool.size());

        if(pool.size() == bevore - count){
            System.out.println("OK");
        } else {
            System.out.println("FAILED");
        }

    }
}
