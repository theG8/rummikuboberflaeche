package de.g8keeper.rummikub;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.g8keeper.rummikub.database.DataSource;

public class Game implements Parcelable {
    private static final String TAG = Game.class.getSimpleName();

    public static final int STATE_NOT_STARTED = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_FINISH = 2;

    public static final int START_TILE_COUNT = 14;

    public static final Parcelable.Creator<Game> CREATOR =
            new Parcelable.Creator<Game>() {

                @Override
                public Game createFromParcel(Parcel source) {
                    return new Game(source);
                }

                @Override
                public Game[] newArray(int size) {
                    return new Game[size];
                }
            };


    private long mID;


    private String mTitle;
    private long mStartTime;
    private long mEndTime;
    private List<Player> mPlayers;
    private List<Lane> mLanes;
    private TilePool mPool;
    private int mPosActPlayer;

    private DataSource mDataSource;







    public Game(long id, String title) {
        this.mID = id;
        this.mTitle = title;
        this.mPlayers = new ArrayList<>();
        this.mLanes = new ArrayList<>();


    }

    public Game(long id, String title, long start, long end) {
        this(id, title);
        this.mStartTime = start;
        this.mEndTime = end;
    }

    public Game(Parcel parcel) {
        this.mID = parcel.readLong();
        this.mTitle = parcel.readString();
        this.mStartTime = parcel.readLong();
        this.mEndTime = parcel.readLong();

        mPlayers = new ArrayList<>();
        mLanes = new ArrayList<>();

        parcel.readTypedList(mPlayers, Player.CREATOR);
        parcel.readTypedList(mLanes, Lane.CREATOR);


        this.mPosActPlayer = parcel.readInt();

        buildPool();

    }

    public void setDataSource(DataSource dataSource){
        this.mDataSource = dataSource;
    }

    public void buildPool() {

        mPool = new TilePool();

        if (mPlayers != null) {
            for (Player player : mPlayers) {
                for (Tile tile : player.getTileSet()) {
                    mPool.removeTile(tile);
                }
            }
        }

        if (mLanes != null) {
            for (Lane lane : mLanes) {
                for (Tile tile : lane) {
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

    public long getId() {
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

    public void addPlayer(Player player) {
        Log.d(TAG, "addPlayer: " + player);

        if(state() == STATE_NOT_STARTED) {
            this.mPlayers.add(player);

            if (mDataSource != null) {
                mDataSource.addPlayerToGame(this, player);
            }
        } else {
            throw new RuntimeException("game is running/finished... adding player is permitted");
        }
    }

    private void addLane(Lane lane) {
        Log.d(TAG, "addLane: " + lane);
        this.mLanes.add(lane);

        if(mDataSource != null) {
            mDataSource.addLaneToGame(this, lane, this.mLanes.indexOf(lane));
        }
    }

    public void setPlayers(List<Player> players) {

        mPlayers = players;

    }

    public void setLanes(List<Lane> lanes) {

        mLanes = lanes;

    }

    public int getActualPlayer(){
        return mPosActPlayer;
    }
    public void setActualPlayer(int pos) {
        mPosActPlayer = pos;
    }

//    public void loadGameData() {
//        mPlayers = mDataSource.getGamePlayers(this);
//
//        mPosActPlayer = mDataSource.getGameActualPlayer(this);
//
//        mLanes = mDataSource.getGameLanes(this);
//
//        buildPool();
//
//    }
//
//    public void saveGameData() {
//
//
//    }

    public boolean hasPlayers() {
        return mPlayers.size() != 0;
    }




    public int state() {
        int state = 0;

        if (mStartTime == -1 && mEndTime == -1) {
            state = STATE_NOT_STARTED;
        } else if (mStartTime != -1 && mEndTime == -1) {
            state = STATE_RUNNING;
        } else if (mStartTime != 1 && mEndTime != -1) {
            state = STATE_FINISH;
        }

        return state;

    }

    public Turn getTurn(){

        List<Lane> lanes = new ArrayList<>();
        TileSet tileSet = new TileSet(mPlayers.get(mPosActPlayer).getTileSet());

        for(Lane l : mLanes){
            lanes.add(new Lane(l));
        }

        Turn turn = new Turn(tileSet, lanes);


        return turn;
    }



    private void saveGameState(){

        mDataSource.updateGame(this);


    }

    public void startGame(){
        if(state() == STATE_NOT_STARTED){

            mStartTime = 1;
            //
            buildPool();

            for(int i = 0; i < START_TILE_COUNT; i++) {
                for (Player player : mPlayers) {
                    player.getTileSet().addTile(mPool.getTile());
                }
            }
            mPosActPlayer = 0;

            saveGameState();
            Log.d(TAG, "startGame: " + toString(true));

        } else if (state() == STATE_RUNNING){
            throw new RuntimeException("game is running... startGame() is not allowed");
        } else if (state() == STATE_FINISH){
            throw new RuntimeException("game is finished... startGame() is not allowed");
        }
    }



    @NonNull
    @Override
    public String toString() {
        return "game(" + this.mID + ", " + this.mTitle + ", " +
                this.mStartTime + ", " + this.mEndTime + ", state: " + this.state() + ")";
    }

    public String toString(boolean b) {

        return "game(ID: " + this.mID + ", title: " + this.mTitle + ", start: " +
                this.mStartTime + ", end: " + this.mEndTime + ", actPlayer: " + this.mPosActPlayer + ", state: " + this.state() + ", " +
                "players: " + this.mPlayers + "\nLanes: " + this.mLanes + ")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mID);
        dest.writeString(mTitle);
        dest.writeLong(mStartTime);
        dest.writeLong(mEndTime);

        dest.writeTypedList(mPlayers);
        dest.writeTypedList(mLanes);

        dest.writeInt(mPosActPlayer);


    }


}
