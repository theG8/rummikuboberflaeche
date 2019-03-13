package de.g8keeper.rummikub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class TileSet implements IEvaluable, Iterable<Tile> {


    protected List<Tile> tiles;

    public TileSet() {
        // initialize this with new ArrayList<Tile>
        this(new ArrayList<Tile>());
    }

    public TileSet(List<Tile> list) {

        tiles = list;
    }

    public void addTile(Tile tile) {

        // add tile to tiles
        tiles.add(tile);
    }

    public void addTile(int index, Tile tile) {
        // add tile to tiles at specified index-point
        tiles.add(index, tile);

    }

    public Tile removeTile(int index) {
        // remove tile at tiles(index)
        return tiles.remove(index);
    }

    public void clear(){
        // remove all tiles
        tiles.clear();
    }

    public Tile tileAt(int index) {
        // return Tile at index-position
        return tiles.get(index);
    }

    public int size() {
        return tiles.size();
    }

    public void sort() {
        // call requires API level 24....
//        tiles.sort((a, b) -> a.compareTo(b));

        Collections.sort(tiles,(a, b) -> a.compareTo(b));

    }

    @Override
    public String toString() {
        return tiles.toString();
    }

    @Override
    public int getPoints() {
        int points = 0;

        // iterate over tiles
        for (Tile t : tiles) {
            // add point-value of every tile to points
            points += t.getPoints();
        }

        return points;
    }

    @Override
    public Iterator<Tile> iterator() {

        return new TileItr(tiles);
    }

    class TileItr implements Iterator<Tile> {

        private List<Tile> itrList;
        private int index = 0;

        public TileItr(List<Tile> tiles) {
            this.itrList = tiles;
        }

        @Override
        public boolean hasNext() {
            return index < itrList.size();
        }

        @Override
        public Tile next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no more elements...");
            }
            return itrList.get(index++);
        }

        @Override
        public void remove() {
            // myList.remove(index);
            throw new UnsupportedOperationException("remove not supported...");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(tiles);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TileSet)) {
            return false;
        }
        TileSet other = (TileSet) obj;
        if (tiles == null) {
            if (other.tiles != null) {
                return false;
            }
        } else if (!tiles.equals(other.tiles)) {
            return false;
        }
        return true;
    }
}
