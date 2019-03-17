package de.g8keeper.rummikub;

import android.support.annotation.NonNull;

import java.util.List;

public class Turn {

    private TileSet mTileSet;
    private List<Lane> mLanes;


    public Turn(TileSet tileSet, List<Lane> lanes) {
        this.mTileSet = tileSet;
        this.mLanes = lanes;
    }

    public Turn(Turn turn) {
        this.mTileSet = new TileSet(turn.mTileSet);

        for (Lane l : turn.mLanes) {
            this.mLanes.add(new Lane(l));
        }
    }


    public TileSet tileSet(){
        return this.mTileSet;
    }
    public List<Lane> lanes(){
        return this.mLanes;
    }

    @NonNull
    @Override
    public String toString() {
        return "Turn(" + this.mTileSet + ", " + this.mLanes + ")";
    }
}
