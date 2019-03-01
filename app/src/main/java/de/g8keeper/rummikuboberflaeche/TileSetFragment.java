package de.g8keeper.rummikuboberflaeche;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import de.g8keeper.rummikub.TileSet;

public class TileSetFragment extends Fragment {

    private static final String TAG = TileSetFragment.class.getSimpleName();


    private TileSet myTiles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = new LinearLayout(getContext());


        ((LinearLayout) view).setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        );

        int pad = getResources().getDimensionPixelOffset(R.dimen.padding_standard);
        view.setPadding(20 ,20,20,20);
        ((LinearLayout) view).setOrientation(LinearLayout.HORIZONTAL);

        view.setMinimumWidth(10);
        view.setMinimumHeight(10);

        view.setBackgroundColor(Color.RED);

        return view;

    }

    public static TileSetFragment newInstance(TileSet tileSet) {
        TileSetFragment fragment = new TileSetFragment();
        fragment.setTileSet(tileSet);

        return fragment;
    }


    private void setTileSet(TileSet tileSet) {
        if (myTiles == null) {
            myTiles = tileSet;
        }
    }

    public TileSet getTileSet() {
        return myTiles;
    }

}
