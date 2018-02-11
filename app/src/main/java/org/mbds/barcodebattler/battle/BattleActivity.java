package org.mbds.barcodebattler.battle;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.mbds.barcodebattler.R;
import org.mbds.barcodebattler.data.Creature;
import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BarcodeBattlerDatabaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class BattleActivity extends AppCompatActivity {

    private static final String TAG = BattleActivity.class.getSimpleName();

    // Player 1 stats view
    TextView P1StatsCreatureName;
    TextView P1HP;
    TextView P1ST;
    TextView P1DF;
    // Player 1 creature image
    TextView P1Name;
    ImageView P1Image;

    List<ICreature> P1creatures;

    //---------------------------

    // Player 2 stats view
    TextView P2StatsCreatureName;
    TextView P2HP;
    TextView P2ST;
    TextView P2DF;
    // Player 2 creature image
    TextView P2Name;
    ImageView P2Image;

    List<ICreature> P2creatures;

    //TextView gameMsg;
    EditText gameMsg;

    ICreature creature;
    ICreature enemy;
    Button buttonTurn;

    Battle battle;
    BarcodeBattlerDatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        P1creatures = new ArrayList<>();

        P2creatures = new ArrayList<>();

        P1StatsCreatureName = (TextView) findViewById(R.id.monsterInfoP1);
        P2StatsCreatureName = (TextView) findViewById(R.id.monsterInfoP2);

        P1HP = (TextView) findViewById(R.id.P1MonsterHP);
        P1ST = (TextView) findViewById(R.id.P1monsterST);
        P1DF = (TextView) findViewById(R.id.P1monsterDF);
        P1Name = (TextView) findViewById(R.id.monsterNameP1);

        P2HP = (TextView) findViewById(R.id.P2MonsterHP);
        P2ST = (TextView) findViewById(R.id.P2monsterST);
        P2DF = (TextView) findViewById(R.id.P2monsterDF);
        P2Name = (TextView) findViewById(R.id.monsterNameP2);

        P1Image = (ImageView) findViewById(R.id.monsterImageP1);
        P2Image = (ImageView) findViewById(R.id.monsterImageP2);

        gameMsg = (EditText) findViewById(R.id.game_msg);
        gameMsg.setEnabled(false);

        databaseAdapter = new BarcodeBattlerDatabaseAdapter(getApplicationContext());
        databaseAdapter.open();

        buttonTurn = (Button) findViewById(R.id.btn_turn);
        buttonTurn.setText(R.string.btn_start);


        buttonTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (battle.getBattleState().getPlayerTurn()) {
                    case NONE:
                        if ( battle.getBattleWinner() == Player.NONE ) {
                            Log.d(TAG, "[game] Click Button START");
                            battle.startBattle();
                        } else {
                            Log.d(TAG, "[game] Close battle");
                            BattleActivity.this.finish();
                        }
                        break;
                    case PLAYER1:
                        if ( battle.isTurnEnded() ) {
                            Log.d(TAG, "Current P1 turn ended");
                            battle.nextTurn();
                        } else {
                            Log.d(TAG, "Current P1 turn");
                            battle.playTurn(Player.PLAYER1);
                        }

                        break;
                    case PLAYER2:
                        Log.d(TAG, "Click player2");
                        break;
                }
            }
        });

        // -- View Component init : --
        setPlayerCreature(Player.PLAYER1);
        setPlayerCreature(Player.PLAYER2);

    }

    public void setPlayerCreature(Player player) {

        String mDrawableN;
        int resI;
        Bitmap bitma;

        switch (player) {
            case PLAYER1:
                // Load P! from various sources
                if (getIntent().getExtras() != null) {
                    creature = getIntent().getExtras().getParcelable("creatureP1");

                    if (creature != null) {
                        P1StatsCreatureName.setText(creature.getName());
                        P1HP.setText(String.valueOf(creature.getEnergy()));
                        P1ST.setText(String.valueOf(creature.getStrike()));
                        P1DF.setText(String.valueOf(creature.getDefense()));

                        String mDrawableName = creature.getImageName();
                        int resID = getResources().getIdentifier(mDrawableName, "drawable", getApplicationContext().getPackageName());

                        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                resID);
                        P1Image.setImageBitmap(bitmap);

                        P1creatures.add(creature);

                        // TOOD: inserer en db le match courant ?
                        Toast.makeText(this, "Début du combat", Toast.LENGTH_LONG).show();

                        // TODO: make it work
//                        battle = new Battle(P1creatures, P2creatures, this);
                    }
                } else {
                    LoadPreferences();
                    Toast.makeText(this, "Reprise du combat", Toast.LENGTH_LONG).show();
                }

                break;
            case PLAYER2:
                enemy = databaseAdapter.getEnemy();
                // Load P2
                if (enemy != null) {
                    P2StatsCreatureName.setText(enemy.getName());
                    P2HP.setText(String.valueOf(enemy.getEnergy()));
                    P2ST.setText(String.valueOf(enemy.getStrike()));
                    P2DF.setText(String.valueOf(enemy.getDefense()));

                    mDrawableN = enemy.getImageName();
                    resI = getResources().getIdentifier(mDrawableN, "drawable", getApplicationContext().getPackageName());

                    bitma = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            resI);
                    P2Image.setImageBitmap(bitma);

                    P2creatures.add(enemy);
                } else {
                    LoadPreferences();
                }
                break;
        }
    }

    public void updateGameMSG(String s) {
        //gameMsg.setText(s);
        gameMsg.append("\n" + s);
        gameMsg.setSelection(gameMsg.length());
    }

    public Button getButtonTurn() {
        return buttonTurn;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        if (creature != null) {
            savedInstanceState.putString("name", creature.getName());
            savedInstanceState.putInt("energy", creature.getEnergy());
            savedInstanceState.putInt("strike", creature.getStrike());
            savedInstanceState.putInt("defense", creature.getDefense());
            savedInstanceState.putString("imageName", creature.getImageName());
        }
    }

    private void SavePreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor creatures = sharedPreferences.edit();
        creatures.putString("hero_name", this.creature.getName());
        creatures.putInt("hero_energy", this.creature.getEnergy());
        creatures.putInt("hero_strike", this.creature.getStrike());
        creatures.putInt("hero_defense", this.creature.getDefense());
        creatures.putString("hero_imageName", this.creature.getImageName());

        creatures.putString("enemy_name", this.enemy.getName());
        creatures.putInt("enemy_energy", this.enemy.getEnergy());
        creatures.putInt("enemy_strike", this.enemy.getStrike());
        creatures.putInt("enemy_defense", this.enemy.getDefense());
        creatures.putString("enemy_imageName", this.enemy.getImageName());
        creatures.apply();
    }

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        if (sharedPreferences.getString("hero_name", null) != null) {
            P1StatsCreatureName.setText(sharedPreferences.getString("hero_name", null));
            P1HP.setText(String.valueOf(sharedPreferences.getInt("hero_energy", 0)));
            P1ST.setText(String.valueOf(sharedPreferences.getInt("hero_strike", 0)));
            P1DF.setText(String.valueOf(sharedPreferences.getInt("hero_defense", 0)));
            String mDrawableName = sharedPreferences.getString("hero_imageName", null);
            int resID = getResources().getIdentifier(mDrawableName, "drawable", getApplicationContext().getPackageName());

            Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    resID);
            P1Image.setImageBitmap(bitmap);
        }

        if (sharedPreferences.getString("enemy_name", null) != null) {
            P2StatsCreatureName.setText(sharedPreferences.getString("enemy_name", null));
            P2HP.setText(String.valueOf(sharedPreferences.getInt("enemy_energy", 0)));
            P2ST.setText(String.valueOf(sharedPreferences.getInt("enemy_strike", 0)));
            P2DF.setText(String.valueOf(sharedPreferences.getInt("enemy_defense", 0)));
            String name = sharedPreferences.getString("enemy_imageName", null);
            int res = getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());

            Bitmap abitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    res);
            P2Image.setImageBitmap(abitmap);
        }
    }

    @Override
    public void onBackPressed() {
        if (creature != null) {
            SavePreferences();
        }
        super.onBackPressed();
    }
}