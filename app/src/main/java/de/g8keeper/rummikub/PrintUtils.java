package de.g8keeper.rummikub;

public class PrintUtils {

    public static int LINE_WIDTH;
    public static String SPLITTER;
    public static String SPACES;
    
    static {
	LINE_WIDTH = 105;
	SPLITTER = "|";
	SPACES = "          ";
    }
    
    
    public static void PrintTiles(Object tiles) {
	
	PrintTiles(tiles,0);
	
    }
    
    public static void PrintTiles(Object tiles, int nr) {
	
	System.out.println(getPrintableTiles(tiles,nr));
	
    }
    
    public static String getPrintableTiles(Object tiles) {
	return getPrintableTiles(tiles,0);
    }
    
    public static String getPrintableTiles(Object tiles, int nr) {
	
	
	TileSet lot = (TileSet) tiles;
	
	StringBuilder head = new StringBuilder();
	StringBuilder body = new StringBuilder();
	
	StringBuilder tileLine = new StringBuilder();
	StringBuilder indexLine = new StringBuilder();
	
	String tmpTile;
	String tmpIndex;
	
	int index = 1;
	
	head.append("\\\\ ");
	
	if (tiles instanceof Lane) {
	    head.append("REIHE " + (nr +1) );
	    
	} else {
	    head.append("SPIELSTEINE");
	}
	
	head.append(" =>\n");
	
	
	for(Tile tile: lot) {
	
	    tmpTile = SPLITTER + tile.toString();
	    tmpIndex = SPLITTER + " " + index++;
	    tmpIndex += SPACES.substring(0, tmpTile.length() - tmpIndex.length());
	    
	    if ((tileLine.length() + tmpTile.length()) < LINE_WIDTH) {
		tileLine.append(tmpTile);
		indexLine.append(tmpIndex);
	    } else {
		body.append(".").append(tileLine).append(SPLITTER).append("\n");
		body.append(".").append(indexLine).append(SPLITTER).append("\n");
		tileLine.delete(0, tileLine.length()).append(tmpTile);
		indexLine.delete(0, indexLine.length()).append(tmpIndex);
	    }
	    
	}
	
	if (tileLine.length() > 0) {
	    body.append(".").append(tileLine).append(SPLITTER).append("\n");
	    body.append(".").append(indexLine).append(SPLITTER).append("\n");
	}
	
	head.append(body).append("//\n");
	
	return head.toString();
//	System.out.print(head);
	
    }
}
