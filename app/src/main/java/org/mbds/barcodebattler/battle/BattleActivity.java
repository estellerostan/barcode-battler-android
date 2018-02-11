package org.mbds.barcodebattler.battle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.mbds.barcodebattler.R;
import org.mbds.barcodebattler.data.ICreature;

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

        gameMsg = (TextView) findViewById(R.id.game_msg);

//        List<Creature> creaturesP1 = new ArrayList<Creature>();
        ICreature creature = getIntent().getExtras().getParcelable("creatureP1");

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
    }
}