package com.fan.innerprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fan.innerprovider.Sqlite.NoteOpenHelper;
import com.fan.innerprovider.Utils.Constants;

/**
 * Created by FanFan_code on 19-2-26.
 * WeChat zhangruifang3320
 * 微信搜索公众号 码农修仙儿，欢迎关注打赏
 */

public class NoteProvider extends ContentProvider {


    private static UriMatcher matcher;
    private SQLiteDatabase db;
    private static NoteOpenHelper noteOH;


//    static {
//        matcher = new UriMatcher(UriMatcher.NO_MATCH);
//        matcher.addURI(Constants.AUTHORIES,Constants.NOTE_TABLE_NAME, Constants.QUERY_ALL);
//        matcher.addURI(Constants.AUTHORIES, Constants.NOTE_TABLE_NAME + "/#", Constants.QUERY_ITEM);
//    }


    @Override
    public boolean onCreate() {
        noteOH = new NoteOpenHelper(getContext());
        return true;
    }

    /**
     * query data
     *
     * select projection from table where selections = selectionArgs order by sortOrder
     *
     * @param uri      used for distinguish dir or item
     * @param strings  projection ,which column to be return
     * @param s        elections
     * @param strings1 selectionArgs
     * @param s1       sortOrder
     * @return result cursor
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        Cursor cursor = null;
        db = noteOH.getReadableDatabase();
        if (db != null && db.isOpen()) {
            cursor = db.query(Constants.NOTE_TABLE_NAME, strings,s,strings1,null,null,s1);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    /**
     * insert one record
     *
     * insert into table contentValues[(column1,column2) values (value1, value2)]
     *
     * @param uri uri for find the provider
     * @param contentValues the record to insert
     * @return uri
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri result_uri = null;
        db = noteOH.getWritableDatabase();
        if (db != null && db.isOpen()) {
            long row = db.insert(Constants.NOTE_TABLE_NAME, null, contentValues);
            if (row > -1) {
                result_uri = ContentUris.withAppendedId(Uri.parse(Constants.PROVIDER_URI),row);
            }
        }
        return result_uri;
    }

    /**
     * delete one record
     *
     * delete from table where whereClause=whereArgs
     *
     * @param uri     uri
     * @param s       whereClause
     * @param strings whereArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        db = noteOH.getWritableDatabase();
        int row = -1;
        if (db != null && db.isOpen()) {
            row = db.delete(Constants.NOTE_TABLE_NAME, s,strings);
        }
        return row;
    }

    /**
     * update one record
     *
     * update table set contentValues where whereClause=whereArgs
     *
     * @param uri           uri
     * @param contentValues contentValues
     * @param s             whereClause
     * @param strings       whereArgs
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        db = noteOH.getWritableDatabase();
        int row = -1;
        if (db != null && db.isOpen()) {
            row = db.update(Constants.NOTE_TABLE_NAME, contentValues, s, strings);
        }
        return row;
    }
}
