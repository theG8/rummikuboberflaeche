package de.g8keeper.rummikub;

import java.util.List;

public class Game {
    private static final String TAG = Game.class.getSimpleName();

    private TilePool pool;
    private List<Lane> lanes;
    private List<Player> players;


    public Game(List<Lane> lanes, List<Player> players) {
        this.lanes = lanes;
        this.players = players;
    }

    public TilePool getPool() {
        return pool;
    }

    public void setPool(TilePool pool) {
        this.pool = pool;
    }

    public List<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(List<Lane> lanes) {
        this.lanes = lanes;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
