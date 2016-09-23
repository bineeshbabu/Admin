package com.phacsin.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GD on 9/23/2016.
 */
public class DBHandler extends SQLiteOpenHelper {
    public DBHandler(Context context) {
        super(context, "carchase", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        String TABLE_ACTIVE_FILTER = "CREATE TABLE registration(uuid TEXT,event_id TEXT)";
        String TABLE_ACTIVE_USER = "CREATE TABLE user(uuid TEXT)";

        db.execSQL(TABLE_ACTIVE_FILTER);
        db.execSQL(TABLE_ACTIVE_USER);
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS registration");
        onCreate(database);
    }

    public void insertEvent(String uuid, String event_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * from registration WHERE uuid='" + uuid + "' AND event_id = '"+ event_id+ "'", null);
        if(res.getCount()==0)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("uuid", uuid);
            contentValues.put("event_id", event_id);
            db.insert("registration", null, contentValues);
        }
    }

    public void insertUser(String uuid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uuid", uuid);
        db.insert("user", null, contentValues);
    }

    public void updateEvent(String uuid, String event_id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "UPDATE registration SET uuid='" + uuid + "' WHERE event_id='" + event_id + "'";
            db.execSQL(query);
        } catch (SQLiteException e) {
            Log.d("sqlite", e.toString());
        }
    }


    public void removeEvent() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM registration";
            db.execSQL(query);
        } catch (SQLiteException e) {
            Log.d("sqlite", e.toString());
        }
    }


    public List<EventDetails> getAllEvents() {
        List<EventDetails> list = new ArrayList<EventDetails>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from registration", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            EventDetails details = new EventDetails();
            details.event_id = res.getString(res.getColumnIndex("event_id"));
            details.uuid = res.getString(res.getColumnIndex("uuid"));
            list.add(details);
            res.moveToNext();
        }
        return list;
    }

    public boolean eventExists(String uuid, String event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from registration WHERE uuid='" + uuid + "' AND event_id = '"+ event_id+ "'", null);
        return res.getCount()!=0;
    }

    public boolean userExists(String uuid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from user WHERE uuid='" + uuid+"'", null);
        return res.getCount()!=0;
    }

    public int numberOfRows(String table_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table_name);
        return numRows;
    }

    public void removeUser() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM user";
            db.execSQL(query);
        } catch (SQLiteException e) {
            Log.d("sqlite", e.toString());
        }
    }
}
