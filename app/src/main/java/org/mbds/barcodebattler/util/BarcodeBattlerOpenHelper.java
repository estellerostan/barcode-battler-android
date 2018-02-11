package org.mbds.barcodebattler.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class BarcodeBattlerOpenHelper extends SQLiteOpenHelper {

    static final String CREATURE_TABLE_NAME = "creature";
    static final String CREATURE_ID = "id";
    static final String CREATURE_BARCODE = "barcode";
    static final String CREATURE_NAME = "name";
    static final String CREATURE_ENERGY = "energy";
    static final String CREATURE_STRIKE = "strike";
    static final String CREATURE_DEFENSE = "defense";
    static final String CREATURE_IMAGE_NAME = "imageName";
    static final String CREATURE_TYPE = "type";
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATURE_TABLE_CREATE =
            "CREATE TABLE " + CREATURE_TABLE_NAME + " (" +
                    CREATURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CREATURE_BARCODE + " TEXT, " +
                    CREATURE_NAME + " TEXT, " +
                    CREATURE_ENERGY + " INTEGER, " +
                    CREATURE_STRIKE + " INTEGER, " +
                    CREATURE_DEFENSE + " INTEGER, " +
                    CREATURE_IMAGE_NAME + " TEXT, " +
                    CREATURE_TYPE + " TEXT " +
                    ");";

    BarcodeBattlerOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATURE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATURE_TABLE_NAME);
        onCreate(db);
    }
}