package com.example.electricitybillestimator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ElectricityBills.db";
    public static final String TABLE_NAME = "bills";
    public static final String COL_ID = "ID";
    public static final String COL_MONTH = "MONTH";
    public static final String COL_UNIT = "UNIT";
    public static final String COL_REBATE = "REBATE";
    public static final String COL_TOTAL = "TOTAL";
    public static final String COL_FINAL = "FINAL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // Called when database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MONTH + " TEXT, " +
                COL_UNIT + " INTEGER, " +
                COL_REBATE + " REAL, " +
                COL_TOTAL + " REAL, " +
                COL_FINAL + " REAL)";
        db.execSQL(createTable);
    }

    // Called when upgrading version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert new record
    public boolean insertData(String month, int unit, double rebate, double total, double finalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MONTH, month);
        contentValues.put(COL_UNIT, unit);
        contentValues.put(COL_REBATE, rebate);
        contentValues.put(COL_TOTAL, total);
        contentValues.put(COL_FINAL, finalCost);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // Return true if insert successful
    }

    // Get all data
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY ID DESC", null);
    }

    // Get single data by ID
    public Cursor getDataById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id, null);
    }

    // ✅ Delete record by ID
    public boolean deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }
}
