package com.example.to_do_list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME ="todo.db";

    private static final String TABLE_NAME="todo";

    private static final String ID ="_ID";
    private static final String T_TITLE ="_TITLE";
    private static final String T_TITLE_MSG ="_TITLEMSG";
    private static final String T_DATE ="_DATE";
    private static final String T_TIME ="_TIME";

    int status = 0;

    SQLiteDatabase db;
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String qry = " CREATE TABLE " + TABLE_NAME +
                " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                T_TITLE + " TEXT NOT NULL," +
                T_TITLE_MSG + " TEXT NOT NULL," +
                T_DATE + " TEXT NOT NULL," +
                T_TIME + " TEXT NOT NULL" + ") ";

        db.execSQL(qry);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean addTodo(String title, String titlemsg, String date, String time)
    {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(T_TITLE,title);
        cv.put(T_TITLE_MSG,titlemsg);
        cv.put(T_DATE,date);
        cv.put(T_TIME,time);

        long result = db.insert(TABLE_NAME,null,cv);

        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getTodo()
    {
        db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME , null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + T_DATE + " ASC", null);
        return  cursor;
    }

    public boolean deleteTodo(String id)
    {
        db = this.getWritableDatabase();
        ContentValues cv =new ContentValues();

        cv.put(ID,id);

        int res =  db.delete(TABLE_NAME, ID + " = ? ", new String[]{id}) ;

        if (res == -1)
        {
            return false;
        }
        else {
            return true;
        }
    }


    public boolean updateTodo(String tid, String title, String message, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(T_TITLE, title);
        cv.put(T_TITLE_MSG, message);
        cv.put(T_DATE, date);
        cv.put(T_TIME, time);

//        Log.d("DBHelper", "Updating task with ID: " + tid + ", Title: " + title + ", Message: " + message + ", Date: " + date + ", Time: " + time);

        int res = db.update(TABLE_NAME, cv, ID + " = ?", new String[]{tid});

//        Log.d("DBHelper", "Update result: " + res);

        return res > 0;
    }


}
