package org.mbds.barcodebattler.data;

import android.os.Parcelable;

import dagger.Module;

@Module
public interface ICreature extends Parcelable {
    int getEnergy();

    void setEnergy(int energy);

    int getStrike();

    void setStrike(int strike);

    int getDefense();

    void setDefense(int defense);

    String getImageName();

    void setImageName(String imageName);
}
