package de.g8keeper.rummikub;

import java.util.ArrayList;

public class LaneDemo {

    public static void main(String[] args) {
	// TODO Auto-generated method stub

	Lane l1 = new Lane();
	Lane l2 = new Lane();
	Lane l3 = new Lane();
	Lane l4 = new Lane();
	Lane l5 = new Lane();
	Lane l6 = new Lane();
	Lane l7 = new Lane();
	Lane l8 = new Lane();
	Lane l9 = new Lane();
	Lane l10 = new Lane();
	Lane l11 = new Lane();

	l1.addTile(new Tile(Color.RED, 7));
	l1.addTile(new Tile(Color.RED, 9));
	l1.addTile(new Tile(Color.RED, 8));

	l2.addTile(new Tile(Color.RED, 10));
	l2.addTile(new Tile(Color.BLUE, 10));
	l2.addTile(new Tile(Color.YELLOW, 10));

	l3.addTile(new Tile(Color.BLACK, 7));
	l3.addTile(new Tile(Color.BLACK, 9));
	l3.addTile(new Tile(Color.BLACK, 10));

	l4.addTile(new Tile(Color.RED, 3));
	l4.addTile(new Tile(Color.BLUE, 3));
	l4.addTile(new Tile(Color.YELLOW, 3));
	l4.addTile(new Tile(Color.RED, 3));

	l5.addTile(new Tile(Color.RED, 7));
	l5.addTile(new Tile(Color.RED, 9));
	l5.addTile(new Tile(Color.RED, 8));
	l5.addTile(new Tile(Color.YELLOW, 10));

	l6.addTile(new Tile(Color.RED, 5));
	l6.addTile(new Tile(Color.BLUE, 5));
	
	l6.addTile(new Tile(Color.BLACK, 5));
	l6.addTile(1,new Tile(true));
//	l6.addTile(new Tile(Color.BLACK, 5));
	
	
	l7.addTile(new Tile(Color.RED, 7));
	l7.addTile(new Tile(Color.BLUE, 7));

	l8.addTile(new Tile(Color.BLUE, 11));
	l8.addTile(new Tile(Color.BLUE, 12));

	
	l9.addTile(new Tile(Color.RED, 1));
	l9.addTile(new Tile(Color.RED, 2));
	
	l9.addTile(new Tile(Color.RED, 11));
	l9.addTile(new Tile(Color.RED, 12));

//	l9.addTile(new Tile(Color.RED, 13));
	
	
	l10.addTile(new Tile(Color.RED, 1));
	l10.addTile(new Tile(Color.RED, 2));
	l10.addTile(new Tile(Color.RED, 1));
	l10.addTile(new Tile(Color.RED, 2));
//	l10.addTile(new Tile(Color.RED, 1));
//	l10.addTile(new Tile(Color.RED, 2));
//	l10.addTile(new Tile(Color.RED, 3));
//	l10.addTile(new Tile(Color.RED, 4));
	l10.addTile(new Tile(Color.RED, 5));
	l10.addTile(new Tile(Color.RED, 6));
	l10.addTile(new Tile(Color.RED, 7));
	l10.addTile(new Tile(Color.RED, 8));
	l10.addTile(new Tile(Color.RED, 9));
	l10.addTile(new Tile(Color.RED, 10));
	l10.addTile(new Tile(Color.RED, 11));
	l10.addTile(new Tile(Color.RED, 12));
	l10.addTile(new Tile(Color.RED, 13));
	l10.addTile(new Tile(Color.RED, 3));
	l10.addTile(new Tile(Color.RED, 4));
	l10.addTile(new Tile(Color.RED, 5));
	l10.addTile(new Tile(Color.RED, 6));
	l10.addTile(new Tile(Color.RED, 7));
	l10.addTile(new Tile(Color.RED, 8));
	l10.addTile(new Tile(Color.RED, 9));
	l10.addTile(new Tile(Color.RED, 10));
	l10.addTile(new Tile(Color.RED, 11));
	l10.addTile(new Tile(Color.RED, 12));
	l10.addTile(new Tile(Color.RED, 13));
	
	l11.addTile(new Tile(Color.RED, 1));
	l11.addTile(new Tile(Color.RED, 1));
	l11.addTile(new Tile(Color.RED, 1));
	l11.addTile(new Tile(Color.RED, 2));
	l11.addTile(new Tile(Color.RED, 2));
	l11.addTile(new Tile(Color.RED, 2));
	l11.addTile(new Tile(Color.RED, 3));
	l11.addTile(new Tile(Color.RED, 3));
	l11.addTile(new Tile(Color.RED, 3));
	
	ArrayList<Lane> lanes = new ArrayList<Lane>();

//	lanes.add(l1);
//	lanes.add(l2);
//	lanes.add(l3);
//	lanes.add(l4);
//	lanes.add(l5);
//	lanes.add(l6);
	lanes.add(l7);
	lanes.add(l8);
	lanes.add(l9);
	lanes.add(l10);
//	lanes.add(l11);
	
	
	for (int i = 0; i < lanes.size(); i++) {
	    
	    System.out.println("Lane" + (i+1) + " -> \t" + lanes.get(i));
	    lanes.get(i).sort();
	}
	System.out.println();

	l9.addTile(2,new Tile(true));
	
	
	for (int i = 0; i < lanes.size(); i++) {
	    System.out.println("l" + (i+1) + " -> \t" + lanes.get(i));
	    System.out.println("l" + (i+1) + ".isRun ->\t\t" + lanes.get(i).isRun());
	    System.out.println("l" + (i+1) + ".isGroup() ->\t\t" + lanes.get(i).isGroup());
	    System.out.println("l" + (i+1) + ".verify() ->\t\t" + lanes.get(i).verify());

	    System.out.println("l" + (i+1) + ".getPoints() ->\t" + lanes.get(i).getPoints());
	    System.out.println("l" + (i+1) + ".isSplitable() ->\t" + lanes.get(i).isSplitable());
	    if (lanes.get(i).isSplitable()) System.out.println("l" + (i+1) + ".splitableTiles() ->\t" + lanes.get(i).splitableTiles());
	    System.out.println();
	    
	}

	for (int i = 0; i < lanes.size(); i++) {
	    PrintUtils.PrintTiles(lanes.get(i), i);
	}
	
//	for (Tile t: lanes.get(0)) {
//	    System.out.println(t);
//	}

    }

}
