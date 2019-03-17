package de.g8keeper.rummikuboberflaeche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view) {

        Intent intent;

        switch (view.getId()){

            case R.id.btn_start_game:
                intent = new Intent(this, PlaygroundActivity.class);
                startActivity(intent);


                break;

            case R.id.btn_configure_players:

                intent = new Intent(this, PlayersActivity.class);
                startActivity(intent);

                break;

            case R.id.btn_config:

                intent = new Intent(this, TestActivity.class);
                startActivity(intent);

                Toast.makeText(this, "Einstellungen wurde gedrückt", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
