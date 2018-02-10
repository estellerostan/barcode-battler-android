package org.mbds.barcodebattler.data;

import android.os.Parcel;

abstract class AbstractCreature implements ICreature {
    private String name;
    private int energy;
    private int strike;
    private int defense;
    private String imageName;

    AbstractCreature() {

    }

    AbstractCreature(String name, int energy, int strike, int defense, String imageName) {
        this.name = name;
        this.energy = energy;
        this.strike = strike;
        this.defense = defense;
        this.imageName = imageName;
    }

    AbstractCreature(Parcel in) {
        String[] data = new String[5];
        in.readStringArray(data);

        try {
            this.name = data[0];
            this.energy = Integer.parseInt(data[1]);
            this.strike = Integer.parseInt(data[2]);
            this.defense = Integer.parseInt(data[3]);
            this.imageName = data[4];
        } catch (NumberFormatException e) {
            //Never trust user input :)
            // TODO:
            //Do something! Anything to handle the exception.
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{name, String.valueOf(getEnergy()), String.valueOf(getStrike()), String.valueOf(getDefense()), imageName});
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
