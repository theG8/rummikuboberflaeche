package de.g8keeper.rummikub;

import java.util.Objects;

public class Tile implements Comparable<Tile>, IEvaluable {

    public static final int MAX_VALUE;
    public static final int MIN_VALUE;

    static {
        // set the highest/lowest value, a tile can get
        MAX_VALUE = 13;
        MIN_VALUE = 1;
    }


    private Color color; // the color-value of the tile
    private int value; // the number-value of the tile
    private boolean joker = false; // represents the joker-state of this tile


    // use this constructor, to setup a normal (not joker) tile
    public Tile(Color color, int value) {
        // set joker-state (for settig tiles value and color)
        this.joker = true;
        // set value and color of the tile
        setValue(value);
        setColor(color);

        // revoke the joker-state; make it a normal tile
        this.joker = false;
    }

    // when creating a joker-tile, use this constructor
    public Tile(boolean isJoker) {
        // set joker-state
        this.joker = true;
        // initialize value and color to [RED 1]. later, in a lane, these values are adjusted on the fly
        // to the value this joker-tile represents
        setColor(Color.RED);
        setValue(MIN_VALUE);
    }

    // a static function to get the next int-value in tiles ordering 
    // (n+1; if (n = MAX_VALUE) n = MIN_VALUE) 
    public static int nextValue(int value) throws IllegalArgumentException {

        if (value > MAX_VALUE || value < MIN_VALUE)
            throw new IllegalArgumentException(
                    "value muss zwischen MIN_VALUE(" + MIN_VALUE + ") und MAX_VALUE (" + MAX_VALUE + ") liegen");

        if (value == MAX_VALUE) {
            return MIN_VALUE;
        } else {
            return value + 1;
        }

    }

    public static int nextValue(Tile tile) throws IllegalArgumentException {
        return nextValue(tile.getValue());

    }

    // a static function to get the previous int-value in tiles ordering 
    // (n-1; if (n = MIN_VALUE) n = MAX_VALUE)
    public static int previousValue(int value) throws IllegalArgumentException {

        if (value > MAX_VALUE || value < MIN_VALUE)
            throw new IllegalArgumentException(
                    "value muss zwischen MIN_VALUE(" + MIN_VALUE + ") und MAX_VALUE (" + MAX_VALUE + ") liegen");

        if (value == MIN_VALUE) {
            return MAX_VALUE;
        } else {
            return value - 1;
        }

    }

    public static int previousValue(Tile tile) throws IllegalArgumentException {
        return previousValue(tile.getValue());
    }

    // a static function to get the next color in tiles ordering 
    // if color = BLACK (last color in colors) returns RED (first color in colors)
    public static Color nextColor(Color color) {

        if (color.ordinal() == Color.values().length - 1) {
            return Color.values()[0];
        } else {
            return Color.values()[color.ordinal() + 1];
        }

    }

    public static Color nextColor(Tile tile) {
        return nextColor(tile.getColor());
    }

    // a static function to get the previous color in tiles ordering 
    // if color = RED (first color in colors) returns BLACK (last color in colors)
    public static Color previousColor(Color color) {

        if (color.ordinal() == 0) {
            return Color.values()[Color.values().length - 1];
        } else {
            return Color.values()[color.ordinal() - 1];
        }

    }

    public static Color previousColor(Tile tile) {
        return previousColor(tile.getColor());
    }

    // returns the difference between two values of tiles.
    // in fact it is the difference from a, ascending to b. this means that, when b<a, 
    // the difference is ((a to MAX_VALUE) + (MIN_VALUE to b))
    public static int getDifference(int a, int b) {
        int count = 1;

        for (int i = nextValue(a); i != b; i = nextValue(i)) {

            count++;
        }

        return count;
    }

    public static int getDifference(Tile tileA, Tile tileB) {

        return getDifference(tileA.getValue(), tileB.getValue());
    }

    // returns the color of the tile
    public Color getColor() {
        return this.color;
    }

    // sets, if tile is joker, the color of the tile
    public void setColor(Color color) {
        // setting a color to a tile is just valid, when tile is a joker (isJoker())
        if (isJoker()) {
            this.color = color;
        } else {
            throw new IllegalArgumentException("Farbe kann nur gesetzt werden, wenn Spielstein Joker ist!");
        }
    }

    // returns the value of the tile
    public int getValue() {
        return this.value;
    }

    // sets, if tile is joker, the value of the tile
    public void setValue(int value) throws IllegalArgumentException {

        // setting a value to a tile is just valid, when tile is a joker (isJoker())
        if (isJoker()) {
            // if value is between bounds (MIN_VALUE and MAX_VALUE; inclusive) set the new value
            if (value <= MAX_VALUE && value >= MIN_VALUE) {
                this.value = value;
                // ...if not, throw exception
            } else {
                throw new IllegalArgumentException("Spielstein darf nur einen Wert zwischen 1 und 13 besitzen!");
            }
            // ...if not, throw exception
        } else {
            throw new IllegalArgumentException("Wert kann nur gesetzt werden, wenn Spielstein Joker ist!");
        }
    }

    // returns true, if tile is a joker, or false, if not
    public boolean isJoker() {

        return this.joker;
    }


    @Override
    public String toString() {

//	return !isJoker() ? "[" + this.color() + " " + this.value() + "]" : "[Joker(" + this.color() + " " + this.value() + ")]";
        return !isJoker() ? "Tile(" + this.getColor() + " " + this.getValue() + ")" : "Tile(Joker)";
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.color, this.value);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        if (compareTo((Tile) obj) != 0) {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(Tile o) {

        /*  natural ordering of tiles in rummikub is:
         *
         *  [red 1], [red 2], ..., [red 13] followed by [YELLOW 1], ...,[YELLOW 13] followed by
         *  [BLUE 1], ...,[BLUE 13] followed by [BLACK 1], ...,[BLACK 13]
         */

        // if this.color and o.color are different, return the result of the comparision of
        // this.color and o.color
        // if this.color and o.color are equal, return the result of the (integer-)comparision of
        // this.value and o.value

        return this.getColor().compareTo(o.getColor()) == 0 ? Integer.compare(this.getValue(), o.getValue())
                : this.getColor().compareTo(o.getColor());

    }

    @Override
    public int getPoints() {
        // returns the point-value of the tile. for normal tiles this is similar to the value of the tile.
        // but if tile is a joker, its point-value is 20
        return isJoker() ? 20 : getValue();
    }
}
