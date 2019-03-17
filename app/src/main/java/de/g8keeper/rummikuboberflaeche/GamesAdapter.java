package de.g8keeper.rummikuboberflaeche;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.g8keeper.rummikub.Game;

public class GamesAdapter extends ArrayAdapter<Game> {

    private static final String TAG = GamesAdapter.class.getSimpleName();



    private int resourceView;
    private Context context;
    private List<Game> mGames;
    private boolean[] mCheckedStates;
    private boolean mCheckable;

    @Override
    public void notifyDataSetChanged() {
        if (mCheckedStates.length != mGames.size()){
            mCheckedStates = new boolean[mGames.size()];
        }

        super.notifyDataSetChanged();
    }

    public void setCheckable(boolean flag){
        mCheckable = flag;
    }
    public void setItemChecked(int itemID, boolean checked){
        mCheckedStates[itemID] = checked;
    }

    public GamesAdapter(@NonNull Context context, int resource, @NonNull List<Game> objects) {
        super(context, resource, objects);

        this.resourceView = resource;
        this.context  = context;
        this.mGames = objects;

        mCheckedStates = new boolean[mGames.size()];

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = ((Activity) context).getLayoutInflater().inflate(resourceView,parent,false);

        Log.d(TAG, "getView: ");

        TextView tvStatus = convertView.findViewById(R.id.li_games_tv_status);
        TextView tvTitle = convertView.findViewById(R.id.li_games_tv_title);
        TextView tvId = convertView.findViewById(R.id.li_games_tv_id);
        ImageView ivCheck = convertView.findViewById(R.id.li_games_iv_check);

        if(mCheckable) {

            ivCheck.setVisibility(View.VISIBLE);
            if (mCheckedStates[position]) {
                ivCheck.setImageDrawable(getContext().getDrawable(android.R.drawable.checkbox_on_background));
                //
            } else {
                ivCheck.setImageDrawable(getContext().getDrawable(android.R.drawable.checkbox_off_background));
            }
        } else {
            ivCheck.setVisibility(View.INVISIBLE);
        }

        Game game = mGames.get(position);

        tvTitle.setText(game.getTitle());
        tvId.setText("ID: " + game.getId() + " players: " + game.getPlayers());

        String[] state = context.getResources().getStringArray(R.array.game_status);

        tvStatus.setText(state[game.state()]);


        return convertView;
    }



}
