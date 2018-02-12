package org.mbds.barcodebattler.battle;

import android.util.Log;

import org.mbds.barcodebattler.data.ICreature;

import java.util.List;

import static org.mbds.barcodebattler.battle.Player.PLAYER1;
import static org.mbds.barcodebattler.battle.Player.PLAYER2;

/**
 * Created by michael on 10/02/2018.
 */

public class BattleState {

    private static final String TAG = BattleState.class.getSimpleName();

    private List<ICreature> player1Creatures;
    private List<ICreature> player2Creatures;

    private ICreature player1CurrentCreature;
    private ICreature player2CurrentCreature;

    private Player playerTurn;

    public BattleState(List<ICreature> player1Creatures, List<ICreature> player2Creatures) {
        this.player1Creatures = player1Creatures;
        this.player2Creatures = player2Creatures;

        this.player1CurrentCreature = player1Creatures.remove(0);
        this.player2CurrentCreature = player2Creatures.remove(0);

        playerTurn = Player.NONE;
    }

    public BattleState(List<ICreature> player1Creatures, List<ICreature> player2Creatures, ICreature player1CurrentCreature, ICreature player2CurrentCreature) {
        this.player1Creatures = player1Creatures;
        this.player2Creatures = player2Creatures;
        this.player1CurrentCreature = player1CurrentCreature;
        this.player2CurrentCreature = player2CurrentCreature;

        playerTurn = PLAYER1;
    }

    // > Debute la 1er battle
    public void startTurn() {
            playerTurn = PLAYER1;
            Log.d(TAG, "[game] Start turn");
    }

    // > Changement de tour par rapport au tour actuel
    public void changeTurn() {
        switch (playerTurn) {
            case PLAYER1:
                playerTurn = PLAYER2;
                break;
            case PLAYER2:
                playerTurn = PLAYER1;
                break;
        }
    }



    //> Change la creature du joueur
    public void changeCreature(Player player) {

        switch (player) {
            case PLAYER1:
                Log.d(TAG, "[game] P1 summon monster");
                player1CurrentCreature = player1Creatures.remove(0);
                break;
            case PLAYER2:
                Log.d(TAG, "[game] P2 summon monster");
                player2CurrentCreature = player2Creatures.remove(0);
                break;
            default:
        }
    }

    // > Signale la fin d'une battle
    public void battleEnd() {
        playerTurn = Player.NONE;
    }

    // > Creature du joueur indiqué mort ?
    public boolean creaturehasDied(Player player) {
        switch (player) {
            case PLAYER1:
                return!( player1CurrentCreature.getEnergy() > 0);
            case PLAYER2:
                return !(player2CurrentCreature.getEnergy() > 0);
            default:
                return false;
        }
    }

    // > Vérifie si un joueur à encore des créatures en état de combattre
    public boolean hasRemainingCreature(Player player) {
        switch (player) {
            case PLAYER1:
                return (player1Creatures.iterator().hasNext() || player1CurrentCreature.getEnergy() > 0);
            case PLAYER2:
                return (player2Creatures.iterator().hasNext() || player2CurrentCreature.getEnergy() > 0);
            default:
                return false;
        }
    }

    // > Vérifie si le joueur a d'autre creature
    public boolean hasOtherCreature(Player player) {
        switch (player) {
            case PLAYER1:
                return player1Creatures.iterator().hasNext();
            case PLAYER2:
                return player2Creatures.iterator().hasNext();
            default:
                return false;
        }
    }

    // > Retourne si le monstre meure à la suite de l'application de ces dommages
    public boolean canDieFromDamage(Player targetedPlayer, int dmg) {

        boolean canDie = true;

        switch (targetedPlayer) {
            case PLAYER1:
                if ( player1CurrentCreature.getEnergy() - dmg > 0) canDie = false;
                break;
            case PLAYER2:
                if ( player2CurrentCreature.getEnergy() - dmg > 0) canDie = false;
        }

        return canDie;
    }

    // > Affecte les dommages d'une attaque au monstre
    public void applyDamage(Player targetedPlayer, int dmg) {

        switch (targetedPlayer) {
            case PLAYER1:
                player1CurrentCreature.setEnergy(Math.max(0, player1CurrentCreature.getEnergy() - dmg) );
                break;
            case PLAYER2:
                player2CurrentCreature.setEnergy(Math.max(0, player2CurrentCreature.getEnergy() - dmg) );
                break;
        }
    }



    // GETTERS AND SETTERS


    public List<ICreature> getPlayer1Creatures() {
        return player1Creatures;
    }

    public void setPlayer1Creatures(List<ICreature> player1Creatures) {
        this.player1Creatures = player1Creatures;
    }

    public List<ICreature> getPlayer2Creatures() {
        return player2Creatures;
    }

    public void setPlayer2Creatures(List<ICreature> player2Creatures) {
        this.player2Creatures = player2Creatures;
    }

    public ICreature getPlayer1CurrentCreature() {
        return player1CurrentCreature;
    }

    public void setPlayer1CurrentCreature(ICreature player1CurrentCreature) {
        this.player1CurrentCreature = player1CurrentCreature;
    }

    public ICreature getPlayer2CurrentCreature() {
        return player2CurrentCreature;
    }

    public void setPlayer2CurrentCreature(ICreature player2CurrentCreature) {
        this.player2CurrentCreature = player2CurrentCreature;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Player player) {
        this.playerTurn = player;
    }

}
