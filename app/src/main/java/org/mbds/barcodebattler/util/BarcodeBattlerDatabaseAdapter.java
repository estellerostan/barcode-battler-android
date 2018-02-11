package org.mbds.barcodebattler.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.mbds.barcodebattler.data.Creature;
import org.mbds.barcodebattler.data.ICreature;

import java.util.ArrayList;

public class BarcodeBattlerDatabaseAdapter {
    // Variable to hold the database instance
    private static SQLiteDatabase db;
    // Database open/upgrade helper
    private static BarcodeBattlerOpenHelper dbHelper;
    // Context of the application using the database.
    private final Context context;

    public BarcodeBattlerDatabaseAdapter(Context _context) {
        context = _context;
        dbHelper = new BarcodeBattlerOpenHelper(context);
    }

    // Method to openthe Database
    public BarcodeBattlerDatabaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // Method to close the Database
    public void close() {
        db.close();
    }

    // method returns an Instance of the Database
    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    // method to insert a record in Table
    public void insertCreature(String barcode, String name, int energy, int strike, int defense, String imageName) {
        try {
            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put(BarcodeBattlerOpenHelper.CREATURE_BARCODE, barcode);
            newValues.put(BarcodeBattlerOpenHelper.CREATURE_NAME, name);
            newValues.put(BarcodeBattlerOpenHelper.CREATURE_ENERGY, energy);
            newValues.put(BarcodeBattlerOpenHelper.CREATURE_STRIKE, strike);
            newValues.put(BarcodeBattlerOpenHelper.CREATURE_DEFENSE, defense);
            newValues.put(BarcodeBattlerOpenHelper.CREATURE_IMAGE_NAME, imageName);
            // Insert the row into your table
            db = dbHelper.getWritableDatabase();
            db.insert(BarcodeBattlerOpenHelper.CREATURE_TABLE_NAME, null, newValues);
//            Toast.makeText(context, "Créature enregistrée", Toast.LENGTH_LONG).show();
//            System.out.println( "---------------------------------------------------------------------------"+ newValues.get("name") + " insere " ); // debug
        } catch (Exception ex) {
            System.out.println("Exceptions " + ex);
        } finally {
//            db.close(); // nope
        }
    }

    public void updateCreature(int id, String name) {
        ContentValues cv = new ContentValues();
        cv.put(BarcodeBattlerOpenHelper.CREATURE_NAME, name);

        db = dbHelper.getWritableDatabase();
        db.update(BarcodeBattlerOpenHelper.CREATURE_TABLE_NAME, cv, "_id=" + id, null);
    }

    public int deleteCreature(int position) {
        String table = BarcodeBattlerOpenHelper.CREATURE_TABLE_NAME;
        String whereClause = String.format("%s%n =?", BarcodeBattlerOpenHelper.CREATURE_ID);
        String[] whereArgs = new String[]{Integer.toString(position)};

//        System.out.println( "---------------------------------------------------------------------------"+ table + " " + whereClause + " " + whereArgs[0]); // debug

        return db.delete(table, whereClause, whereArgs);
    }

    public ArrayList<ICreature> getCreatures() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(BarcodeBattlerOpenHelper.CREATURE_TABLE_NAME, new String[]{
                        BarcodeBattlerOpenHelper.CREATURE_ID,
                        BarcodeBattlerOpenHelper.CREATURE_BARCODE,
                        BarcodeBattlerOpenHelper.CREATURE_NAME,
                        BarcodeBattlerOpenHelper.CREATURE_ENERGY,
                        BarcodeBattlerOpenHelper.CREATURE_STRIKE,
                        BarcodeBattlerOpenHelper.CREATURE_DEFENSE,
                        BarcodeBattlerOpenHelper.CREATURE_IMAGE_NAME},
                null, null, null, null, null); // pas de where, on les veut tous

        ArrayList<ICreature> creatures = new ArrayList<>();

        //Parcour des résultats  :
        if (cursor.moveToFirst()) {
            do {
                ICreature temp = new Creature();
                temp.setId(Integer.parseInt(cursor.getString(0)));
                temp.setBarcode(cursor.getString(1));
                temp.setName(cursor.getString(2));
                temp.setEnergy(Integer.parseInt(cursor.getString(3)));
                temp.setStrike(Integer.parseInt(cursor.getString(4)));
                temp.setDefense(Integer.parseInt(cursor.getString(5)));
                temp.setImageName(cursor.getString(6));
//            System.out.println( "---------------------------------------------------------------------------"+ temp.getName()+ " lu depuis la base de donnees " ); // debug
                creatures.add(temp);
            }
            while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return creatures;
    }

    public ICreature getCreature(String barcode) {
        ICreature temp = new Creature();
        try (Cursor cursor = db.rawQuery("SELECT * FROM creature WHERE barcode=?", new String[]{barcode + ""})) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                temp.setId(Integer.parseInt(cursor.getString(0)));
                temp.setBarcode(cursor.getString(1));
                temp.setName(cursor.getString(2));
                temp.setEnergy(Integer.parseInt(cursor.getString(3)));
                temp.setStrike(Integer.parseInt(cursor.getString(4)));
                temp.setDefense(Integer.parseInt(cursor.getString(5)));
                temp.setImageName(cursor.getString(6));
            }
            return temp;
        }
    }
}