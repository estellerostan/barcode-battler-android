package org.mbds.barcodebattler.data;

abstract class AbstractCreature implements ICreature {
    private int energy;
    private int strike;
    private int defense;

    AbstractCreature(int energy, int strike, int defense) {
        this.energy = energy;
        this.strike = strike;
        this.defense = defense;
    }

    public final int getEnergy() {
        return energy;
    }

    public final void setEnergy(int energy) {
        this.energy = energy;
    }

    public final int getStrike() {
        return strike;
    }

    public final void setStrike(int strike) {
        this.strike = strike;
    }

    public final int getDefense() {
        return defense;
    }

    public final void setDefense(int defense) {
        this.defense = defense;
    }
}
