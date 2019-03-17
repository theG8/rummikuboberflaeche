package de.g8keeper.rummikuboberflaeche;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.g8keeper.rummikub.Game;

public class GamesAdapter extends ArrayAdapter<Game> {

    private static final String TAG = GamesAdapter.class.getSimpleName();



    private int resourceView;
    private Context context;
    private List<Game> games;




    public GamesAdapter(@NonNull Context context, int resource, @NonNull List<Game> objects) {
        super(context, resource, objects);

        this.resourceView = resource;
        this.context  = context;
        this.games = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = ((Activity) context).getLayoutInflater().inflate(resourceView,parent,false);

        Log.d(TAG, "getView: ");

        TextView tvStatus = convertView.findViewById(R.id.li_games_tv_status);
        TextView tvTitle = convertView.findViewById(R.id.li_games_tv_title);
        TextView tvId = convertView.findViewById(R.id.li_games_tv_id);

        Game game = games.get(position);

        tvTitle.setText(game.getTitle());
        tvId.setText("ID: " + game.getId() + " players: " + game.getPlayers());

        String[] state = context.getResources().getStringArray(R.array.game_status);

        tvStatus.setText(state[game.state()]);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + game.getTitle() + " " + game.getPlayers());

                if(game.hasPlayers()){

                    Intent intent = new Intent(getContext(), PlaygroundActivity.class);
                    intent.putExtra("game", game.getId());
                    getContext().startActivity(intent);

                } else {




                }





            }
        });

        return convertView;
    }



}
