package org.mbds.barcodebattler.battle;

import org.mbds.barcodebattler.data.Creature;

import java.util.List;
import java.util.Random;

/**
 * Created by michael on 10/02/2018.
 */

public class Battle {

    private BattleState battleState;
    private boolean roundEnded;
    private boolean battleEnded;

    public Battle(List<Creature> player1Creatures, List<Creature> player2Creatures) {
        this.battleState = new BattleState(player1Creatures, player2Creatures);
        roundEnded = false;
        battleEnded = false;
    }

    public void launchAttack(Player targetedPlayer) {

        int dmg = caculDamage(targetedPlayer);

        if ( battleState.canDieFromDamage(targetedPlayer, dmg) ) {
            roundEnded = true;
        }

        battleState.applyDamage(targetedPlayer, dmg);

    }

    public void nextRound(Player loser) {

        if ( roundEnded ) {

            //TODO : verifier si le loser a encore des monstres

            battleState.changeCreature(loser);
            roundEnded = false;
        }
    }

    public Player getBattleWinner() {

        Player winner = Player.NONE;

        if ( battleState.hasRemainingCreature(Player.PLAYER1) && !battleState.hasRemainingCreature(Player.PLAYER2) ) winner = Player.PLAYER1;
        if ( !battleState.hasRemainingCreature(Player.PLAYER1) && battleState.hasRemainingCreature(Player.PLAYER2) ) winner = Player.PLAYER2;

        return winner;
    }

    private int caculDamage(Player targetedPlayer) {

        int dmg = 0;
        Random luck = new Random();

        switch (targetedPlayer) {
            case PLAYER1:
                int player2Atk = battleState.getPlayer2CurrentCreature().getStrike();
                int player1Def = battleState.getPlayer1CurrentCreature().getDefense();

                dmg = (player1Def * luck.nextInt(1)) - player2Atk;
                break;
            case PLAYER2:
                int player1Atk = battleState.getPlayer1CurrentCreature().getStrike();
                int player2Def = battleState.getPlayer2CurrentCreature().getDefense();

                dmg = (player2Def * luck.nextInt(1)) - player1Atk;
                break;
        }

        if ( dmg >= 0 ) dmg = 0;

        return Math.abs(dmg);

    }


}
