package com.fan.worker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.worker.note.NoteActivity;


/**
 * For users, WorkerBean & adapter is usable.But WorkOpenHelper & WorkOperation is unusable
 */
public class MainActivity extends Activity implements View.OnClickListener {


    private ListView listView;
    private EditText nameEt;
    private EditText ageEt;
    private EditText addressEt;
    private EditText wageEt;
    private EditText oldNameEt;

    private Button insert;
    private Button update;
    private Button delete;
    private Button query;
    private Button confirm;

    private TextView result;

    private ListAdapter mAdapter;
    private int operateType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        initView();
    }

    private void initView() {

        listView = findViewById(R.id.list);
        nameEt = findViewById(R.id.name_et);
        wageEt = findViewById(R.id.wage_et);
        addressEt = findViewById(R.id.address_et);
        ageEt = findViewById(R.id.age_et);
        oldNameEt = findViewById(R.id.old_name_et);

        insert = findViewById(R.id.insert);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        query = findViewById(R.id.query);
        confirm = findViewById(R.id.confirm);
        result = findViewById(R.id.result);

        Button jump = findViewById(R.id.jump_note);

        confirm.setEnabled(false);
        setViewVisible(false, false, false);
        nameEt.addTextChangedListener(mWatcher);
        oldNameEt.addTextChangedListener(mWatcher);
        setClickListener(insert, update, delete, query, confirm,jump);
    }

    /**
     * set onClickerListener for views
     *
     * @param views views to set click
     */
    private void setClickListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        mAdapter = new ListAdapter(getApplicationContext());
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.insert:
                setViewVisible(false, true, true);
                operateType = Constants.TYPE_INSERT;
                break;
            case R.id.update:
                setViewVisible(true, true, true);
                operateType = Constants.TYPE_UPDATE;
                break;
            case R.id.delete:
                setViewVisible(true, false, false);
                operateType = Constants.TYPE_DELETE;
                break;
            case R.id.query:
                setViewVisible(true, false, false);
                operateType = Constants.TYPE_QUERY;
                break;
            case R.id.confirm:
                execute();
                break;
            case R.id.jump_note:
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void execute() {
        switch (operateType) {
            case Constants.TYPE_INSERT:
                insert();
                break;
            case Constants.TYPE_DELETE:
                delete();
                break;
            case Constants.TYPE_QUERY:
                query();
                break;
            case Constants.TYPE_UPDATE:
                update();
                break;
            default:
                break;
        }
        clearEditText();
    }

    /**
     * execute insert
     */
    private void insert() {

        String name = nameEt.getText().toString().trim();
        String address = addressEt.getText().toString();
        String age = ageEt.getText().toString();
        String wage = wageEt.getText().toString();
        if (Utils.isEmpty(name) || Utils.isEmpty(address)
                || Utils.isEmpty(age) || Utils.isEmpty(wage)) {

            Utils.showT(this, R.string.failed);
            return;
        }
        WorkerBean workerBean = new WorkerBean(name,
                age, address, wage);
        boolean success = mAdapter.addWorker(workerBean);
        result.setText(success ? R.string.success : R.string.failed);
    }

    /**
     * at least the name should be updated
     */
    private void update() {
        String old_name = oldNameEt.getText().toString().trim();
        String name = nameEt.getText().toString();
        if (Utils.isEmpty(old_name) || Utils.isEmpty(name)) {
            Utils.showT(this, R.string.failed);
            return;
        }
        WorkerBean workerBean = new WorkerBean();
        workerBean.setName(name);

        String address = addressEt.getText().toString();
        String age = ageEt.getText().toString();
        String wage = wageEt.getText().toString();
        if (!Utils.isEmpty(address)) {
            workerBean.setAddress(address);
        }
        if (!Utils.isEmpty(age)) {
            workerBean.setAge(age);
        }
        if (!Utils.isEmpty(wage)) {
            workerBean.setWage(wage);
        }
        boolean success = mAdapter.updateWorker(old_name, workerBean);
        result.setText(success ? R.string.success : R.string.failed);

    }

    private void delete() {
        String old_name = oldNameEt.getText().toString().trim();
        if (Utils.isEmpty(old_name)) {
            Utils.showT(this, R.string.failed);
            return;
        }

        boolean success = mAdapter.deleteWorker(old_name);
        result.setText(success ? R.string.success : R.string.failed);
    }

    private void query() {
        String old_name = oldNameEt.getText().toString().trim();
        if (old_name.length() < 1) {
            Utils.showT(this, R.string.failed);
            return;
        }
        WorkerBean workerBean = mAdapter.queryWorker(old_name);
        result.setText(workerBean == null ? getString(R.string.not_known) : workerBean.toString());
    }


    /**
     * according to user operation,  visible|gone editText
     *
     * @param oldNameVisible old_name_editText visible
     * @param nameVisible    name_editText visible
     * @param otherVisible   other editText visible
     */
    private void setViewVisible(boolean oldNameVisible, boolean nameVisible, boolean otherVisible) {
        oldNameEt.setVisibility(oldNameVisible ? View.VISIBLE : View.GONE);
        nameEt.setVisibility(nameVisible ? View.VISIBLE : View.GONE);
        addressEt.setVisibility(otherVisible ? View.VISIBLE : View.GONE);
        ageEt.setVisibility(otherVisible ? View.VISIBLE : View.GONE);
        wageEt.setVisibility(otherVisible ? View.VISIBLE : View.GONE);
    }

    private void clearEditText(){
        oldNameEt.setText("");
        nameEt.setText("");
        ageEt.setText("");
        addressEt.setText("");
        wageEt.setText("");
    }

    /**
     * register textWatcher for old_name_editText & name_editText
     */
    public TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String str = editable.toString();
            if (str.length() > 0) {
                confirm.setEnabled(true);
            } else {
                confirm.setEnabled(false);
            }
        }
    };
}
