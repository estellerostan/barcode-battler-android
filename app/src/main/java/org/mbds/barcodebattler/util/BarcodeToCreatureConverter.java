package org.mbds.barcodebattler.util;

import org.mbds.barcodebattler.data.Creature;

public class BarcodeToCreatureConverter {

    public static Creature convert(String barcode) {
        if (barcode == null) {
            return null;
        }
        else {
            Creature creature = new Creature();
            creature.setEnergy(100 * Integer.parseInt(barcode.substring(0, 1))+ 100);
            creature.setStrike(100 * Integer.parseInt(barcode.substring(1, 2)) + 100);
            creature.setDefence(100 * Integer.parseInt(barcode.substring(2, 3)) + 100);
            return creature;
        }
    }
}
