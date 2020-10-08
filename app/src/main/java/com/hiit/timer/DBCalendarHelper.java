package com.hiit.timer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;


public class DBCalendarHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "EVENTS_DB";
    public static final int DB_VERSION = 1;
    public static final String EVENT_TABLE_NAME = "EVENTS_TABLE";
    public static final String COL_ID = "COL_ID";
    public static final String COL_EVENT = "EVENT";
    public static final String COL_DAY = "DAY";
    public static final String COL_NUMBER_OF_EVENTS = "EVENT_NO";
    public static final String COL_MONTH = "MONTH";
    public static final String COL_YEAR = "YEAR";


    public DBCalendarHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + EVENT_TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EVENT + " TEXT, " +
                COL_DAY + " TEXT, " +
                COL_NUMBER_OF_EVENTS + " TEXT, " +
                COL_MONTH + " TEXT, " +
                COL_YEAR + " TEXT " + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE_NAME);
        onCreate(db);
    }

    public boolean SaveEventsToDB(String event, String day, String noOfEvents, String month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_EVENT, event);
        cv.put(COL_DAY, day);
        cv.put(COL_NUMBER_OF_EVENTS, noOfEvents);
        cv.put(COL_MONTH, month);
        cv.put(COL_YEAR, year);

        long result = db.insert(EVENT_TABLE_NAME, null, cv);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor readEventsFromDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EVENT_TABLE_NAME, null);
        return cursor;
    }

    public Cursor showOneEventFromDB(String day, String month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EVENT_TABLE_NAME + " WHERE "
                + COL_DAY + "=? AND "
                + COL_MONTH + "=? AND "
                + COL_YEAR + "=?", new String[]{day, month, year});
        return cursor;
    }

    public void updateEvent(String id, String numberOfEvents) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NUMBER_OF_EVENTS, numberOfEvents);
        db.update(EVENT_TABLE_NAME, cv, COL_ID + "=?", new String[]{id});
    }

    public Cursor readEventsPerMonthFromDB(String month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EVENT_TABLE_NAME + " WHERE "
                + COL_MONTH + "=? AND "
                + COL_YEAR + "=?", new String[]{month, year});
        return cursor;
    }

    public Cursor readEventsPerDayFromDB(String day, String month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EVENT_TABLE_NAME + " WHERE "
                + COL_DAY + "=? AND "
                + COL_MONTH + "=? AND "
                + COL_YEAR + "=?", new String[]{day, month, year});
        return cursor;
    }

}
