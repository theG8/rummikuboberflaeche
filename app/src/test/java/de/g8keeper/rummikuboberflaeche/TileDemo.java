package de.g8keeper.rummikuboberflaeche;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Tile;

public class TileDemo {

    public static void main(String[] args) {
        Tile a = new Tile(Color.BLACK, 12);
        Tile b = new Tile(Color.BLACK, 12);

        System.out.println("Tile a: " + a);
        System.out.println("Tile b: " + b);
        System.out.println("getDifference -> " + Tile.getDifference(a, b));
        System.out.println("a equals b -> \t" + a.equals(b));
        System.out.println("a compareTo b -> \t" + a.compareTo(b));
        System.out.println("a.hashCode -> " + a.hashCode());
        System.out.println("b.hashCode -> " + b.hashCode());



        System.out.println("\nbit-representation:\n\n");

        for (Color c : Color.values()) {
            for (int v = 1; v <= 13; v++) {
                Tile tile = new Tile(c, v);
                System.out.print(tile.toString() + "\t -> " + TestUtilitys.toBinaryString(tile.toByte()) + " =\t");
                System.out.println(new Tile(tile.toByte()));
            }
        }

        Tile tile = new Tile(true);
        System.out.print(tile.toString() + "\t -> " + TestUtilitys.toBinaryString(tile.toByte()) + " =\t");
        System.out.println(new Tile(tile.toByte()));

    }

}
