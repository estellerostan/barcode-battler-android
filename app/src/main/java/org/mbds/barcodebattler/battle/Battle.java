package org.mbds.barcodebattler.battle;

import android.util.Log;

import org.mbds.barcodebattler.R;
import org.mbds.barcodebattler.data.ICreature;

import java.util.List;
import java.util.Random;

/**
 * Created by michael on 10/02/2018.
 */

public class Battle {

    private static final String TAG = Battle.class.getSimpleName();


    private BattleState battleState;
    private boolean roundEnded;
    private boolean battleEnded;
    private boolean turnEnded;

    private BattleActivity activity;

    public Battle(List<ICreature> player1Creatures, List<ICreature> player2Creatures, BattleActivity activity) {
        this.battleState = new BattleState(player1Creatures, player2Creatures);
        this.activity = activity;
        roundEnded = false;
        battleEnded = false;
        turnEnded = false;
    }

    // > Demarre une battle
    public void startBattle() {
        Log.d(TAG, "[game] Starting a new Battle");
        battleState.startTurn();
        String msg = activity.getResources().getString(R.string.msg_turn, Player.PLAYER1);
        Log.d(TAG, "[game] new Msg : "+msg);
        //activity.updateGameMSG(msg);
        activity.getButtonTurn().setText(R.string.btn_turn);
    }

    // > Joue les actions d'un joueur
    public void playTurn(Player player) {

        int dmg = 0;
        boolean died = false;
        String msg = "";

        switch (player) {
            case PLAYER1:
                msg = activity.getResources().getString(R.string.msg_turn, Player.PLAYER1);
                activity.updateGameMSG(msg);

                dmg = caculDamage(Player.PLAYER2);
                died = battleState.canDieFromDamage(Player.PLAYER2, dmg);
                disableActionButton();

                battleState.applyDamage(Player.PLAYER2, dmg);
                activity.setPlayerCreature(Player.PLAYER2);
                msg = activity.getResources().getString(R.string.msg_damage, player, dmg);
                activity.updateGameMSG(msg);

                if ( died ) {
                    roundEnded = true;
                }

                turnEnded = true;
                enableEndTurnButton();
                break;

            case PLAYER2: //bot
                msg = activity.getResources().getString(R.string.msg_turn, Player.PLAYER2);
                activity.updateGameMSG(msg);

                dmg = caculDamage(Player.PLAYER1);
                died = battleState.canDieFromDamage(Player.PLAYER1, dmg);
                disableActionButton();

                battleState.applyDamage(Player.PLAYER1, dmg);
                activity.setPlayerCreature(Player.PLAYER1);
                msg = activity.getResources().getString(R.string.msg_damage, player, dmg);
                activity.updateGameMSG(msg);

                if ( died ) {
                    roundEnded = true;
                }

                turnEnded = true;
                nextTurn();
                break;
        }
    }

    // > UI
    private void disableActionButton() {
        Log.d(TAG, "[game] DisableTurnButton");
        activity.getButtonTurn().setEnabled(false);
        activity.getButtonTurn().setText("...");
        activity.getButtonTurn().setBackgroundColor(activity.getResources().getColor(R.color.btn_turn_off));
    }

    // > UI
    private void enableEndTurnButton() {
        Log.d(TAG, "[game] EndTurnButton");
        activity.getButtonTurn().setEnabled(true);
        activity.getButtonTurn().setText(R.string.btn_end_turn);
        activity.getButtonTurn().setBackgroundColor(activity.getResources().getColor(R.color.btn_turn));
    }

    // > UI
    private void enableQuitTurnButton() {
        Log.d(TAG, "[game] enableQuitButton");
        activity.getButtonTurn().setEnabled(true);
        activity.getButtonTurn().setText(R.string.btn_quit);
        activity.getButtonTurn().setBackgroundColor(activity.getResources().getColor(R.color.black));
    }

    // > UI
    private void enableNextTurnButton() {
        Log.d(TAG, "[game] NextTurnButton");
        activity.getButtonTurn().setEnabled(true);
        activity.getButtonTurn().setText(R.string.btn_turn);
        activity.getButtonTurn().setBackgroundColor(activity.getResources().getColor(R.color.btn_turn));
    }

    // > UI
    private void enableSummonTurnButton() {
        Log.d(TAG, "[game] SummonButton");
        activity.getButtonTurn().setEnabled(true);
        activity.getButtonTurn().setText(R.string.btn_summon_next);
        activity.getButtonTurn().setBackgroundColor(activity.getResources().getColor(R.color.btn_summon));
    }

