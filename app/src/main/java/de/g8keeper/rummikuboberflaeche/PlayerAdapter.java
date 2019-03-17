package de.g8keeper.rummikuboberflaeche;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.g8keeper.rummikub.Player;

public class PlayerAdapter extends ArrayAdapter<Player> {

    private static final String TAG = PlayerAdapter.class.getSimpleName();

    public static final int MODE_NORMAL = 0;
    public static final int MODE_CHOOSABLE = 1;


    private int mMode;
    private int mResourceView;
    private Context mContext;
    private List<Player> mPlayers;
    private boolean[] mCheckedStates;


    @Override
    public void notifyDataSetChanged() {
        Log.d(TAG, "notifyDataSetChanged: ");
        if (mCheckedStates.length != mPlayers.size()){
            mCheckedStates = new boolean[mPlayers.size()];
        }

        super.notifyDataSetChanged();

    }

    public void setItemChecked(int itemID, boolean checked){
        mCheckedStates[itemID] = checked;
    }

    public PlayerAdapter(@NonNull Context context, int resource, @NonNull List<Player> objects) {
        super(context, resource, objects);

        this.mResourceView = resource;
        this.mContext = context;
        this.mPlayers = objects;


            mCheckedStates = new boolean[mPlayers.size()];


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = ((Activity) mContext).getLayoutInflater().inflate(mResourceView,parent,false);


        ConstraintLayout layout = convertView.findViewById(R.id.li_players_cl_ground);
        TextView tvName = convertView.findViewById(R.id.li_players_tv_name);
        TextView tvId = convertView.findViewById(R.id.li_players_tv_id);
        ImageView ivCheck = convertView.findViewById(R.id.li_players_iv_check);


        if(mCheckedStates[position]){
            ivCheck.setImageDrawable(getContext().getDrawable(android.R.drawable.checkbox_on_background));
//
        } else {
            ivCheck.setImageDrawable(getContext().getDrawable(android.R.drawable.checkbox_off_background));
        }


        Log.d(TAG, "getView: tvName: " + tvName.getText());

        tvName.setText(mPlayers.get(position).getName());
        tvId.setText("ID: " + mPlayers.get(position).getId());

        return convertView;
    }



}
