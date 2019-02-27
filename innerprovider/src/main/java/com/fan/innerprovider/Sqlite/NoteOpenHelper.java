package com.fan.innerprovider.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fan.innerprovider.Utils.Constants;

/**
 * Created by FanFan_code on 19-2-26.
 * WeChat zhangruifang3320
 * 微信搜索公众号 码农修仙儿，欢迎关注打赏
 */

public class NoteOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    public NoteOpenHelper(Context context){
        this(context, Constants.DB_NAME, null, 1);
    }

    private NoteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists "
                + Constants.NOTE_TABLE_NAME
                + " ("
                + Constants.ID + " integer primary key AUTOINCREMENT, "
                + Constants.GOODS + " text not null, "
                + Constants.PRICE + " text not null, "
                + Constants.TIME + " text not null, "
                + Constants.DESCRIPTION + " char(50) not null, "
                + Constants.DIRECT + " text not null"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    private void createNoteTable (SQLiteDatabase db) {
        db.execSQL("create table if not exists " + Constants.NOTE_TABLE_NAME
                + " ("
                + Constants.ID + " int primary key AUTOINCREMENT, "
                + Constants.GOODS + " text not null, "
                + Constants.PRICE + " text not null, "
                + Constants.TIME + " text not null, "
                + Constants.DESCRIPTION + " char(50) not null, "
                + Constants.DIRECT + " text not null"
                + ")");
    }
}
