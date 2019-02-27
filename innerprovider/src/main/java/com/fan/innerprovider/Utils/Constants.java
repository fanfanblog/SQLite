package com.fan.innerprovider.Utils;

/**
 * Created by FanFan_code on 19-2-26.
 * WeChat zhangruifang3320
 * 微信搜索公众号 码农修仙儿，欢迎关注打赏
 */

public class Constants {

    public static final String DB_NAME = "fan.db";
    public static final String NOTE_TABLE_NAME = "note";
    public static final String AUTHORIES = "com.fan.noteprovider";

    public static final String DIRECT = "_direct";
    public static final String PRICE = "_price";
    public static final String TIME = "_time";
    public static final String GOODS = "_goods";
    public static final String DESCRIPTION = "_description";
    public static final String ID = "_id";

    public static final int QUERY_ALL = 0;
    public static final int QUERY_ITEM = 1;

    public static final String PROVIDER_URI = "content://" + AUTHORIES + "/" + NOTE_TABLE_NAME;
}
