package org.mbds.barcodebattler.battle;

import org.mbds.barcodebattler.data.Creature;

import java.util.List;

/**
 * Created by michael on 10/02/2018.
 */

public class BattleState {

    private List<Creature> player1Creatures;
    private List<Creature> player2Creatures;

    private Creature player1CurrentCreature;
    private Creature player2CurrentCreature;

    private Player playerTurn;

    public BattleState(List<Creature> player1Creatures, List<Creature> player2Creatures) {
        this.player1Creatures = player1Creatures;
        this.player2Creatures = player2Creatures;

        this.player1CurrentCreature = player1Creatures.remove(0);
        this.player2CurrentCreature = player2Creatures.remove(0);

        playerTurn = Player.PLAYER1;
    }

    public BattleState(List<Creature> player1Creatures, List<Creature> player2Creatures, Creature player1CurrentCreature, Creature player2CurrentCreature) {
        this.player1Creatures = player1Creatures;
        this.player2Creatures = player2Creatures;
        this.player1CurrentCreature = player1CurrentCreature;
        this.player2CurrentCreature = player2CurrentCreature;
    }

    // Change la creature du joueur
    public void changeCreature(Player player) {

        switch (player) {
            case PLAYER1:
                player1CurrentCreature = player1Creatures.iterator().next();
                player1Creatures.iterator().remove();
                break;
            case PLAYER2:
                player2CurrentCreature = player2Creatures.iterator().next();
                player2Creatures.iterator().remove();
                break;
            default:
        }
    }

    // Vérifie si un joueur à encore des créatures
    public boolean hasRemainingCreature(Player player) {
        switch (player) {
            case PLAYER1:
                return player1Creatures.iterator().hasNext();
            case PLAYER2:
                return player2Creatures.iterator().hasNext();
            default:
                return false;
        }
    }

    // Retourne si le monstre meure à la suite de l'application de ces dommages
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

    // Affecte les dommages d'une attaque au monstre
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


    public List<Creature> getPlayer1Creatures() {
        return player1Creatures;
    }

    public void setPlayer1Creatures(List<Creature> player1Creatures) {
        this.player1Creatures = player1Creatures;
    }

    public List<Creature> getPlayer2Creatures() {
        return player2Creatures;
    }

    public void setPlayer2Creatures(List<Creature> player2Creatures) {
        this.player2Creatures = player2Creatures;
    }

    public Creature getPlayer1CurrentCreature() {
        return player1CurrentCreature;
    }

    public void setPlayer1CurrentCreature(Creature player1CurrentCreature) {
        this.player1CurrentCreature = player1CurrentCreature;
    }

    public Creature getPlayer2CurrentCreature() {
        return player2CurrentCreature;
    }

    public void setPlayer2CurrentCreature(Creature player2CurrentCreature) {
        this.player2CurrentCreature = player2CurrentCreature;
    }
}
