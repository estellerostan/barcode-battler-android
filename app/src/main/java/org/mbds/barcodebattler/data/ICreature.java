package org.mbds.barcodebattler.data;

import dagger.Module;

@Module
public interface ICreature {
    int getEnergy();

    void setEnergy(int energy);

    int getStrike();

    void setStrike(int strike);

    int getDefense();

    void setDefense(int defense);
}
