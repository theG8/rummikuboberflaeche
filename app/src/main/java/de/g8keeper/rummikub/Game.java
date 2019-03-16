package de.g8keeper.rummikub;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final String TAG = Game.class.getSimpleName();


    private long mID;


    private String mTitle;
    private long mStartTime;
    private long mEndTime;
    private List<Player> mPlayers;
    private List<Lane> mLanes;
    private TilePool mPool;
    private long mIDActualPlayer;


    public Game(long id, String title) {
        this.mID = id;
        this.mTitle = title;
        mPlayers = new ArrayList<>();
        mLanes = new ArrayList<>();
    }


    public void addPlayer(Player player){

    }



    private void buildPool() {

        mPool = new TilePool();

        if (mPlayers != null) {
            for (Player player : mPlayers) {
                for (Tile tile : player.getTileSet()) {
                    mPool.removeTile(tile);
                }
            }
        }

        if(mLanes != null){
            for(Lane lane: mLanes){
                for (Tile tile : lane){
                    mPool.removeTile(tile);
                }
            }
        }
    }


    public long getId(){
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }


    public TilePool getPool() {
        return mPool;
    }

    public void setPool(TilePool pool) {
        this.mPool = pool;
    }

    public List<Lane> getLanes() {
        return mLanes;
    }

    public void setLanes(List<Lane> lanes) {
        this.mLanes = lanes;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    public void setPlayers(List<Player> players) {
        this.mPlayers = players;
    }
}
