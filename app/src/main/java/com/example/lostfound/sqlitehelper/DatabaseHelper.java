package com.example.lostfound.sqlitehelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_COMMAND = "CREATE TABLE "
                + Util.TABLE_NAME + "("
                + Util.AD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Util.NAME + " TEXT,"
                + Util.PHONE + " INTEGER,"
                + Util.DESCRIPTION + " TEXT,"
                + Util.DATE + " TEXT,"
                + Util.LOCATION + " TEXT,"
                + Util.IS_LOST + " INTEGER"
                + ")";

        db.execSQL(CREATE_TABLE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertAd(Advert ad) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.NAME, ad.getName());
        contentValues.put(Util.PHONE, ad.getPhone());
        contentValues.put(Util.DESCRIPTION, ad.getDescription());
        contentValues.put(Util.DATE, ad.getDate());
        contentValues.put(Util.LOCATION, ad.getLocation());
        contentValues.put(Util.IS_LOST, ad.isLost());

        long rowId = db.insert(Util.TABLE_NAME, null, contentValues);

        return rowId;
    }

    public List<Advert> getAllAds() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Advert> adverts = new ArrayList<>();

        Cursor cursor = db.query(Util.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(Util.AD_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Util.NAME));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(Util.PHONE));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(Util.DESCRIPTION));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(Util.DATE));
            @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex(Util.LOCATION));
            @SuppressLint("Range") int isLost = cursor.getInt(cursor.getColumnIndex(Util.IS_LOST));
            Advert ad = new Advert(id, name, phone, description, date, location, isLost > 0);
            adverts.add(ad);
        }

        return adverts;
    }

    public int deleteAd(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(Util.TABLE_NAME, Util.AD_ID + " = ?", new String[] { String.valueOf(id) });

        return result;
    }
}
