package de.g8keeper.rummikuboberflaeche;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import de.g8keeper.rummikub.Player;

public class PlayerAdapter extends ArrayAdapter<Player> {


    private int resourceView;
    private Context context;
    private List<Player> players;




    public PlayerAdapter(@NonNull Context context, int resource, @NonNull List<Player> objects) {
        super(context, resource, objects);

        this.resourceView = resource;
        this.context  = context;
        this.players = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = ((Activity) context).getLayoutInflater().inflate(resourceView,parent,false);



        TextView tvName = convertView.findViewById(R.id.li_tv_name);
        TextView tvId = convertView.findViewById(R.id.li_tv_id);

        tvName.setText(players.get(position).getName());
        tvId.setText("ID: " + players.get(position).getId());

        return convertView;
    }



}
