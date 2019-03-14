package com.fan.lib.tree;

import java.util.Iterator;

/**
 * Created by FanFan on 19-3-11.
 * 搜索微信公众号码农修仙儿（fanfan_code）
 */

public class TreeNormal<K extends Object, V extends Object> {


    private Node root;

    private int size;


    public void put(K key, V value) {

        if (key == null || value == null) {
            throw (new NullPointerException("key and value should not be null"));
        }

        if (root == null) {
            root = new Node<>(key, value, null);
            size++;
        } else {
            int cmp = 0;
            Node temp = root;
            Node parent;
            Comparable<Object> k = (Comparable<Object>) key;
            do {

                cmp = k.compareTo(temp.key);
                parent = temp;
                if (cmp < 0) {
                    temp = parent.left;
                } else if (cmp > 0) {
                    temp = parent.right;
                } else {
                    temp.value = value;
                    break;
                }
            } while (temp != null);
            if (temp == null) {
                Node newNode = new Node<>(key, value, parent);
                if (cmp < 0) {
                    parent.left = newNode;
                } else {
                    parent.right = newNode;
                }
                size++;
            }

        }
    }

    public int size() {
        return size;
    }

    public boolean containKey(K key) {
        return node(key) != null;
    }

    /**
     * 有两种方式
     * 1，寻找移除节点的右子树的最小值：遍历右子树中的所有左子节点
     * 2，寻找移除节点的左子树的最大值
     * 本代码使用第一种方式实现
     * 伪代码
     * if rem != null
     * parent = rem.parent
     * left = rem.left
     * right = rem.right
     * <p>
     * temp = right
     * B = null
     * <p>
     * while temp != null//循环遍历至右子树的最小的节点
     * B = temp
     * temp = temp.left
     * <p>
     * <p>
     * if B != null//找到右子树最小节点，并进行对应赋值，如果rem != null，那么B.parent是一定存在的
     * B.parent.left = null//因为B要上调到要移除的位置，所以清除B.parent的left节点
     * B.parent = parent
     * B.left = left
     * B.right = right
     * <p>
     * if left != null//对left的parent指向B
     * left.parent = B
     * <p>
     * if right != null
     * right.parent = B
     * <p>
     * if parent == null//移除的是根节点，所以需要给根节点重新赋值
     * root = (B == null) ? left : B//代表right为null，也就是只有左子树，所以left为root,如果left也为null，那么root就是null
     *
     * @param key
     */
    public void remove(K key) {
        if (key == null) {
            throw (new NullPointerException("key should not be null"));
        }
        Node removeNode = node(key);

        if (removeNode == null) {
            return;
        }
        Node parent = removeNode.parent;
        Node left = removeNode.left;
        Node right = removeNode.right;

        Node temp = right;
        Node tempParent = null;
        while (temp != null) {
            tempParent = temp;
            temp = temp.left;
        }

        if (tempParent != null) {
            tempParent.parent.left = null;
            tempParent.parent = parent;
            tempParent.left = left;
            tempParent.right = right;
        }

        if (left != null) {
            left.parent = tempParent;
        }

        if (right != null) {
            right.parent = tempParent;
        }

        //接下来要为root赋值
        if (parent == null) {
            //要移除的是root根节点
            root = (tempParent == null) ? left : tempParent;
        }


        size--;

    }

    public void set(K key, V value) {
        Node updateNode = node(key);
        if (updateNode != null) {
            updateNode.value = value;
        }
    }

    /**
     * 通过key 遍历找到对应节点，若不存在，返回null
     *
     * @param key
     * @return
     */
    private Node node(K key) {
        Node node = null;
        if (key == null || root == null) {
            return null;
        }
        Comparable<Object> k = (Comparable<Object>) key;
        Node temp = root;
        int cmp = 0;
        do {
            cmp = k.compareTo(temp.key);

            if (cmp < 0) {
                temp = temp.left;
            } else if (cmp > 0) {
                temp = temp.right;
            } else {
                node = temp;
                break;
            }
        } while (temp != null);
        return node;
    }


    public TreeKeyIterator treeIterator() {
        return new TreeKeyIterator();
    }

    public class TreeKeyIterator {


        Node curNode;//curNode是遍历至最小的


        TreeKeyIterator() {
            curNode = root;
        }

        int cnt = 0;

        /**
         * 判断有没有parent或者right，如果没有，那么节点就没有下一个
         *
         * @return
         */
        public boolean hasNext() {
            return cnt < size;
        }

        public Object next() {

            if (curNode == null) {
                throw (new NullPointerException("curNode should not be null"));
            }

            if (cnt == 0) {

                //找寻最小的key
                cnt++;
                Node left = curNode;
                Node leftParent = null;
                while (left != null) {
                    leftParent = left;
                    left = left.left;
                }
                curNode = leftParent;
            } else {

                Node temp = curNode.right;
                Node tempParent = null;
                while (temp != null) {
                    tempParent = temp;
                    temp = temp.left;
                }

                if (tempParent != null) {
                    curNode = tempParent;

                } else {
                    //没有右分支，此时要看是父节点的左子节点还是右子节点

                    Node parent = curNode.parent;
                    Node child = curNode;

                    while (parent != null && child == parent.right) {
                        child = parent;
                        parent = child.parent;
                    }

                    curNode = parent;
                }


                cnt++;
            }
            return curNode == null ? null : curNode.value;
        }


    }

    /**
     * 抽象数据类型
     *
     * @param <K>
     * @param <V>
     */
    static class Node<K, V> {
        Node<K, V> parent;
        Node<K, V> left;
        Node<K, V> right;
        K key;
        V value;

        Node(K key, V value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }
}
