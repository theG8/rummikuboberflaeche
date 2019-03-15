package de.g8keeper.rummikub;

import java.io.Serializable;

import de.g8keeper.rummikuboberflaeche.R;

public enum Color implements Serializable {
    RED(
            "red",
            R.color.tile_red
    ),

    YELLOW(
            "yellow",
            R.color.tile_yellow
    ),

    BLUE(
            "blue",
            R.color.tile_blue
    ),

    BLACK(
            "black",
            R.color.tile_black
    );

    private String name;
    private int colorId;


    private Color(String name, int colorId) {
        this.name = name;
        this.colorId = colorId;
    }

    public byte bitmask() {
        return (byte) (1 << (this.ordinal() + 4));
    }

    public int colorId() {
        return this.colorId;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
