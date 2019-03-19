package de.g8keeper.rummikuboberflaeche;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.g8keeper.rummikub.Player;

public class PlayerAdapter extends ArrayAdapter<Player> {

    private static final String TAG = PlayerAdapter.class.getSimpleName();


    private int mResourceView;
    private Context mContext;
    private List<Player> mPlayers;
    private boolean[] mCheckedStates;
    private boolean mCheckable;


    @Override
    public void notifyDataSetChanged() {
        Log.d(TAG, "notifyDataSetChanged: ");
        if (mCheckedStates.length != mPlayers.size()){
            mCheckedStates = new boolean[mPlayers.size()];
        }

        super.notifyDataSetChanged();

    }

    public void setCheckable(boolean flag){
        mCheckable = flag;
    }
    public void setItemChecked(int itemID, boolean checked){
        mCheckedStates[itemID] = checked;
        notifyDataSetChanged();
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


        if(mCheckable) {

            ivCheck.setVisibility(View.VISIBLE);
            if (mCheckedStates[position]) {
                // erst ab level 23
                //                ivCheck.setImageDrawable(getContext().getDrawable(android.R.drawable.checkbox_on_background));
                // für tablet
                ivCheck.setImageDrawable(ContextCompat.getDrawable(getContext(),android.R.drawable.checkbox_on_background));
            } else {
//                ivCheck.setImageDrawable(getContext().getDrawable(android.R.drawable.checkbox_off_background));
                ivCheck.setImageDrawable(ContextCompat.getDrawable(getContext(),android.R.drawable.checkbox_off_background));
            }
        } else {
            ivCheck.setVisibility(View.INVISIBLE);
        }



        tvName.setText(mPlayers.get(position).getName());
        tvId.setText("ID: " + mPlayers.get(position).getId());

        return convertView;
    }


    public List<Player> getSelectedPlayers() {
        List<Player> players = new ArrayList<>();

        for(int i = 0; i < mCheckedStates.length; i++){
            if (mCheckedStates[i]){
                players.add(mPlayers.get(i));
            }
        }

        return players;
    }
}
