package com.fan.lib.list;


import java.util.Arrays;

/**
 * Created by FanFan on 19-3-7.
 * 自定义实现custom
 */

public class ArrayListCustom<E extends Object> {


    //容量为0的空数组
    private static final Object[] EMPTY_DATA = {};
    //步长，每次扩容的增量
    private static final int BLOCK_SIZE = 20;

    //存储元素的数组
    private static Object[] elementData = {};
    //元素的数量
    private static int size = 0;


    public ArrayListCustom(int capacity) {
        if (capacity < 0) {
            throw (new IllegalArgumentException("illegal capacity = " + capacity));
        } else if (capacity == 0) {
            elementData = EMPTY_DATA;
        } else {
            elementData = new Object[capacity];
        }
    }

    /**
     * 插入数据，
     * 不允许插入不同类型数据，
     * 不允许插入null，
     * 允许插入重复元素
     * 容量不够时，每次扩容20
     *
     * @param element element
     */
    public  void addElement(E element) {
        if (element == null) {
            throw (new NullPointerException("element should not be null"));
        }

        if (size < elementData.length) {
            //数据数量小于数组的容量，此时不需要扩容，直接赋值即可
            elementData[size++] = element;
        } else {
            //数据数量大于数组的容量，此时需要扩容,扩容20
            elementData = Arrays.copyOf(elementData, size + BLOCK_SIZE);
            elementData[size++] = element;
        }
    }

    public  void removeElement(int index) {
        if (!isValid(index)) {
            throw (new IndexOutOfBoundsException("index is invalid,please check it"));
        }
        //此时不需要修改容量大小，只需要修改size
        size--;
        System.arraycopy(elementData, index + 1, elementData, index, size - index);
    }

    public  void setElement(int index, E element) {
        if (!isValid(index)) {
            throw (new IndexOutOfBoundsException("index is invalid,please check it"));
        }

        elementData[index] = element;
    }


    public  MyIterator listIterator() {
        return new MyIterator();
    }

    /**
     * 判断index是否属于有效范围
     *
     * @param index index
     * @return true reference valid ,otherwise invalid
     */
    private static boolean isValid(int index) {
        return index >= 0 && index < size;
    }


    /**
     * 获取到当前的元素的数量，注意，不是数组容量，是元素的数量
     * @return
     */
    public int size() {
        return size;
    }


    /**
     * 自定义查找类
     */
    public class MyIterator {

        int cursor = 0;


        public boolean hasNext() {
            return cursor < size;
        }

        public Object next() {
            if (!isValid(cursor)) {
                throw (new IndexOutOfBoundsException("index is invalid,please check it"));
            }
            return elementData[cursor++];
        }
    }
}
