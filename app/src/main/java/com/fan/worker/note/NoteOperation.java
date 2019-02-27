package com.fan.worker.note;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.fan.worker.*;

/**
 * Created by FanFan_code on 19-2-26.
 * WeChat zhangruifang3320
 * 微信搜索公众号 码农修仙儿，欢迎关注打赏
 */

public class NoteOperation {

    static void insert(ContentResolver resolver, ContentValues values) {

        resolver.insert(Uri.parse(NoteConstants.PROVIDER_URI), values);
    }

    static void query(ContentResolver resolver, String[] projection,
                      String selection, String[] selectionArgs) {
        Cursor cursor = resolver.query(Uri.parse(NoteConstants.PROVIDER_URI),
                projection, selection, selectionArgs, null);

        if (cursor != null) {
            while (cursor.moveToNext()){
                int priceIndex = cursor.getColumnIndex(NoteConstants.PRICE);
                String price = null;
                if (priceIndex > -1) {
                     price = cursor.getString(priceIndex);
                }

                int goodsIndex = cursor.getColumnIndex(NoteConstants.GOODS);
                String goods = null;
                if (goodsIndex > -1) {
                    goods = cursor.getString(goodsIndex);
                }

                int timeIndex = cursor.getColumnIndex(NoteConstants.TIME);
                String time = null;
                if (timeIndex > -1) {
                    time = cursor.getString(timeIndex);
                }

                int desIndex = cursor.getColumnIndex(NoteConstants.DESCRIPTION);
                String description = null;
                if (desIndex > -1) {
                    description = cursor.getString(desIndex);
                }

                int dirIndex = cursor.getColumnIndex(NoteConstants.DIRECT);
                String dir = null;
                if (dirIndex > -1) {
                    dir = (cursor.getString(dirIndex).equals(NoteConstants.INCOME) ? "income" : "expand" );
                }
                String msg = NoteConstants.PRICE + ":" + price + ","
                        + NoteConstants.GOODS + ":" + goods + ","
                        + NoteConstants.DESCRIPTION + ":" + description + ","
                        + NoteConstants.DIRECT + ":" + dir + ","
                        + NoteConstants.TIME + ":" + time ;
                Utils.logD(msg);
            }
            cursor.close();
        }
    }

    static void update(ContentResolver resolver, ContentValues values,
                       String selection, String[] selectionArgs){
        resolver.update(Uri.parse(NoteConstants.PROVIDER_URI), values, selection, selectionArgs);
    }

    static void delete(ContentResolver resolver, String selections, String[] selectionArgs) {
        resolver.delete(Uri.parse(NoteConstants.PROVIDER_URI),selections, selectionArgs);
    }
}
