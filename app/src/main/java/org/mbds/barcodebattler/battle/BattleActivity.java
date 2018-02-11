package org.mbds.barcodebattler.battle;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.mbds.barcodebattler.R;
import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BarcodeBattlerDatabaseAdapter;

public class BattleActivity extends AppCompatActivity {

    // Player 1 stats view
    TextView P1StatsCreatureName;
    TextView P1HP;
    TextView P1ST;
    TextView P1DF;
    // Player 1 creature image
    ImageView P1Image;

    //---------------------------

    // Player 2 stats view
    TextView P2StatsCreatureName;
    TextView P2HP;
    TextView P2ST;
    TextView P2DF;
    // Player 2 creature image
    ImageView P2Image;

    TextView gameMsg;

    BattleState battle;
    ICreature creature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        P1StatsCreatureName = (TextView) findViewById(R.id.monsterInfoP1);
        P2StatsCreatureName = (TextView) findViewById(R.id.monsterInfoP2);

        P1HP = (TextView) findViewById(R.id.P1MonsterHP);
        P1ST = (TextView) findViewById(R.id.P1monsterST);
        P1DF = (TextView) findViewById(R.id.P1monsterDF);
        P2HP = (TextView) findViewById(R.id.P2MonsterHP);
        P2ST = (TextView) findViewById(R.id.P2monsterST);
        P2DF = (TextView) findViewById(R.id.P2monsterDF);

        P1Image = (ImageView) findViewById(R.id.monsterImageP1);
        P2Image = (ImageView) findViewById(R.id.monsterImageP2);

        // TODO: a deplacer + sauvegarde du monstre choisit au debut du combat
        BarcodeBattlerDatabaseAdapter databaseAdapter = new BarcodeBattlerDatabaseAdapter(getApplicationContext());
        databaseAdapter.open();

        ICreature enemy = databaseAdapter.getEnemy();

        P2StatsCreatureName.setText(enemy.getName());
        P2HP.setText(String.valueOf(enemy.getEnergy()));
        P2ST.setText(String.valueOf(enemy.getStrike()));
        P2DF.setText(String.valueOf(enemy.getDefense()));

        String mDrawableN = enemy.getImageName();
        int resI = getResources().getIdentifier(mDrawableN, "drawable", getApplicationContext().getPackageName());

        Bitmap bitma = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                resI);
        P2Image.setImageBitmap(bitma);

        gameMsg = (TextView) findViewById(R.id.game_msg);

//        List<Creature> creaturesP1 = new ArrayList<Creature>();
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

                // TOOD: inserer en db le match courant ?
                Toast.makeText(this, "Début du combat", Toast.LENGTH_LONG).show();
            }
        } else if (savedInstanceState != null) {
            P1StatsCreatureName.setText(savedInstanceState.getString("name"));
            P1HP.setText(savedInstanceState.getInt("energy"));
            P1ST.setText(savedInstanceState.getInt("strike"));
            P1DF.setText(savedInstanceState.getInt("defense"));
            String mDrawableName = savedInstanceState.getString("imageName");
            int resID = getResources().getIdentifier(mDrawableName, "drawable", getApplicationContext().getPackageName());

            Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    resID);
            P1Image.setImageBitmap(bitmap);
            Toast.makeText(this, "Reprise du combat", Toast.LENGTH_LONG).show();
        } else {
            LoadPreferences();
            Toast.makeText(this, "Reprise du combat", Toast.LENGTH_LONG).show();
        }
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
        SharedPreferences.Editor creature = sharedPreferences.edit();
        creature.putString("name", this.creature.getName());
        creature.putInt("energy", this.creature.getEnergy());
        creature.putInt("strike", this.creature.getStrike());
        creature.putInt("defense", this.creature.getDefense());
        creature.putString("imageName", this.creature.getImageName());
        creature.apply();   // I missed to save the data to preference here,.
    }

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        P1StatsCreatureName.setText(sharedPreferences.getString("name", null));
        P1HP.setText(String.valueOf(sharedPreferences.getInt("energy", 0)));
        P1ST.setText(String.valueOf(sharedPreferences.getInt("strike", 0)));
        P1DF.setText(String.valueOf(sharedPreferences.getInt("defense", 0)));
        String mDrawableName = sharedPreferences.getString("imageName", null);
        int resID = getResources().getIdentifier(mDrawableName, "drawable", getApplicationContext().getPackageName());

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                resID);
        P1Image.setImageBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        if (creature != null) {
            SavePreferences();
        }
        super.onBackPressed();
    }
}