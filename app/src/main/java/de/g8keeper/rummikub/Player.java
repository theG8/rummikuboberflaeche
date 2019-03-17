package de.g8keeper.rummikub;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable, Parcelable {


    public static final Parcelable.Creator<Player> CREATOR =
            new Parcelable.Creator<Player>(){

                @Override
                public Player createFromParcel(Parcel source) {
                    return new Player(source);
                }

                @Override
                public Player[] newArray(int size) {
                    return new Player[size];
                }
            };

    private static int count = 0;

    private long mId;
    private String mName;
    private TileSet mTileSet;





    public Player(long id, String name) {
	    this.mId = id;
        this.mName = name;
        this.mTileSet = new TileSet();
    }

    public Player(long id, String name, TileSet tileSet) {
        this(id, name);
        this.mTileSet = tileSet;
    }

    public Player(Player player){
        this(player.mId, new String(player.mName), new TileSet(player.mTileSet));
    }


    public long getId() {
        return mId;
    }

//	public void setId(long id) {
//		this.mId = id;
//	}

    public String getName() {
        return mName;
    }

//    public void setName(String name) {
//        this.mName = name;
//    }

    public TileSet getTileSet() {
        return mTileSet;
    }






    public void setTileSet(TileSet tileSet) {
        this.mTileSet = tileSet;
    }

    @Override
    public String toString() {

        return "Player(" + this.mId + ", " + this.mName + ")";
    }

    public String toStringWithTiles() {

        return "Player(" + this.mId + ", " + this.mName + ") {" + this.mTileSet + "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.mName, this.mTileSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Player)) {
            return false;
        }
        Player other = (Player) obj;
        if (mName == null) {
            if (other.mName != null) {
                return false;
            }
        } else if (!mName.equals(other.mName)) {
            return false;
        }
        if (mTileSet == null) {
            if (other.mTileSet != null) {
                return false;
            }
        } else if (!mTileSet.equals(other.mTileSet)) {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(mName);
        byte[] bytearray = mTileSet.toBytearray();
        dest.writeInt(bytearray.length);
        dest.writeByteArray(mTileSet.toBytearray());
    }

    public Player(Parcel parcel){
        this.mId = parcel.readLong();
        this.mName = parcel.readString();
        byte[] tileSet = new byte[parcel.readInt()];
        parcel.readByteArray(tileSet);
        this.mTileSet = new TileSet(tileSet);
    }
}
