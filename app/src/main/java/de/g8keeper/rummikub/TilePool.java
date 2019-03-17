package de.g8keeper.rummikub;

import java.util.ArrayList;
import java.util.Collections;


// TilePool is the Pool from which the players get their mTiles
// it contains twice the values Tile.MAX_VALUE (=1) to Tile.MIN_VALUE (=13) of each
// color (RED, YELLOW, BLUE, BLACK) plus 2 joker, which means 106 mTiles

public class TilePool {

    private ArrayList<Tile> mPool;


    public TilePool() {

        mPool = new ArrayList<Tile>();

        buildPool();
    }

    public TilePool(TilePool tilePool){
        this.mPool = new ArrayList<>();
        for(Tile t: tilePool.mPool){
            this.mPool.add(new Tile(t));
        }
    }

    private void buildPool() {
        System.out.print("Building mPool...");

        // farben iterieren (rot, gelb, blau, schwarz)
        for (Color color : Color.values()) {
            // werte von 1 bis 13 iterieren
            for (int value = Tile.MIN_VALUE; value <= Tile.MAX_VALUE; value++) {
                // jeweils 2 Steine erzeugen und in spielsteine hinzufügen
                mPool.add(new Tile(color, value));
                mPool.add(new Tile(color, value));
            }
        }
        // 2 joker erzeugen
        mPool.add(new Tile(true));
        mPool.add(new Tile(true));

        Collections.shuffle(mPool);

        System.out.println("complete");
    }

    public int size() {
        return this.mPool.size();
    }

    public Tile getTile() {

        if (mPool.isEmpty()) {
            throw new RuntimeException("Der Pool ist leer.");
        }

        return mPool.remove((int) ((Math.random() * (mPool.size() - 1)) + 1));
    }

    public void removeTile(Tile tile){

        if(tile.isJoker()){
            for(Tile t :mPool){
                if(t.isJoker()){
                    mPool.remove(t);
                    break;
                }
            }
        } else {

            if (mPool.contains(tile)) {
                mPool.remove(tile);
            }
        }
    }

    @Override
    public String toString() {
        return mPool.toString();
    }

}
