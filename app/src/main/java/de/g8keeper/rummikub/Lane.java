
package de.g8keeper.rummikub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Lane extends TileSet {

    private static final boolean DEBUG = false;
    public static int MIN_TILES_TO_VERIFY; // minimum amount of tiles in a lane to verify

    static {
	MIN_TILES_TO_VERIFY = 3; // in rummikub, a lane must contain a minimum of
				 // three tiles, to verify

    }

    // initializes the ListOfTiles
    public Lane() {

	super();
    }

    // initializes the ListOfTiles with set
    public Lane(List<Tile> list) {

	super(list);
    }
    
    public Lane(Lane lane) {
	super();
	
	for (Tile tile:lane) {
	    super.addTile(tile);
	}
	
    }
    

    // adds a tile to the lane
    @Override
    public void addTile(Tile tile) {

	// if tile is joker and the lane allready contains tiles, its value and color
	// needs to be set
	// to the right values, based on the ordering of the lane
	if (tile.isJoker() && size() > 0) {
	    if (isGroup()) {
		tile.setValue(tileAt(size() - 1).getValue());
		tile.setColor(Tile.nextColor(tileAt(size() - 1)));
	    } else if (isRun()) {
		tile.setValue(Tile.nextValue(tileAt(size() - 1)));
		tile.setColor(tileAt(size() - 1).getColor());
	    }
	}

	super.addTile(tile);

    }

    // adds a tile to the lane at the given index
    @Override
    public void addTile(int index, Tile tile) {

	// if tile is joker and the lane allready contains tiles, its value and color
	// needs to be set
	// to the right values, based on the ordering of the lane
	if (size() > 0) {
	    // if index>0 (there are tile/s before the adding-position) the joker will be
	    // set to the
	    // next value of the tile at index-1
	    if (tile.isJoker() && index > 0) {
		if (isGroup()) {
		    tile.setValue(tileAt(0).getValue());
		    tile.setColor(Tile.nextColor(tileAt(index - 1)));
		} else if (isRun()) {
		    tile.setValue(Tile.nextValue(tileAt(index - 1)));
		    tile.setColor(tileAt(index - 1).getColor());
		}
		// if index==0 (first position in lane) the joker will be set to the previous
		// value of the
		// tile, which is actualy at position 0
	    } else if (tile.isJoker() && index == 0) {
		if (isGroup()) {
		    tile.setValue(tileAt(0).getValue());
		    tile.setColor(Tile.previousColor(tileAt(0)));
		} else if (isRun()) {
		    tile.setValue(Tile.previousValue(tileAt(0)));
		    tile.setColor(tileAt(0).getColor());
		}
	    }
	}

	super.addTile(index, tile);

    }

    // returns true, if the lane is correct in the sense of the rummikub rules
    public boolean verify() {

	// if ammount of tiles in this lane is smaller than MIN_TILES_TO_VERIFY
	if (size() < Lane.MIN_TILES_TO_VERIFY)
	    return false;

	// sort tiles to the correct order of a lane
	this.sort();

	// if lane is a group... (i.e. [RED 1],[YELLOW 1],[BLUE 1])
	if (isGroup()) {

	    Color checkColor = tileAt(0).getColor();

	    for (int i = 1; i < size(); i++) {
		if (tileAt(i).getColor() == checkColor)
		    return false;
		checkColor = tileAt(i).getColor();
	    }

	    return true;

	    // if lane is a run...
	} else if (isRun()) {

	    int nextValue = tileAt(0).getValue();
	    // iterate over tiles and check if tiles are in coherent order

	    for (Tile t : super.tiles) {

		if (t.getValue() != nextValue) {
		    return false;
		}
		nextValue = Tile.nextValue(t);
	    }

	    return true;

	}

	return false;

    }

    public boolean isSplitable() {

	return size() >= (Lane.MIN_TILES_TO_VERIFY * 2) ? true : false;
    }

    public List<Tile> splitableTiles() {
	List<Tile> tmp = new ArrayList<>();

	if (isSplitable()) {

	    tmp = super.tiles.subList(Lane.MIN_TILES_TO_VERIFY, size() - Lane.MIN_TILES_TO_VERIFY);

	}

	return tmp;
    }

    // returns true, if lane could be/is a run... (all tiles are of the same color)
    // (i.e. [RED 1],[RED 2],[RED 3] (passes verify())
    // or [BLUE 3],[BLUE 6] (is also a run but fails verify())

    public boolean isRun() {

	if (size() >= 2) {

	    Color checkColor = tileAt(0).getColor();

	    for (int i = 1; i < size(); i++) {
		if (checkColor != tileAt(i).getColor()) {
		    return false;
		}
	    }

	    return true;

	} else if (size() == 1) {

	    return true;

	}

	return false;
    }

    // returns true, if lane could be/is a group. (all tiles are of the same value)
    // (i.e. [RED 1],[YELLOW 1],[BLUE 1] (passes verify())
    // or [RED 1],[YELLOW 1] (is also a group but fails verify())
    // or [RED 1],[RED 1],[BLUE 1] (is also a group but fails verify())

    public boolean isGroup() {

	if (size() >= 2) {

	    int checkValue = tileAt(0).getValue();

	    for (int i = 1; i < size(); i++) {
		if (checkValue != tileAt(i).getValue()) {
		    return false;

		}
	    }

	    return true;

	} else if (size() == 1) {

	    return true;

	} else {

	    return false;

	}

    }

    public void sort() {

	// if isGroup() = true, then just sort super.myLane by natural ordering of
	// Enum Color...
	// RED, YELLOW, BLUE, BLACK

	if (isGroup()) {

	    super.sort();
	    return;

	}
	
	// ... if not, maybe it's a run...
	
	if (isRun()) {

	    // first, sorting super.myTiles by natual ordering... ([RED 1], [RED 2], [RED 2],
	    // [RED 3],...)
	    super.sort();

	    // then, running a lane-conform ordering...

	    // create new ArrayList<ArrayList<Tile>>
	    ArrayList<ArrayList<Tile>> lists = new ArrayList<ArrayList<Tile>>();
	    // add an empty ArrayList<Tile> to lists
	    lists.add(new ArrayList<Tile>());

	    boolean match = false; // match indicator for the ordering algorithm

	    if (DEBUG) {
		System.out.println(super.tiles);
	    }

	    // iterate over tiles in this lane
	    for (Tile t : super.tiles) {

		match = false; // reset match indicator

		if (DEBUG) {
		    System.out.println("\nSearching " + t);
		}

		outer:
		// iterate over lists<tile> in lists
		for (ArrayList<Tile> tmpList : lists) {

		    if (DEBUG) {
			System.out.println("tmpList -> " + tmpList);
		    }

		    // iterate over tiles in tmpList
		    for (Tile tmpTile : tmpList) {

			// if a tile that equals t, allready exists in tmpList...
			if (t.equals(tmpTile)) {
			    if (DEBUG)
				System.out.println("match! -> " + t + " tmpTile -> " + tmpTile);

			    match = true;
			    break;

			} else {
			    match = false;
			}
		    }

		    if (!match) {

			if (DEBUG)
			    System.out.println("no match... add " + t + " to tmpList");

			tmpList.add(t);
			break outer;
		    }

		}
		if (match) {
		    // if tile was found in every tmpList, then a new ArrayList<Tile> is created
		    // in lists and the tile t is added in the new ArrayList<Tile>
		    if (DEBUG)
			System.out.println(
				t + " war in jeder Liste... also neue liste " + "erstellen und " + t + " adden");

		    lists.add(new ArrayList<Tile>());
		    lists.get(lists.size() - 1).add(t);
		}
		//
	    }

	    // clearing the lanes tiles
	    super.tiles.clear();

	    int count = 0; // just needed for DEBUG

	    // ordering each list in lists by natural ordering...
	    for (ArrayList<Tile> tmpList : lists) {

		tmpList.sort(null);

		if (DEBUG) {
		    count++;
		    System.out.println("tmpList " + count + " -> " + tmpList);
		}
		// ...and put it into this lanes tiles
		super.tiles.addAll(tmpList);
	    }

	    // finally, if nessisary, rotate tiles in this lane to get correct order
	    int sIndex = getStartIndex();
	    if (sIndex != 0) {
		Collections.rotate(super.tiles, -sIndex);
	    }
	}
    }

    private int getStartIndex() {

	// for example:
	// given two lanes
	// [RED 1], [RED 2], [RED 3], [RED 9], [RED 10], [RED 11]
	// [RED 4], [RED 5], [RED 6], [RED 1], [RED 2], [RED 3]
	//
	// <-|(end) |-> lane should start here (start-index)
	// [RED 1], [RED 2], [RED 3]..(biggestGap = 5)..[RED 9], [RED 10], [RED
	// 11]..(gap to first tile = 2)..
	// [RED 4], [RED 5], [RED 6]..(biggestGap = 7)..[RED 1], [RED 2], [RED 3]..(gap
	// to first tile = 0)..
	//
	// correct order:
	// [RED 9], [RED 10], [RED 11], [RED 1], [RED 2], [RED 3]
	// [RED 1], [RED 2], [RED 3], [RED 4], [RED 5], [RED 6]

	int biggestGap = 0;
	int position = 0;
	int difference = 0;

	if (size() > 1) {
	    for (int i = 1; i < size(); i++) {
		difference = Tile.getDifference(tileAt(i - 1), tileAt(i));
		if (difference > biggestGap) {
		    position = i;
		    biggestGap = difference;
		}
	    }

	    if (Tile.getDifference(tileAt(size() - 1), tileAt(0)) >= biggestGap) {
		return 0;
	    }

	    return position;
	}

	return 0;
    }

    @Override
    public String toString() {
	return super.toString();
    }

    @Override
    public Iterator<Tile> iterator() {

	return new TileItr(super.tiles);
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
}
