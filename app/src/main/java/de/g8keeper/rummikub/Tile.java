package de.g8keeper.rummikub;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class Tile implements Comparable<Tile>, IEvaluable, Serializable {

    public static final byte BYTE_JOKER = 15; // -> 00001111

    public static final int MAX_VALUE;
    public static final int MIN_VALUE;

    static {
        // set the highest/lowest mValue, a tile can get
        MAX_VALUE = 13;
        MIN_VALUE = 1;
    }


    private Color mColor; // the mColor-mValue of the tile
    private int mValue; // the number-mValue of the tile
    private boolean mIsJoker = false; // represents the mIsJoker-state of this tile


    // use this constructor, to setup a normal (not mIsJoker) tile
    public Tile(Color color, int value) {
        // set mIsJoker-state (for settig mTiles mValue and mColor)
        this.mIsJoker = true;
        // set mValue and mColor of the tile
        setValue(value);
        setColor(color);


        this.mIsJoker = false;
    }

    // when creating a mIsJoker-tile, use this constructor
    public Tile(boolean isJoker) {
        // set mIsJoker-state
        this.mIsJoker = true;
        // initialize mValue and mColor to [RED 1]. later, in a lane, these values are adjusted on the fly
        // to the mValue this mIsJoker-tile represents
        setColor(Color.RED);
        setValue(MIN_VALUE);
    }

    // generates a random Tile
    public Tile(){

        if((int) (Math.random() * 53) == 1){
            this.mIsJoker = true;
            setColor(Color.RED);
            setValue(MIN_VALUE);
        } else {
            this.mIsJoker = true;
            setColor(Color.values()[(int) (Math.random() * 4)]);
            setValue(((int) (Math.random() * MAX_VALUE - MIN_VALUE)) + MIN_VALUE + 1);
            this.mIsJoker = false;
        }

    }

    public Tile(byte b) {
        Color color = null;
        int value = 0;

        if (b == BYTE_JOKER) {

            this.mIsJoker = true;
            setColor(Color.RED);
            setValue(MIN_VALUE);

        } else {

            for (Color c : Color.values()) {

                if ((b & c.bitmask()) != 0) {
                    color = c;
                    break;
                }
            }

            if (color == null) {
                throw new IllegalArgumentException("error in Tile(byte)-constructor");
            }

            b = (byte) (b ^ color.bitmask());
            if (b > 0 && b <= 13) {
                value = b;
            } else {
                throw new IllegalArgumentException("error in Tile(byte)-constructor. b(" + b + ") is not > 0 && <= 13");
            }

            this.mIsJoker = true;
            setValue(value);
            setColor(color);
            this.mIsJoker = false;

        }


    }

    public Tile(Tile tile){
        this.mColor = tile.getColor();
        this.mValue = tile.getValue();
    }


    private static byte getByteRepresentation(Tile tile) {

        /*
        	Byte-Representation eines Tiles

            			(B)  (A)
		                /    /
            bits	1111 1111
            nr->	8... ...1

        	(A) -> tile-value (1-13)
	        (B) -> tile-color (RED(16), YELLOW(32), BLUE(64), BLACK(-127))
	        JOKER = 15 (00001111)
         */

        byte tmp = 0;

        if (tile.isJoker()) {
            tmp = BYTE_JOKER;
        } else {
            tmp = (byte) tile.getValue();

            tmp = (byte) (tile.getColor().bitmask() | tmp);
        }

        return tmp;
    }

    // a static function to get the next int-mValue in mTiles ordering
    // (n+1; if (n = MAX_VALUE) n = MIN_VALUE) 
    public static int nextValue(int value) throws IllegalArgumentException {

        if (value > MAX_VALUE || value < MIN_VALUE)
            throw new IllegalArgumentException(
                    "mValue muss zwischen MIN_VALUE(" + MIN_VALUE + ") und MAX_VALUE (" + MAX_VALUE + ") liegen");

        if (value == MAX_VALUE) {
            return MIN_VALUE;
        } else {
            return value + 1;
        }

    }

    public static int nextValue(Tile tile) throws IllegalArgumentException {
        return nextValue(tile.getValue());

    }

    // a static function to get the previous int-mValue in mTiles ordering
    // (n-1; if (n = MIN_VALUE) n = MAX_VALUE)
    public static int previousValue(int value) throws IllegalArgumentException {

        if (value > MAX_VALUE || value < MIN_VALUE)
            throw new IllegalArgumentException(
                    "mValue muss zwischen MIN_VALUE(" + MIN_VALUE + ") und MAX_VALUE (" + MAX_VALUE + ") liegen");

        if (value == MIN_VALUE) {
            return MAX_VALUE;
        } else {
            return value - 1;
        }

    }

    public static int previousValue(Tile tile) throws IllegalArgumentException {
        return previousValue(tile.getValue());
    }

    // a static function to get the next mColor in mTiles ordering
    // if mColor = BLACK (last mColor in colors) returns RED (first mColor in colors)
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

    // a static function to get the previous mColor in mTiles ordering
    // if mColor = RED (first mColor in colors) returns BLACK (last mColor in colors)
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

    // returns the difference between two values of mTiles.
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

    // returns the mColor of the tile
    public Color getColor() {
        return this.mColor;
    }

    // sets, if tile is mIsJoker, the mColor of the tile
    public void setColor(Color color) {
        // setting a mColor to a tile is just valid, when tile is a mIsJoker (isJoker())
        if (isJoker()) {
            this.mColor = color;
        } else {
            throw new IllegalArgumentException("Farbe kann nur gesetzt werden, wenn Spielstein Joker ist!");
        }
    }

    // returns the mValue of the tile
    public int getValue() {
        return this.mValue;
    }

    // sets, if tile is mIsJoker, the mValue of the tile
    public void setValue(int value) throws IllegalArgumentException {

        // setting a mValue to a tile is just valid, when tile is a mIsJoker (isJoker())
        if (isJoker()) {
            // if mValue is between bounds (MIN_VALUE and MAX_VALUE; inclusive) set the new mValue
            if (value <= MAX_VALUE && value >= MIN_VALUE) {
                this.mValue = value;
                // ...if not, throw exception
            } else {
                throw new IllegalArgumentException("Spielstein darf nur einen Wert zwischen 1 und 13 besitzen!");
            }
            // ...if not, throw exception
        } else {
            throw new IllegalArgumentException("Wert kann nur gesetzt werden, wenn Spielstein Joker ist!");
        }
    }

    // returns true, if tile is a mIsJoker, or false, if not
    public boolean isJoker() {

        return this.mIsJoker;
    }

    public byte toByte() {
        return getByteRepresentation(this);
    }


    @Override
    public String toString() {

//	return !isJoker() ? "[" + this.mColor() + " " + this.mValue() + "]" : "[Joker(" + this.mColor() + " " + this.mValue() + ")]";
        return !isJoker() ? "(" + this.getColor() + " " + this.getValue() + ")" : "(Joker)";
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.mColor, this.mValue);
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

        /*  natural ordering of mTiles in rummikub is:
         *
         *  [red 1], [red 2], ..., [red 13] followed by [YELLOW 1], ...,[YELLOW 13] followed by
         *  [BLUE 1], ...,[BLUE 13] followed by [BLACK 1], ...,[BLACK 13]
         */

        // if this.mColor and o.mColor are different, return the result of the comparision of
        // this.mColor and o.mColor
        // if this.mColor and o.mColor are equal, return the result of the (integer-)comparision of
        // this.mValue and o.mValue

        return this.getColor().compareTo(o.getColor()) == 0 ? Integer.compare(this.getValue(), o.getValue())
                : this.getColor().compareTo(o.getColor());

    }

    @Override
    public int getPoints() {
        // returns the point-mValue of the tile. for normal mTiles this is similar to the mValue of the tile.
        // but if tile is a mIsJoker, its point-mValue is 20
        return isJoker() ? 20 : getValue();
    }
}
