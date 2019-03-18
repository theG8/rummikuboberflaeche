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

    public static final int MOVE_RESULT_INVALID = 0;
    public static final int MOVE_RESULT_VALID = 1;
    public static final int MOVE_RESULT_WIN = 2;

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

    public void setDataSource(DataSource dataSource) {
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


    //TODO remove!
    public void setmStartTime(){
        mStartTime = 1;
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

        if (state() == STATE_NOT_STARTED) {
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

        if (mDataSource != null) {
            mDataSource.addLaneToGame(this, lane, this.mLanes.indexOf(lane));
        }
    }

    public void setPlayers(List<Player> players) {

        mPlayers = players;

    }

    public void setLanes(List<Lane> lanes) {

        mLanes = lanes;

    }

    public int getActualPlayer() {
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

    public Move getNextMove() {

        List<Lane> lanes = new ArrayList<>();
        TileSet tileSet = new TileSet(mPlayers.get(mPosActPlayer).getTileSet());

        for (Lane l : mLanes) {
            lanes.add(new Lane(l));
        }

        Move move = new Move(tileSet, lanes);


        return move;
    }


    private void saveGameState() {

        Log.d("DEBUG", "saving gamestate\n");

        mDataSource.updateGame(this);


    }

    public void startGame() {
        if (state() == STATE_NOT_STARTED) {

            mStartTime = 1;
            //
            buildPool();

            for (int i = 0; i < START_TILE_COUNT; i++) {
                for (Player player : mPlayers) {
                    player.getTileSet().addTile(mPool.getTile());
                }
            }
            mPosActPlayer = 0;

            saveGameState();
            Log.d("DEBUG", "startGame: " + toString(true));

        } else if (state() == STATE_RUNNING) {
            throw new RuntimeException("game is running... startGame() is not allowed");
        } else if (state() == STATE_FINISH) {
            throw new RuntimeException("game is finished... startGame() is not allowed");
        }
    }

    public int makeMove(Move move) {

        StringBuilder sb = new StringBuilder("makeMove:\n\n");
        boolean result = true;
        boolean hasPlayedTiles = false;
        int moveResult = MOVE_RESULT_INVALID;

        //durch rückwärts-durchlaufen ist löschen von leeren lanes möglich
        for(int i = move.lanes().size()-1; i>=0;i--){
            if(move.lanes().get(i).isEmpty()){
                move.lanes().remove(i);
            } else {
                if (move.lanes().get(i).verify()){
                    sb.append(move.lanes().get(i) + " is valid\n");
                } else {
                    sb.append(move.lanes().get(i) + " IS NOT VALID\n");
                    moveResult = MOVE_RESULT_INVALID;
                    result = false;
                }
            }
        }



        sb.append("\n");

        if(result) {

            if (move.tileSet().equals(mPlayers.get(mPosActPlayer).getTileSet())) {
                // player played no tile
                hasPlayedTiles = false;

                sb.append("player played NO tiles\n\n");
            } else {
                // player has played tile/s
                hasPlayedTiles = true;
                sb.append("player played tiles\n\n");
            }


        }

        if(result){

            sb.append("set lanes and tileset from move to game\n\n");
            setLanes(move.lanes());
            getPlayers().get(mPosActPlayer).setTileSet(move.tileSet());

            if(!hasPlayedTiles){
                // player gets new tile from pool
                sb.append("player got a new tile\n\n");
                getPlayers().get(mPosActPlayer).getTileSet().addTile(mPool.getTile());
            }


            if(getPlayers().get(mPosActPlayer).getTileSet().size() == 0){
                sb.append("players tileset-size is 0... PLAYER WINS!\n\n");
                moveResult = MOVE_RESULT_WIN;
                mEndTime = 2;

            } else {
                sb.append("move was valid... set mPosActPlayer to next player\n\n");
                moveResult = MOVE_RESULT_VALID;
                mPosActPlayer = getNextPlayer();
            }
            sb.append("savinng game state\n\n");
            saveGameState();
        }


        Log.d("DEBUG", sb.toString());

        return moveResult;
    }


    private int getNextPlayer() {
        if (mPosActPlayer < (mPlayers.size() - 1)) {
            return mPosActPlayer + 1;
        } else if (mPosActPlayer == (mPlayers.size() -1)){
            return 0;
        } else {
            throw new RuntimeException();
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
