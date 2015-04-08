package com.jarvis.be.dailychores.com.jarvis.be.dailychores.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by parimal on 08-04-2015.
 */
public class EventDbHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "choresevent.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String TABLE_NAME = "chores_event";
    public static final String _ID = "_id";
    public static final String COLUMN_CHORES_TITLE = "chores_title";
    public static final String COLUMN_CHORES_ADDRESS = "chores_address";
    public static final String COLUMN_CHORES_LASTUPDATED = "chores_last_updated";
    public static final String COLUMN_CHORES_CALENDAR_ID = "cal_id";
    public static final String COLUMN_CHORES_CALENDAR_NAME = "cal_name";


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_CHORES_TITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_CHORES_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    COLUMN_CHORES_LASTUPDATED + TEXT_TYPE + COMMA_SEP +
                    COLUMN_CHORES_CALENDAR_ID + TEXT_TYPE + COMMA_SEP +
                    COLUMN_CHORES_CALENDAR_NAME + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_ENTRIES ="DROP TABLE IF EXISTS " + TABLE_NAME;

    public EventDbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertChoresToDb(String title, String address, String lastUpdated, String calId, String calName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CHORES_TITLE, title);
        contentValues.put(COLUMN_CHORES_ADDRESS, address);
        contentValues.put(COLUMN_CHORES_LASTUPDATED, lastUpdated);
        contentValues.put(COLUMN_CHORES_CALENDAR_ID, calId);
        contentValues.put(COLUMN_CHORES_CALENDAR_NAME, calName);

        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getDataById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from chores_event where _id="+id+"", null );
        return res;
    }

    public ArrayList<ChoresModel> getAllChores()
    {
        ArrayList<ChoresModel> array_list = new ArrayList<ChoresModel>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from chores_event", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            //array_list.add(res.getString(res.getColumnIndex(TABLE_NAME)));
            ChoresModel tempModel = new ChoresModel(res.getString(res.getColumnIndex(COLUMN_CHORES_TITLE)),
                                                    res.getString(res.getColumnIndex(COLUMN_CHORES_ADDRESS)),
                                                    res.getString(res.getColumnIndex(COLUMN_CHORES_LASTUPDATED)),
                                                    res.getString(res.getColumnIndex(COLUMN_CHORES_CALENDAR_ID)),
                                                    res.getString(res.getColumnIndex(COLUMN_CHORES_CALENDAR_NAME)));
            array_list.add(tempModel);

            res.moveToNext();
        }
        return array_list;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }
}
