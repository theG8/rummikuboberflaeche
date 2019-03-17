package de.g8keeper.rummikub;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.g8keeper.rummikub.database.DataSource;

public class Game implements Parcelable {
    private static final String TAG = Game.class.getSimpleName();

    public static final int STATE_NOT_STARTED = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_ENDED = 2;

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
    private int mIDActualPlayer;

    private DataSource dataSource;







    public Game(long id, String title, DataSource dataSource) {
        this.mID = id;
        this.mTitle = title;
        this.mPlayers = new ArrayList<>();
        this.mLanes = new ArrayList<>();

        this.dataSource = dataSource;
    }

    public Game(long id, String title, long start, long end, DataSource dataSource) {
        this(id, title, dataSource);
        this.mStartTime = start;
        this.mEndTime = end;
    }

    public Game(Parcel parcel) {
        this.mTitle = parcel.readString();
        this.mStartTime = parcel.readLong();
        this.mEndTime = parcel.readLong();

        mPlayers = new ArrayList<>();
        mLanes = new ArrayList<>();

        parcel.readTypedList(mPlayers, Player.CREATOR);
        parcel.readTypedList(mLanes, Lane.CREATOR);

        this.mIDActualPlayer = parcel.readInt();

        buildPool();

    }

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
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
        this.mPlayers.add(player);

        dataSource.addPlayerToGame(this, player);
    }

    public void addLane(Lane lane) {
        Log.d(TAG, "addLane: " + lane);
        this.mLanes.add(lane);

        dataSource.addLaneToGame(this, lane, this.mLanes.indexOf(lane));

    }

    public void setPlayers(List<Player> players) {

        mPlayers = players;

    }

    public void setLanes(List<Lane> lanes) {

        mLanes = lanes;

    }

    public void setActualPlayer(int id) {
        mIDActualPlayer = id;
    }

    public void loadGameData() {
        mPlayers = dataSource.getGamePlayers(this);

        mIDActualPlayer = dataSource.getGameActualPlayer(this);

        mLanes = dataSource.getGameLanes(this);

        buildPool();

    }

    public void saveGameData() {


    }

    public boolean hasPlayers() {
        return mPlayers.size() != 0;
    }

    public Turn getActualTurn() {
        List<Lane> lanes = new ArrayList<>();

        for (Lane l : this.mLanes) {
            lanes.add(new Lane(l));
        }

        Turn turn = new Turn(
                new TileSet(this.mPlayers.get(this.mIDActualPlayer).getTileSet()),
                lanes
        );

        return turn;
    }


    public int state() {
        int state = 0;

        if (mStartTime == -1 && mEndTime == -1) {
            state = STATE_NOT_STARTED;
        } else if (mStartTime != -1 && mEndTime == -1) {
            state = STATE_RUNNING;
        } else if (mStartTime != 1 && mEndTime != -1) {
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

    public String toString(boolean b) {

        return "game(" + this.mID + ", " + this.mTitle + ", " +
                this.mStartTime + ", " + this.mEndTime + ", state: " + this.state() + ", " +
                "players: " + this.mPlayers + "\nLanes: " + this.mLanes + ")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeLong(mStartTime);
        dest.writeLong(mEndTime);

        dest.writeTypedList(mPlayers);
        dest.writeTypedList(mLanes);

        dest.writeInt(mIDActualPlayer);


    }


}
