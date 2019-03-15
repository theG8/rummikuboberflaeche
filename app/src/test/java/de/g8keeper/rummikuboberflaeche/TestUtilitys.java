package de.g8keeper.rummikuboberflaeche;

public class TestUtilitys {



    public static String toBinaryString(byte zahl) {
        StringBuilder sb = new StringBuilder(8);
        // StringBuilder wäre hier etwas besser... denn StringBuffer ist
        // thread-save und das ist hier nicht nötig

        for (int n = (1 << 7); n != 0; n >>>= 1) {

            sb.append((zahl & n) != 0 ? 1 : 0);
        }
        return sb.toString();
    }

    public static String toBinaryString(int zahl) {
        StringBuilder sb = new StringBuilder(32);
        // StringBuilder wäre hier etwas besser... denn StringBuffer ist
        // thread-save und das ist hier nicht nötig

        for (int n = (1 << 31); n != 0; n >>>= 1) {

            sb.append((zahl & n) != 0 ? 1 : 0);
        }
        return sb.toString();
    }

}
