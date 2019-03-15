package de.g8keeper.rummikub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class TileSet implements IEvaluable, Iterable<Tile>, Serializable {


    protected List<Tile> mTiles;


    public TileSet() {
        // initialize this with new ArrayList<Tile>
        this(new ArrayList<>());
    }

    public TileSet(List<Tile> list) {

        mTiles = list;
    }

    public TileSet(byte[] bytearray){
        List<Tile> tmp = new ArrayList<>();

        if(bytearray.length > 0){
            for(byte b:bytearray){
                tmp.add(new Tile(b));
            }
        }

        mTiles = tmp;
    }


    private static byte[] getByterepresentation(TileSet tileSet) {
        byte[] byteArray = new byte[tileSet.size()];

        int i = 0;

        for (Tile tile : tileSet) {
            byteArray[i++] = tile.toByte();
        }

        return byteArray;
    }

    public void addTile(Tile tile) {

        // add tile to mTiles
        mTiles.add(tile);
    }

    public void addTile(int index, Tile tile) {
        // add tile to mTiles at specified index-point
        mTiles.add(index, tile);

    }

    public Tile removeTile(int index) {
        // remove tile at mTiles(index)
        return mTiles.remove(index);
    }

    public void clear() {
        // remove all mTiles
        mTiles.clear();
    }

    public Tile tileAt(int index) {
        // return Tile at index-position
        return mTiles.get(index);
    }

    public int size() {
        return mTiles.size();
    }

    public void sort() {
        // call requires API level 24....
//        mTiles.sort((a, b) -> a.compareTo(b));

        Collections.sort(mTiles, (a, b) -> a.compareTo(b));

    }

    public byte[] toBytearray(){
        return getByterepresentation(this);
    }


    @Override
    public String toString() {
        return mTiles.toString();
    }

    @Override
    public int getPoints() {
        int points = 0;

        // iterate over mTiles
        for (Tile t : mTiles) {
            // add point-value of every tile to points
            points += t.getPoints();
        }

        return points;
    }

    @Override
    public Iterator<Tile> iterator() {

        return new TileItr(mTiles);
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
        return Objects.hash(mTiles);
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
        if (mTiles == null) {
            if (other.mTiles != null) {
                return false;
            }
        } else if (!mTiles.equals(other.mTiles)) {
            return false;
        }
        return true;
    }
}
