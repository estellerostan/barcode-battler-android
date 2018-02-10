package org.mbds.barcodebattler.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Creature extends AbstractCreature implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Creature createFromParcel(Parcel in) {
            return new Creature(in);
        }

        public Creature[] newArray(int size) {
            return new Creature[size];
        }
    };

    public Creature(String name, int energy, int strike, int defense, String imageName) {
        super(name, energy, strike, defense, imageName);
    }

    public Creature() {

    }

    public Creature(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
