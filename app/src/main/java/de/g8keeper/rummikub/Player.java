package de.g8keeper.rummikub;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
    
    private static int count = 0;

    private long id;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

		return "Player(" + this.id + ", " + this.name + ")";
    }

	public String toStringWithTiles() {

		return "Player(" + this.id + ", " + this.name + ") {" + this.tileSet + "}";
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
