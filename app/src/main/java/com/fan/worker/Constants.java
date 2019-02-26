package com.fan.worker;

/**
 * Created by pc on 19-2-25.
 */

public class Constants {

    public static final String DB_NAME = "fan.db";
    public static final String TABLE_WORKER = "worker";

    /**
     * the columns of worker
     */
    public static final String NAME = "_name";
    public static final String AGE = "_age";
    public static final String ADDRESS = "_address";
    public static final String WAGE = "_wage";
    public static final String CREATE_WORKER = "create table " + TABLE_WORKER + " ("
            + NAME + " text not null,"
            + AGE + " int not null,"
            + ADDRESS + " char(50) not null,"
            + WAGE + " real not null"
            + ");";

    public static final int DEFAULT_AGE = 25;
    public static final long DEFAULT_WAGE = 20000;
    public static final String DEFAULT_ADDRESS = "China";

    public static final int TYPE_INSERT = 0;
    public static final int TYPE_UPDATE = 1;
    public static final int TYPE_DELETE = 2;
    public static final int TYPE_QUERY = 3;


}
