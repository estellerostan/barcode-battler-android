package org.mbds.barcodebattler.util;

import org.mbds.barcodebattler.data.ICreature;

public class BarcodeToCreatureConverter {

    private ICreature creature;

    public BarcodeToCreatureConverter(ICreature creature) {
        this.creature = creature;
    }

    public ICreature convert(String barcode) {
        if (barcode == null) {
            return null;
        }
        else {
            creature.setEnergy(100 * Integer.parseInt(barcode.substring(0, 1))+ 100);
            creature.setStrike(100 * Integer.parseInt(barcode.substring(1, 2)) + 100);
            creature.setDefense(100 * Integer.parseInt(barcode.substring(2, 3)) + 100);
            return creature;
        }
    }
}
