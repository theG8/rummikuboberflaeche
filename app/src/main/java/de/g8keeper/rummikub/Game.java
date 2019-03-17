package de.g8keeper.rummikub;

import android.support.annotation.NonNull;
import android.util.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.g8keeper.rummikub.database.DataSource;

public class Game {
    private static final String TAG = Game.class.getSimpleName();

    public static final int STATE_NOT_STARTED = 1;
    public static final int STATE_RUNNING = 2;
    public static final int STATE_ENDED = 3;


    private long mID;


    private String mTitle;
    private long mStartTime;
    private long mEndTime;
    private List<Player> mPlayers;
    private List<Lane> mLanes;
    private TilePool mPool;
    private int mIDActualPlayer;

    private DataSource dataSource;



    public Game(long id, String title, DataSource dataSource) {
        this.mID = id;
        this.mTitle = title;
        this.mPlayers = new ArrayList<>();
        this.mLanes = new ArrayList<>();

        this.dataSource = dataSource;
    }

    public Game(long id, String title, long start, long end, DataSource dataSource){
        this(id,title, dataSource);
        this.mStartTime = start;
        this.mEndTime = end;
    }

    public Game(Game game){
        this.mID = game.mID;
        this.mTitle = new String(game.mTitle);
        this.mStartTime = game.mEndTime;
        this.mEndTime = game.mEndTime;

        this.mPlayers = new ArrayList<>();
        for(Player p: game.mPlayers){
            this.mPlayers.add(p);
        }

        this.mLanes = new ArrayList<>();
        for(Lane l: game.mLanes){
            this.mLanes.add(l);
        }

        this.mPool = new TilePool(game.mPool);
        this.mIDActualPlayer = game.mIDActualPlayer;

        this.dataSource = game.dataSource;
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


    public long getStartTime() {
        return mStartTime;
    }

    public long getEndTime() {
        return mEndTime;
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
        return Collections.unmodifiableList(mLanes);
    }

    public List<Player> getPlayers() {

        return Collections.unmodifiableList(mPlayers);
    }

    public void addPlayer(Player player){
        Log.d(TAG, "addPlayer: " + player);
        this.mPlayers.add(player);

        dataSource.addPlayerToGame(this,player);
    }

    public void addLane(Lane lane) {
        Log.d(TAG, "addLane: " + lane);
        this.mLanes.add(lane);

        dataSource.addLaneToGame(this,lane,this.mLanes.indexOf(lane));

    }


    public void loadGameData(){
        mPlayers = dataSource.getGamePlayers(this);
        mIDActualPlayer = dataSource.getGameActualPlayer(this);

        mLanes = dataSource.getGameLanes(this);

    }


    public Turn getActualTurn(){
        List<Lane> lanes = new ArrayList<>();

        for(Lane l: this.mLanes){
            lanes.add(new Lane(l));
        }

        Turn turn = new Turn(
                new TileSet(this.mPlayers.get(this.mIDActualPlayer).getTileSet()),
                lanes
        );

        return turn;
    }


    public int state(){
        int state = 0;

        if(mStartTime == -1 && mEndTime == -1){
            state = STATE_NOT_STARTED;
        } else if (mStartTime != -1 && mEndTime == -1){
            state = STATE_RUNNING;
        } else if (mStartTime != 1 && mEndTime != -1){
            state = STATE_ENDED;
        }

        return state;

    }



    @NonNull
    @Override
    public String toString() {
        return "game(" + this.mID + ", " + this.mTitle + ", " +
                this.mStartTime + ", " + this.mEndTime + ", state: " + this.state() + ")";
    }

    public String toString(boolean b){

        return "game(" + this.mID + ", " + this.mTitle + ", " +
                this.mStartTime + ", " + this.mEndTime + ", state: " + this.state() + ", " +
                "players: " + this.mPlayers + "\nLanes: " + this.mLanes + ")";
    }


}
