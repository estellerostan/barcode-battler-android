package org.mbds.barcodebattler.data;

import android.os.Parcelable;

import dagger.Module;

@Module
public interface ICreature extends Parcelable {
    int getId();

    void setId(int id);

    String getBarcode();

    void setBarcode(String barcode);

    String getName();

    void setName(String name);

    int getEnergy();

    void setEnergy(int energy);

    int getStrike();

    void setStrike(int strike);

    int getDefense();

    void setDefense(int defense);

    String getImageName();

    void setImageName(String imageName);

    String getType();

    void setType(String type);
}
