package com.fan.innerprovider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.fan.innerprovider.Sqlite.NoteBean;
import com.fan.innerprovider.Utils.Constants;

public class MainActivity extends Activity implements View.OnClickListener{


    private EditText priceEt;
    private EditText goodsEt;
    private ListView list;
    private ContentResolver resolver;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        context = this;
    }



    private void initView() {
        priceEt = findViewById(R.id.price);
        goodsEt = findViewById(R.id.goods);
        Button insertBtn = findViewById(R.id.add);
        Button queryBtn = findViewById(R.id.query);
        Button deleteBtn = findViewById(R.id.delete);
        Button updateBtn = findViewById(R.id.update);

        list = findViewById(R.id.list_note);
        setViewClickListener(insertBtn,queryBtn, deleteBtn, updateBtn);
    }

    private void setViewClickListener(View...views) {
        for (View view:views) {
            view.setOnClickListener(this);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        resolver = getContentResolver();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:
                insert();
                break;
            case R.id.query:
                query();
                break;
            case R.id.delete:
                delete();
                break;
            case R.id.update:
                update();
                break;
            default:
                break;
        }
    }

    private void insert(){
        String price = priceEt.getText().toString();
        String goods = goodsEt.getText().toString();

        ContentValues values = new ContentValues();
        values.put(Constants.PRICE, price);
        values.put(Constants.GOODS, goods);
        values.put(Constants.DIRECT, "1");
        values.put(Constants.TIME, SystemClock.elapsedRealtime());
        values.put(Constants.DESCRIPTION, "TEST");
        Uri uri = Uri.parse(Constants.PROVIDER_URI);
        resolver.insert(uri, values);
    }

    private void query(){
        Uri uri = Uri.parse(Constants.PROVIDER_URI);

        String[] projection = new String[]{Constants.PRICE, Constants.GOODS};
        Cursor cursor = resolver.query(uri,projection,null,null,null,null);
        if (cursor != null) {
            while (cursor.moveToNext()){
                NoteBean noteBean = null;
                int priceIndex = cursor.getColumnIndex(Constants.PRICE);
                String price = cursor.getString(priceIndex);
                int goodsIndex = cursor.getColumnIndex(Constants.GOODS);
                String goods = cursor.getString(goodsIndex);
//                int timeIndex = cursor.getColumnIndex(Constants.TIME);
//                String time = cursor.getString(timeIndex);
//                int desIndex = cursor.getColumnIndex(Constants.DESCRIPTION);
//                String description = cursor.getString(desIndex);
//                int idIndex = cursor.getColumnIndex(Constants.ID);
//                int id = cursor.getInt(idIndex);
//                int dirIndex = cursor.getColumnIndex(Constants.DIRECT);
//                String dir = cursor.getString(dirIndex);
//                noteBean = new NoteBean(dir,price,time,goods,description);
//                noteBean.setId(id);

                noteBean = new NoteBean();
                noteBean.setGoods(goods);
                noteBean.setPrice(price);
                Log.i("fan",noteBean.toString());
            }
            cursor.close();
        }
    }

    private void delete() {
        Uri uri = Uri.parse(Constants.PROVIDER_URI);
        String where =  Constants.GOODS +  "=? and " + Constants.PRICE  + "=?";
        String[] selectionArgs = new String[]{"手机","5555"};
        resolver.delete(uri, where, selectionArgs);
    }

    private void update() {
        Uri uri = Uri.parse(Constants.PROVIDER_URI);
        String where = Constants.GOODS + "=?";
        String[] selectionArgs = new String[]{"手机"};
        ContentValues values = new ContentValues();
        values.put(Constants.GOODS, "更新手机");
        values.put(Constants.DESCRIPTION, "重新更新");
        resolver.update(uri,values,where,selectionArgs);
    }
 }
