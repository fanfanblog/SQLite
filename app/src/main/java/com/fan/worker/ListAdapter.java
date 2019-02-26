package com.fan.worker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by FanFan on 19-2-25.
 */

public class ListAdapter extends BaseAdapter {

    private List<WorkerBean> workerList;
    private Map<String, WorkerBean> workMap;
    private Context context;
    private WorkerOperation operation;

    ListAdapter(Context context) {
        operation = new WorkerOperation(context);
        workerList = operation.queryAllWorker();
        workMap = new HashMap<>();
        this.context = context;
        list2Map();
    }


    boolean addWorker(WorkerBean workerBean) {
        boolean success = false;
        if (isExist(workerBean.getName())) {
            return success;
        }
        long row = operation.insertWorker(workerBean);
        if (row > -1) {
            workerList.add(workerBean);
            workMap.put(workerBean.getName(), workerBean);
            notifyDataSetChanged();
            success = true;
        }

        return success;

    }

    boolean deleteWorker(String name) {
        boolean success = false;
        if (!isExist(name)) {
            return success;
        }
        WorkerBean workerBean = workMap.get(name);
        int row = operation.deleteWorker(name);
        if (row > -1) {
            workMap.remove(name);
            workerList.remove(workerBean);
            notifyDataSetChanged();
            success = true;
        }

        return success;
    }

    boolean updateWorker(String old_name, WorkerBean workerBean) {
        boolean success = false;
        if (!isExist(old_name)) {
            return success;
        }

        int row = operation.updateWorker(old_name, workerBean);
        if (row > -1) {
            int update_index = workerList.indexOf(workMap.get(old_name));
            String name = workerBean.getName();
            workerBean = operation.queryWorker(name);
            workerList.set(update_index, workerBean);
            workMap.put(name, workerBean);
            notifyDataSetChanged();
            success = true;
        }
        return success;
    }

    WorkerBean queryWorker(String old_name) {
        return workMap.get(old_name);
    }


    /**
     * is exist in map or list
     *
     * @param name query name
     * @return isExist
     */
    private boolean isExist(String name) {
        return workMap.containsKey(name);
    }

    private void list2Map() {
        for (WorkerBean workerBean : workerList) {
            workMap.put(workerBean.getName(), workerBean);
        }
    }

    @Override
    public int getCount() {
        return workerList.size();
    }

    @Override
    public Object getItem(int i) {
        return workerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        }

        TextView name = view.findViewById(R.id.name);
        TextView age = view.findViewById(R.id.age);
        TextView address = view.findViewById(R.id.address);
        TextView wage = view.findViewById(R.id.wage);

        name.setText(workerList.get(i).getName());
        age.setText(String.valueOf(workerList.get(i).getAge()));
        address.setText(workerList.get(i).getAddress());
        wage.setText(String.valueOf(workerList.get(i).getWage()));


        return view;
    }
}
