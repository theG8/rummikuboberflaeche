package de.g8keeper.rummikub;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Move implements Parcelable {

    public static final Parcelable.Creator<Move> CREATOR =
            new Parcelable.Creator<Move>() {

                @Override
                public Move createFromParcel(Parcel source) {
                    return new Move(source);
                }

                @Override
                public Move[] newArray(int size) {
                    return new Move[size];
                }
            };


    private TileSet mTileSet;
    private List<Lane> mLanes;


    public Move(Parcel parcel){
        this.mTileSet = parcel.readParcelable(TileSet.class.getClassLoader());
        this.mLanes = new ArrayList<>();
        parcel.readTypedList(this.mLanes, Lane.CREATOR);

    }

    public Move(TileSet tileSet, List<Lane> lanes) {
        this.mTileSet = tileSet;
        this.mLanes = lanes;
    }

    public Move(Move move) {
        this.mTileSet = new TileSet(move.mTileSet);

        for (Lane l : move.mLanes) {
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
        return "Move(" + this.mTileSet + ", " + this.mLanes + ")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mTileSet,flags);
        dest.writeTypedList(mLanes);
    }
}
