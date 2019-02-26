package com.fan.worker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by FanFan on 19-2-25.
 *
 */

public class WorkerOpenHelper extends SQLiteOpenHelper {


    private static WorkerOpenHelper sInstance;

    private WorkerOpenHelper(Context context){
        this(context, Constants.DB_NAME,null,1);
    }

    private WorkerOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static WorkerOpenHelper getWorkOHInstance (Context context){
        if (sInstance == null){
            sInstance = new WorkerOpenHelper(context);
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constants.CREATE_WORKER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
