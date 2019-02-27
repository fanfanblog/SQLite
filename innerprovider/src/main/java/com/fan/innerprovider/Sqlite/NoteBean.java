package com.fan.innerprovider.Sqlite;

/**
 * Created by FanFan_code on 19-2-26.
 * WeChat zhangruifang3320
 * 微信搜索公众号 码农修仙儿，欢迎关注打赏
 */

public class NoteBean {

    private String direct;//收入or支出
    private String price;//消费金额
    private String time;//若无输入时间，则获取当前时间戳
    private String goods;//消费对象or收入来源
    private String description;//对自己说
    private int id;

    public NoteBean() {
    }

    public NoteBean(String direct, String price, String time, String goods, String description) {
        this.direct = direct;
        this.price = price;
        this.time = time;
        this.goods = goods;
        this.description = description;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "record is "
                + "goods: " + goods
                + ", price: " + price
                + ", description: " + description
                + ", id: " + id
                + ", time:" + time;
    }
}
