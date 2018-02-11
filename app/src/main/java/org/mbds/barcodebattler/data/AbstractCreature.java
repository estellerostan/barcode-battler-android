package org.mbds.barcodebattler.data;

import android.os.Parcel;

abstract class AbstractCreature implements ICreature {
    private int id;
    private String barcode;
    private String name;
    private int energy;
    private int strike;
    private int defense;
    private String imageName;

    AbstractCreature() {

    }

    AbstractCreature(String barcode, String name, int energy, int strike, int defense, String imageName) {
        this.barcode = barcode;
        this.name = name;
        this.energy = energy;
        this.strike = strike;
        this.defense = defense;
        this.imageName = imageName;
    }

    AbstractCreature(Parcel in) {
        String[] data = new String[6];
        in.readStringArray(data);

        try {
            this.barcode = data[0];
            this.name = data[1];
            this.energy = Integer.parseInt(data[2]);
            this.strike = Integer.parseInt(data[3]);
            this.defense = Integer.parseInt(data[4]);
            this.imageName = data[5];
        } catch (NumberFormatException e) {
            //Never trust user input :)
            // TODO:
            //Do something! Anything to handle the exception.
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{barcode, name, String.valueOf(getEnergy()), String.valueOf(getStrike()), String.valueOf(getDefense()), imageName});
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    public final String getBarcode() {
        return barcode;
    }

    public final void setBarcode(String barcode) {
        this.barcode = barcode;
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
