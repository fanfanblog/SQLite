package com.fan.worker.note;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.fan.worker.*;
import com.fan.worker.Utils;

/**
 * Created by FanFan_code on 19-2-26.
 * WeChat zhangruifang3320
 * 微信搜索公众号 码农修仙儿，欢迎关注打赏
 */

public class NoteActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {


    private int checkedRadioId;
    private ContentResolver resolver;
    private Context context;

    private EditText priceEt;
    private EditText goodsEt;
    private EditText describeEt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_main);
        initView();
        context = this;
    }

    private void initView() {
        Button insertNote = findViewById(R.id.insert_note);
        Button deleteNote = findViewById(R.id.delete_note);
        Button updateNote = findViewById(R.id.update_note);
        Button queryNote = findViewById(R.id.query_note);


        priceEt = findViewById(R.id.price_et);
        goodsEt = findViewById(R.id.goods_et);
        describeEt = findViewById(R.id.describe_et);

        RadioGroup radioGroup = findViewById(R.id.direct);
        radioGroup.setOnCheckedChangeListener(this);
        checkedRadioId = radioGroup.getCheckedRadioButtonId();
        setClickListener(insertNote, deleteNote, updateNote, queryNote);
    }

    private void setClickListener(View... views) {
        for (View view : views) {
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
        switch (view.getId()) {
            case R.id.insert_note:

                String price = priceEt.getText().toString();
                String goods = goodsEt.getText().toString();
                String describe = describeEt.getText().toString();
                if (Utils.isEmpty(price) || Utils.isEmpty(goods) || Utils.isEmpty(describe)) {
                    Utils.logD(getString(R.string.failed));
                    return;
                }
                ContentValues values = new ContentValues();
                values.put(NoteConstants.PRICE, price);
                values.put(NoteConstants.GOODS, goods);
                values.put(NoteConstants.DESCRIPTION, describe);
                values.put(NoteConstants.TIME, SystemClock.elapsedRealtime());
                values.put(NoteConstants.DIRECT, checkedRadioId == R.id.income ?
                        NoteConstants.INCOME : NoteConstants.EXPAND);
                NoteOperation.insert(resolver, values);
                priceEt.setText("");
                goodsEt.setText("");
                describeEt.setText("");
                break;
            case R.id.delete_note:
                String delete_goods = goodsEt.getText().toString();
                String selection = NoteConstants.GOODS + "=?";
                NoteOperation.delete(resolver, selection, new String[]{delete_goods});
                goodsEt.setText("");
                break;
            case R.id.query_note:
                NoteOperation.query(resolver, null, null, null);
                break;
            case R.id.update_note:
                String query_goods = goodsEt.getText().toString();
                String query_selection = NoteConstants.GOODS + "=?";
                String update_price = priceEt.getText().toString();
                String update_goods = goodsEt.getText().toString();
                String update_describe = describeEt.getText().toString();
                ContentValues update_values = new ContentValues();
                if (!Utils.isEmpty(update_price)){
                    update_values.put(NoteConstants.PRICE, update_price);
                }
                if (!Utils.isEmpty(update_goods)){
                    update_values.put(NoteConstants.GOODS, update_goods);
                }
                if (!Utils.isEmpty(update_describe)){
                    update_values.put(NoteConstants.DESCRIPTION, update_describe);
                }
                NoteOperation.update(resolver,update_values,query_selection,new String[]{query_goods});
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        checkedRadioId = i;
    }



}
