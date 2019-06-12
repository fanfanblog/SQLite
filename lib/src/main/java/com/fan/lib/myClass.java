package com.fan.lib;

import com.fan.lib.list.ArrayListCustom;
import com.fan.lib.list.LinkedListCustom;
import com.fan.lib.tree.TreeBalance;
import com.fan.lib.tree.TreeNormal;

public class myClass {

    public static void main(String[] args) {

        //test customArrayList
//        testArray();
        //test Linked
//        testLinked();
//        testNormalTree();
        testBalanceTree();
//        int a = -127;
//
//        double pi_2 = (Math.PI)/180;
//        float last = 88.951996f;
//        float current = -87.961996f;
//        float cur = (float) ((current * pi_2 )/ Math.cos(current * pi_2));
//        System.out.print("current = ");
//        System.out.println(current);
//        System.out.print("Math.cos(current * pi_2) = ");
//        System.out.println(Math.cos(current * pi_2));
//        System.out.print("current / Math.cos(current * pi_2) = ");
//        System.out.println(cur);
//        System.out.println("********************");
//
//
//        float lastR = (float) ((last * pi_2) / Math.cos(last * pi_2));
//        System.out.print("last = ");
//        System.out.println(last);
//        System.out.print("Math.cos(last * pi_2) = ");
//        System.out.println(Math.cos(last * pi_2));
//        System.out.print("last / Math.cos(last * pi_2) = ");
//        System.out.println(lastR);
//        System.out.println("********************");
//
//        float offsetX = cur - lastR;
//        System.out.print("offsetX = ");
//        System.out.println(offsetX);
//        System.out.print("offsetX * Math.cos(last * pi_2) = ");
//        System.out.println(offsetX * Math.cos(current * pi_2));
//        System.out.println("********************");
//
//        System.out.print("Math.cos((current - last) * pi_2) = ");
//        System.out.println(Math.cos((current - last) * pi_2));
//        System.out.print("(current - last) / (Math.cos((current - last) * pi_2)) = ");
//        System.out.println((current - last) / (Math.cos((current - last) * pi_2)));
//
//        float result = (float) ((540 * Math.tan(89 * pi_2)) * (pi_2/Math.cos(last * pi_2)) * (1/Math.cos(pi_2))*(current - last));
//        System.out.print("result = ");
//        System.out.println(result);
//        float R = (float) ((1080/2) / Math.tan(89 * pi_2));
//        System.out.print("R = ");
//        System.out.println(R);
//
//        last = -79.951996f;
//        current = -78.961996f;
//        result = (float) (R * (Math.tan(current * pi_2) - Math.tan(last * pi_2)));
//        System.out.print("Math.tan(current * pi_2) = ");
//        System.out.println(Math.tan(current * pi_2));
//        System.out.print("Math.tan(last * pi_2) = ");
//        System.out.println(Math.tan(last * pi_2));
//        System.out.print("result = ");
//        System.out.println(result);
//
//
//        System.out.print("a  =");
//        System.out.println(Math.tan(90 * pi_2));
//
//        float currentDegree = -59;
//        float lastDegree  = 59;
//        float last_multX = 59;
//
//        float offsetX_hh = (float) (((currentDegree - lastDegree) * (1080/60)) / Math.cos(last_multX * pi_2));
//
//        System.out.print("offsetX_hh  =");
//        System.out.print(offsetX_hh);

    }


    private static void testLinked() {
        LinkedListCustom<String> linkedListCustom = new LinkedListCustom<>();

        for (int i = 0; i < 30; i++) {
            linkedListCustom.addLast("item " + i);
        }
//
//        linkedListCustom.removeNode(16);
//        linkedListCustom.addNode(17, "add");

        LinkedListCustom.LinkIterator linkIterator = linkedListCustom.linkIterator();
        while (linkIterator.hasNext()) {
            System.out.println(linkIterator.next());
        }

        while (linkIterator.hasPrevious()) {
            System.out.println(linkIterator.previous());
        }
    }

    private static void testArray() {
        ArrayListCustom<String> arrayListCustom = new ArrayListCustom<>(0);
        for (int i = 0; i < 50; i++) {
            arrayListCustom.addElement("element " + i);
        }

        arrayListCustom.removeElement(30);
        arrayListCustom.setElement(10, "modify");
        ArrayListCustom.MyIterator myIterator = arrayListCustom.listIterator();
        while (myIterator.hasNext()) {
            String str = (String) myIterator.next();
            System.out.println(str);
        }
        System.out.println(arrayListCustom.size());
    }


    private static void testNormalTree() {
        TreeNormal<String, String> tree = new TreeNormal<>();

        for (int i = 0; i < 20; i++) {
            tree.put("key：" + i, "value:" + i);
        }

        System.out.println("size = " + tree.size());
        tree.put("key：" + 2, "value:" + 5);

        TreeNormal.TreeKeyIterator iterator = tree.treeIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    private static void testBalanceTree() {
        TreeBalance<String, String> tree = new TreeBalance<>();

        for (int i = 0; i < 20; i++) {
            System.out.println("put key:" + i + ", value" + i);
            tree.put("key：" + i, "value:" + i);
        }

        System.out.println("size = " + tree.size());
//        tree.put("key：" + 2, "value:" + 5);

        TreeBalance.TreeKeyIterator iterator = tree.treeIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

}

class Item {
    Item left;
    Item right;
    Item parent;
    Object value;
    Object key;

    Item(Object k, Object v, Item parent) {
        this.key = k;
        this.value = v;
        this.parent = parent;
    }

    void setValue(Object value) {
        this.value = value;
    }

}


class TreeList {

    private static Item root;

    void addElement(Object key, Object value) {
        if (key == null || value == null || containKey(key) != null) {
            return;
        }
        if (root == null) {
            root = new Item(key, value, null);
        } else {
            Item t = root;
            Item parent;
            int cmp;
            do {
                //进行比较
                cmp = ((Comparable<Object>) key).compareTo(root.key);
                parent = t;
                if (cmp < 0) {
                    t = t.left;
                } else if (cmp > 0) {
                    t = t.right;
                } else {
                    t.setValue(value);
                    return;
                }
            } while (t != null);

            //绑定parent
            Item added = new Item(key, value, parent);

            //parent绑定子节点
            if (cmp > 0) {
                parent.right = added;
            } else {
                parent.left = added;
            }
        }
    }

    private Item containItem(Object key) {
        Item queryItem = null;
        Item temp = root;
        while (temp != null) {
            int cmp = ((Comparable<Object>) key).compareTo(temp.key);
            if (cmp < 0) {
                temp = temp.left;
            } else if (cmp > 0) {
                temp = temp.right;
            } else {
                queryItem = temp;
            }
        }
        return queryItem;
    }

    Object containKey(Object key) {
        Item query = containItem(key);
        return query == null ? null : query.value;
    }

    void remove(Object key) {
        Item remove = containItem(key);

        if (remove != null) {
            Item remLeft = remove.left;
            Item remRight = remove.right;
            if (remRight != null) {
                remRight.parent = remove.parent;
                Item remLeftOfRight = remRight.left;
                Item remLast = remRight;
                while (remLeftOfRight != null) {
                    remLast = remLeftOfRight;
                    remLeftOfRight = remLeftOfRight.left;
                }
                remLast.left = remLeft;
            }

        }
    }

    boolean setELement(Object key, Object value) {
        boolean isSucceed = false;
        Item setItem = containItem(key);
        if (setItem != null) {
            setItem.value = value;
            isSucceed = true;
        }
        return isSucceed;
    }
}