package com.fan.lib.list;

/**
 * Created by FanFan on 19-3-7.
 * 链表结构
 * 操作对象：
 * <p>
 * 操作行为：
 * 增，删，改，查
 */

public class LinkedListCustom<I extends Object> {


    //the first node, 第一个节点
    private static Node first;
    //the last node, 最后一个节点
    private static Node last;
    private static int size;

    public LinkedListCustom() {

    }

    /**
     * 默认添加到链表尾端
     *
     * @param item
     */
    public void addLast(I item) {
        if (item == null) {
            throw (new NullPointerException("item is null "));
        }
        if (last == null) {
            first = last = new Node(item, null, null);
        } else {
            Node newNode = new Node(item, last, null);
            last.next = newNode;
            last = newNode;
        }
        size++;
    }

    /**
     * 添加到制定位置
     *
     * @param index
     * @param item
     */
    public void addNode(int index, I item) {
        Node oldNode = node(index);

//        if (oldNode == null) {
//            //说明目前链表是空的,这种情况不存在,因为我优先保证了index的有效性，也就是说肯定会有节点存在
//            first = last = new Node<I>(item, null, null);
//        }

        Node newNode = new Node(item, oldNode.prev, oldNode);

        //如果是第一个节点，那么插入后，要给first赋值
        if (oldNode.prev == null) {
            first = newNode;
        } else {
            oldNode.prev.next = newNode;
        }
        size++;
    }

    /**
     * 根据index获取到节点
     *
     * @param index
     * @return
     */
     private Node node(int index) {
        ensureValid(index);
        Node temp;
        if (index < size / 2) {
            temp = first;
            //属于前半部分，从前边儿开始查询
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
        } else {
            temp = last;
            for (int i = size - 1; i > index; i--) {
                temp = temp.prev;
            }
        }
        return temp;
    }

    /**
     * 移除最后一个
     */
    public void removeLast() {
        if (last == null) {
            //链表是空的，无法进行移除操作
            throw (new UnsupportedOperationException("last is null"));
        }
        last = last.prev;
        if (last == null) {
            first = null;
        } else {
            //解除和最后节点的绑定
            last.next = null;
        }
        size--;
    }

    /**
     * 移除制定位置的node
     *
     * @param index
     */
    public void removeNode(int index) {
        Node oldNode = node(index);
        Node preNode = oldNode.prev;
        Node nextNode = oldNode.next;
        if (preNode == null) {
            //当链表中要移除的节点位于链表开始位置时,需要给first重新赋值
            first = nextNode;
        } else {
            preNode.next = nextNode;
        }

        if (nextNode == null) {
            //当节点位于链表末尾时，
            last = preNode;
        }else {
            nextNode.prev = preNode;
        }
        size--;
    }

    /**
     * 对index位置的节点进行修改
     *
     * @param index
     * @param item
     */
    public void setNode(int index, I item) {
        if (item == null) {
            throw(new NullPointerException("item should not be null"));
        }
        Node updataNode = node(index);
        updataNode.item = item;
    }

    public int size() {
        return size;
    }

    private void ensureValid(int index) {
        if (index < 0 || index >= size) {
            throw (new IndexOutOfBoundsException("index is not in the scope"));
        }
    }

    public LinkIterator linkIterator(){
        return new LinkIterator();
    }

    public class LinkIterator {

        int cursor = 0;
        LinkIterator(){

        }
        public boolean hasPrevious() {
            return node(cursor).prev != null;
        }

        public boolean hasNext(){
            return node(cursor).next != null;
        }

        public Object previous(){
            return node(cursor--).prev.item;
        }

        public Object next(){
            return node(cursor++).next.item;
        }
    }
}




/**
 * 节点对象
 *
 */
class Node {
    Object item;
    Node prev;
    Node next;

    Node(Object item, Node prev, Node next) {
        this.item = item;
        this.prev = prev;
        this.next = next;
    }
}