package de.g8keeper.rummikuboberflaeche;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.g8keeper.rummikub.Tile;

public class TileFragment extends Fragment {
    private static final String TAG = TileFragment.class.getSimpleName();


    private Tile myTile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tile, container, false);

        TextView tvText = view.findViewById(R.id.tv_text);


        if (!myTile.isJoker()) {
            tvText.setText(Integer.toString(myTile.getValue()));
            tvText.setTextColor(ContextCompat.getColor(getContext(), myTile.getColor().getColorId()));
        } else {
            tvText.setText("J");
            tvText.setTextColor(ContextCompat.getColor(getContext(), R.color.tile_joker));
        }


        view.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Tile " + this.myTile.toString() + " wurde geklickt", Toast.LENGTH_SHORT).show();
        });



        return view;

    }


    public static TileFragment newInstance(Tile tile) {
        TileFragment fragment = new TileFragment();
        fragment.setTile(tile);

        return fragment;
    }


    private void setTile(Tile tile) {
        if (myTile == null) {
            myTile = tile;
        }
    }

    public Tile getTile() {
        return myTile;
    }


}
