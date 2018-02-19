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
    private String type;

    AbstractCreature() {

    }

    AbstractCreature(String barcode, String name, int energy, int strike, int defense, String imageName, String type) {
        this.barcode = barcode;
        this.name = name;
        this.energy = energy;
        this.strike = strike;
        this.defense = defense;
        this.imageName = imageName;
        this.type = type;
    }

    AbstractCreature(String barcode, String name, String imageName, String type) {
        this.barcode = barcode;
        this.name = name;
        this.imageName = imageName;
        this.type = type;
    }

    AbstractCreature(Parcel in) {
        String[] data = new String[8];
        in.readStringArray(data);

        try {
            this.id = Integer.parseInt(data[0]);
            this.barcode = data[1];
            this.name = data[2];
            this.energy = Integer.parseInt(data[3]);
            this.strike = Integer.parseInt(data[4]);
            this.defense = Integer.parseInt(data[5]);
            this.imageName = data[6];
            this.type = data[7];
        } catch (NumberFormatException e) {
            //Never trust user input :)
            // TODO:
            //Do something! Anything to handle the exception.
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(getId()), barcode, name, String.valueOf(getEnergy()), String.valueOf(getStrike()), String.valueOf(getDefense()), imageName, type});
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
