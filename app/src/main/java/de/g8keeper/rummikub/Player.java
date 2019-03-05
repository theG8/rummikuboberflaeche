package de.g8keeper.rummikub;

import java.util.Objects;

public class Player {
    
    private static int count = 0;
    
    private String name;
    private TileSet tileSet;
    
    
    
    public Player(String name) {
	this.name = name;
	this.tileSet = new TileSet();
    }
    
    public Player(String name, TileSet tileSet) {
	this(name);
	this.tileSet = tileSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public void setTileSet(TileSet tileSet) {
        this.tileSet = tileSet;
    }

    @Override 
    public String toString() {
	return "\"" + this.name + "\":\n" + PrintUtils.getPrintableTiles(this.tileSet);
    }
    
    @Override
    public int hashCode() {
	return Objects.hash(this.name,this.tileSet);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof Player)) {
	    return false;
	}
	Player other = (Player) obj;
	if (name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!name.equals(other.name)) {
	    return false;
	}
	if (tileSet == null) {
	    if (other.tileSet != null) {
		return false;
	    }
	} else if (!tileSet.equals(other.tileSet)) {
	    return false;
	}
	return true;
    }
    
    
   
    
    
    
    
    
}