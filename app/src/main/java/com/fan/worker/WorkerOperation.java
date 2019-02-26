package com.fan.worker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 19-2-25.
 */

public class WorkerOperation {

    private  WorkerOpenHelper workerOpenHelper;
    private  SQLiteDatabase sqLiteDatabase;

    public WorkerOperation(Context context){
        workerOpenHelper = WorkerOpenHelper.getWorkOHInstance(context);
    }
    public long insertWorker(WorkerBean worker){
        long row = -1;
        sqLiteDatabase = workerOpenHelper.getWritableDatabase();
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()){
            ContentValues values = new ContentValues();
            values.put(Constants.NAME, worker.getName());
            values.put(Constants.AGE, worker.getAge());
            values.put(Constants.ADDRESS, worker.getAddress());
            values.put(Constants.WAGE, worker.getWage());
            row = sqLiteDatabase.insert(Constants.TABLE_WORKER,null,values);
            sqLiteDatabase.close();
        }
        return row;
    }

    public int deleteWorker(String name){
        int row = -1;
        sqLiteDatabase = workerOpenHelper.getWritableDatabase();
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            row = sqLiteDatabase.delete(Constants.TABLE_WORKER,  Constants.NAME + "=?", new String[]{name});
            sqLiteDatabase.close();
        }
        return row;
    }

    public int updateWorker(String old_name, WorkerBean worker){
        int row = -1;
        sqLiteDatabase = workerOpenHelper.getWritableDatabase();
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(Constants.NAME, worker.getName());
            if (!Utils.isEmpty(worker.getAge())) {
                values.put(Constants.AGE, worker.getAge());
            }
            if (!Utils.isEmpty(worker.getWage())) {
                values.put(Constants.WAGE, worker.getWage());
            }
            if (!Utils.isEmpty(worker.getAddress())) {
                values.put(Constants.ADDRESS, worker.getAddress());
            }

            row = sqLiteDatabase.update(Constants.TABLE_WORKER, values, Constants.NAME + "=?", new String[]{old_name});
            sqLiteDatabase.close();
        }
        return row;
    }

    public WorkerBean queryWorker(String old_name){
        WorkerBean workerBean = null;
        sqLiteDatabase = workerOpenHelper.getWritableDatabase();
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {

            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_WORKER, null, Constants.NAME + "=?",
                    new String[]{old_name}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()){
                    workerBean = getWorker(cursor);
                }
                cursor.close();
            }

            sqLiteDatabase.close();
        }
        return workerBean;
    }

    public List<WorkerBean> queryAllWorker(){
        List<WorkerBean> workerList = new ArrayList<>();
        sqLiteDatabase = workerOpenHelper.getWritableDatabase();
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {

            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_WORKER, null, null,
                    null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()){
                    workerList.add(getWorker(cursor));
                }
                cursor.close();
            }

            sqLiteDatabase.close();
        }

        return workerList;
    }


    private WorkerBean getWorker(Cursor cursor){
        WorkerBean workerBean = new WorkerBean();
        int nameIndex = cursor.getColumnIndex(Constants.NAME);
        int addressIndex = cursor.getColumnIndex(Constants.ADDRESS);
        int wageIndex = cursor.getColumnIndex(Constants.WAGE);
        int ageIndex = cursor.getColumnIndex(Constants.AGE);
        String name = cursor.getString(nameIndex);
        String address = cursor.getString(addressIndex);
        String age = cursor.getString(ageIndex);
        String wage = cursor.getString(wageIndex);

        workerBean.setAddress(address);
        workerBean.setName(name);
        workerBean.setAge(age);
        workerBean.setWage(wage);
        return workerBean;
    }
}
