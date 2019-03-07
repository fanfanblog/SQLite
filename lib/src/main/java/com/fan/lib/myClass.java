package com.fan.lib;

import com.fan.lib.list.ArrayListCustom;

public class myClass {

    public static void main(String[] args) {

        //test customArrayList

        ArrayListCustom<String> arrayListCustom = new ArrayListCustom<>(0);
        for (int i = 0; i < 50; i++) {
            arrayListCustom.addElement("element " + i);
        }

        arrayListCustom.removeElement(30);
        arrayListCustom.setElement(10, "modify");
        ArrayListCustom.MyIterator myIterator = arrayListCustom.listIterator();
        while(myIterator.hasNext()) {
            String str = (String) myIterator.next();
            System.out.println(str);
        }
        System.out.println(arrayListCustom.size());

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