    // > Preparation au changement de tour et verifie l'état du jeu
    public void nextTurn() {
        Log.d(TAG, "[game] Change player turn");
        // verif de l'état de la battle

        switch (battleState.getPlayerTurn()) {
            case PLAYER1:
                if ( battleState.creaturehasDied(Player.PLAYER2)) {
                    String msg = activity.getResources().getString(R.string.msg_win, Player.PLAYER2, Player.PLAYER1);
                    activity.updateGameMSG(msg);
                    roundEnded = true;
//                   activity.getButtonTurn().setText(R.string.btn_start);

                    if ( !battleState.hasOtherCreature(Player.PLAYER2) ) {
                        //Un dialog
                        Log.d(TAG, "[game] Player 2 lost completly");
                        msg = activity.getResources().getString(R.string.msg_battle_winner, Player.PLAYER1);
                        activity.updateGameMSG(msg);
                        //disableActionButton();
                        battleState.battleEnd();

                        enableQuitTurnButton();

                    } else {
                        Log.d(TAG, "[game] Player 2 must summon another creature");
                        //enableSummonTurnButton();
                        activity.updateGameMSG(activity.getResources().getString(R.string.msg_summon));
                        battleState.changeCreature(Player.PLAYER2);
                        activity.setPlayerCreature(Player.PLAYER2);

                        changeTurn();
                        disableActionButton();

                        playTurn(Player.PLAYER2);
                        //enableNextTurnButton();
                    }


                } else {
                    //lancer directement le game du PLAYER2
                    disableActionButton();

                    changeTurn();

                    playTurn(Player.PLAYER2);

                }
                break;
            case PLAYER2:
                if ( battleState.creaturehasDied(Player.PLAYER1)) {
                    String msg = activity.getResources().getString(R.string.msg_win, Player.PLAYER1, Player.PLAYER2);
                    activity.updateGameMSG(msg);
                    roundEnded = true;
                    Log.d(TAG, "[game] Player 1 lost a creature");
                    //activity.getButtonTurn().setText(R.string.btn_start);

                    if ( !battleState.hasOtherCreature(Player.PLAYER1) ) {
                        //Un dialog
                        Log.d(TAG, "[game] Player 1 lost completly");
                        msg = activity.getResources().getString(R.string.msg_battle_winner, Player.PLAYER2);
                        activity.updateGameMSG(msg);
                        //disableActionButton();
                        battleState.battleEnd();

                        enableQuitTurnButton();

                    } else {
                        Log.d(TAG, "[game] Player 1 must summon another creature");
                        //enableSummonTurnButton();
                        activity.updateGameMSG(activity.getResources().getString(R.string.msg_summon));
                        battleState.changeCreature(Player.PLAYER1);
                        activity.setPlayerCreature(Player.PLAYER1);

                        changeTurn();
                        enableNextTurnButton();
                    }

                } else {
                    Log.d(TAG, "[game] Player 2 turn ended");
                    changeTurn();

                    enableNextTurnButton();
                }
                break;
        }

        ///battleState.changeTurn();
    }

    // > maj du tour
    private void changeTurn() {
        battleState.changeTurn();
        turnEnded = false;
    }

    // > lance une attack - mantenant déprécier : voir nextTurn
    public void launchAttack(Player targetedPlayer) {

        Log.d(TAG, "Player attacked : " + targetedPlayer);
        int dmg = caculDamage(targetedPlayer);
        Log.d(TAG, "DMG from attack : " + dmg);


        if ( battleState.canDieFromDamage(targetedPlayer, dmg) ) {
            Log.d(TAG, "ROUND END");
            roundEnded = true;
        }

        battleState.applyDamage(targetedPlayer, dmg);

    }

    // > Recupere le grand gagnant
    public Player getBattleWinner() {

        Player winner = Player.NONE;

        if ( battleState.hasRemainingCreature(Player.PLAYER1) && !battleState.hasRemainingCreature(Player.PLAYER2) ) winner = Player.PLAYER1;
        if ( !battleState.hasRemainingCreature(Player.PLAYER1) && battleState.hasRemainingCreature(Player.PLAYER2) ) winner = Player.PLAYER2;

        return winner;
    }

    // > Cacul les dommages infligé/subits
    private int caculDamage(Player targetedPlayer) {

        int dmg = 0;
        Double d;
        Random luck = new Random();

        switch (targetedPlayer) {
            case PLAYER1:
                int player2Atk = battleState.getPlayer2CurrentCreature().getStrike();
                int player1Def = battleState.getPlayer1CurrentCreature().getDefense();
                d = player1Def * 0.25;
                //dmg =  d.intValue() - player2Atk;
                dmg =  player2Atk - d.intValue();
                break;
            case PLAYER2:
                int player1Atk = battleState.getPlayer1CurrentCreature().getStrike();
                int player2Def = battleState.getPlayer2CurrentCreature().getDefense();
                d = player2Def * 0.25;
                //dmg =  d.intValue() - player1Atk;
                dmg = player1Atk - d.intValue();
                break;
        }

        if ( dmg <= 0) dmg =0;
        //if ( dmg >= 0 ) dmg = 0;

        return Math.abs(dmg);

    }

    public String getPlayerCreatureName(Player player) {

        String name = "";

        switch (player) {
            case PLAYER1:
                name = this.battleState.getPlayer1CurrentCreature().getName();
                break;
            case PLAYER2:
                name = this.battleState.getPlayer2CurrentCreature().getName();
                break;
            case NONE:
                name = "NA";
                break;
        }
        return name;
    }

    public BattleState getBattleState() {
        return battleState;
    }

    public boolean isTurnEnded() {
        return turnEnded;
    }
}
