package com.fan.worker;

/**
 * Created by FanFan on 19-2-25.
 * name---age---address--wage
 *
 */

public class WorkerBean {

    private String name;
    private String age;
    private String address;
    private String wage;


    WorkerBean(){}

    public WorkerBean(String name, String age, String address, String wage) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.wage = wage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    @Override
    public String toString() {
        return  "worker is"
                + "name =" + name
                + " ,age=" + age
                + " ,address=" + address
                + " ,wage=" + wage;
    }
}
