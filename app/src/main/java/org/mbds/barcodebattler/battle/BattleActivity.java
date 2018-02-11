package org.mbds.barcodebattler.battle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.mbds.barcodebattler.R;
import org.mbds.barcodebattler.data.Creature;
import org.mbds.barcodebattler.data.ICreature;

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

    //---------------------------

    // Player 2 stats view
    TextView P2StatsCreatureName;
    TextView P2HP;
    TextView P2ST;
    TextView P2DF;
    // Player 2 creature image
    TextView P2Name;
    ImageView P2Image;

    //TextView gameMsg;
    EditText gameMsg;

    Button buttonTurn;

    Battle battle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);


        List<ICreature> P1creatures = new ArrayList<ICreature>();
        ICreature s1 = new Creature("Beast Feast", 40, 500, 100, "beast_feast");
        ICreature s3 = new Creature("Jam Bam", 1000, 250, 350, "jam_bam");
        P1creatures.add(s1);
        P1creatures.add(s3);

        List<ICreature> P2creatures = new ArrayList<ICreature>();
        ICreature s2 = new Creature("Cool Candy", 550, 400, 200, "cool_candy");
        ICreature s6 = new Creature("Razor Fist", 1000, 450, 300, "razor_fist");
        P2creatures.add(s2);
        P2creatures.add(s6);

        battle = new Battle(P1creatures, P2creatures, this);


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
        switch (player) {
            case PLAYER1:
                P1StatsCreatureName.setText(battle.getPlayerCreatureName(Player.PLAYER1));
                P1Name.setText(battle.getPlayerCreatureName(Player.PLAYER1));
                P1HP.setText(Integer.toString(battle.getBattleState().getPlayer1CurrentCreature().getEnergy()));
                P1ST.setText(Integer.toString(battle.getBattleState().getPlayer1CurrentCreature().getStrike()));
                P1DF.setText(Integer.toString(battle.getBattleState().getPlayer1CurrentCreature().getDefense()));
                // TODO : les images

                break;
            case PLAYER2:
                P2StatsCreatureName.setText(battle.getPlayerCreatureName(Player.PLAYER2));
                P2Name.setText(battle.getPlayerCreatureName(Player.PLAYER2));
                P2HP.setText(Integer.toString(battle.getBattleState().getPlayer2CurrentCreature().getEnergy()));
                P2ST.setText(Integer.toString(battle.getBattleState().getPlayer2CurrentCreature().getStrike()));
                P2DF.setText(Integer.toString(battle.getBattleState().getPlayer2CurrentCreature().getDefense()));
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
}