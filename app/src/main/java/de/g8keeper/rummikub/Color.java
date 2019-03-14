package de.g8keeper.rummikub;

import java.io.Serializable;

import de.g8keeper.rummikuboberflaeche.R;

public enum Color implements Serializable {
    RED("rot", R.color.tile_red),
    YELLOW("gelb", R.color.tile_yellow),
    BLUE("blau", R.color.tile_blue),
    BLACK("schwarz", R.color.tile_black);
    
    private String name;
    private int colorId;
    
    private Color(String name, int colorId) {
	      this.name = name;
	      this.colorId = colorId;
    }


    public int getColorId(){
        return this.colorId;
    }
    @Override
    public String toString() {
	    return this.name;
    }
    
}
