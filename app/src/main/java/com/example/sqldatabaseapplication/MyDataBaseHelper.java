package com.example.sqldatabaseapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Reminder";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "myReminder";
    public static final String ID_COL = "id";
    public static final String DATE_COL = "date";
    public static final String TIME_COL = "time";
    public static final String DESCRIPTION_COL = "description";
    public MyDataBaseHelper(@Nullable Context context) {
        super(context,  DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_COL + " TEXT,"
                + TIME_COL + " TEXT,"
                + DESCRIPTION_COL + " TEXT)";

        db.execSQL(query);
    }

    public void addNewCourse(String date, String time,String desc) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(DATE_COL, date);
        values.put(TIME_COL, time);
        values.put(DESCRIPTION_COL, desc);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<MyDataModel> getAllData() {
        ArrayList<MyDataModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {

                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(ID_COL));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DATE_COL));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(TIME_COL));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION_COL));

                MyDataModel data = new MyDataModel(id,date, time, description);
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dataList;
    }
    public int updateData(int id, String date, String time, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATE_COL, date);
        values.put(TIME_COL, time);
        values.put(DESCRIPTION_COL, description);

        return db.update(TABLE_NAME, values, ID_COL + " = ?",
                new String[]{String.valueOf(id)});
    }


    public void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_COL + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}