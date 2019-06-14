package com.fan.lib.tree;

import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.tree.Tree;

/**
 * Created by FanFan on 19-3-14.
 * <p>
 * 平衡二叉树的性质
 * 1,只有一个根节点
 * 2,每个根节点最多有两个子节点
 * 3,key值遵循一定的排序,左<父<右
 * 4,根节点到空的叶子节点的最短距离和最长距离差不能超过1
 * <p>
 * 在进行增删改查时，先将节点插入到二叉树，再对二叉树进行调整
 */

public class TreeBalance<K extends Object, V extends Object> {


    public TreeBalance() {

    }

    private Node<K, V> root;

    private int size;

    /**
     * 插入一个节点，
     * 在插入之前，二叉树是平衡的
     * 1，首先执行插入操作,分三种情况
     * <1>若插入的为root节点，那么二叉树不需要进行任何调整</>
     * <2>若插入的是已存在的节点，那么二叉树只需要更新value,也不需要调整</>
     * <3>若插入的是新的节点，那就表示插入的一定是叶子节点，那么二叉树需要调整</>
     * 2，调整height和level
     * <1>level,由root到根节点 ++
     * <2>height，由根节点到root ++
     * 3，调整二叉树平衡，需要根据平衡二叉树的第四条性质，对二叉树进行相应的调整
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        if (key == null || value == null) {
            //不支持插入null的key或者null的value
            throw (new NullPointerException("null should not be supported"));
        }

        Node<K, V> temp = root;
        Node<K, V> tempParent = null;
        int cmp = 0;
        while (temp != null) {
            tempParent = temp;

            Comparable<Object> k = (Comparable<Object>) key;
            cmp = k.compareTo(temp.key);

            if (cmp < 0) {
                temp = temp.left;
            } else if (cmp > 0) {
                temp = temp.right;
            } else {
                temp.value = value;
                break;
            }
        }

        if (tempParent == null) {
            //说明root是null， 二叉树是空的
            root = new Node<>(key, value, null);
            size++;
        } else if (temp == null) {
            //说明root不为null，并且也没有重复数据，所以需要插入节点
            Node<K, V> newNode = new Node<>(key, value, tempParent);
            if (cmp < 0) {
                //添加到左叶子节点
                tempParent.left = newNode;
            } else {
                tempParent.right = newNode;
            }
            size++;

            increaseHeight(newNode, tempParent);

            /*
             *  至此，节点已经被添加成功，接下来就是看二叉树是否需要调整了
             *  依据二叉树的第4条性质来调整
             *  总结下来就是所插入的叶子节点的parent的parent的左右子树均需要存在
             *  否则，就会导致高度不满足要求，就需要调整
             *
             *  在添加的是非root节点并且也不是重复节点的情况下，
             *  就需要对二叉树进行判断调整
             *
             */

            System.out.println("newNode: info :" + newNode.toString());

            adjustTree();
        }


    }


    /**
     * 移除一个节点
     * 移除规则:
     * 找到要移除的节点的右分支的最小值来代替该节点
     * 如果该节点没有右分支,那么就用左节点代替,否则,就对该节点的parent进行对应的操作
     * 节点移除之后需要调节height和level
     *
     * @param key
     */
    public void remove(K key) {
        if (key == null) {
            throw (new NullPointerException("key should not be null"));
        }

        Node<K, V> remNode = node(key);
        if (remNode == null) {
            return;
        }

        Node<K, V> remLeft = remNode.left;
        Node<K, V> remRight = remNode.right;
        Node<K, V> remParent = remNode.parent;

        Node<K, V> minNode = minNodeOfRightTree(remNode);

        if (minNode != null) {
            /**
             * 右分之存在,
             * 此时minNode只含有右子树节点
             * minNode的右子树所有节点会会上移，所以调整level--
             * 1，调整minNode位置：minNode代替remNode的位置
             * 2，调整minNode右子树的位置minNode的右子树作为minNode.parent的左子树
             * 3，调整minNode右子树的level
             * 4，调整从minNode所有parent的height
             * 5，调整remNode的左右子树的parent
             */
            List<Node<K, V>> leftTree = iteratorTree(false, minNode);
            for (Node<K, V> node : leftTree) {
                node.level--;
            }

            //调整minNode的右子树
            if (minNode != minNode.parent.right) {
                /*
                 * 表明minNode不是remNode的右节点
                 * 调整minNode的parent和minNode的右子树
                 */
                minNode.parent.left = minNode.right;
                minNode.parent.leftHeight = minNode.rightHeight;
                if (minNode.right != null) {
                    minNode.right.parent = minNode.parent;
                }

                //节点有删减，所以需要调整height
                adjustParentHeight(minNode.parent);

                /*
                 * 对于remRight.parent的赋值必须放在adjustParentHeight之后，
                 * 否则对height的修改就会是死循环
                 */
                minNode.right = remRight;
                if (remRight != null) {
                    remRight.parent = minNode;
                }

            }

            /**
             * 调整remNode的左子树,交给minNode
             * 因为只是发生了节点的交换，所以不需要调整parent的height
             */
            minNode.left = remLeft;
            minNode.leftHeight = remNode.leftHeight;
            minNode.rightHeight = remNode.rightHeight;
            if (remLeft != null) {
                remLeft.parent = minNode;
            }

            //调整minNode
            minNode.parent = remParent;
            if (remParent != null) {
                if (remNode == remParent.left) {
                    remParent.left = minNode;
                } else {
                    remParent.right = minNode;
                }
            } else {
                root = minNode;
            }

        } else if (remLeft != null) {
             /*
             * level和height调节
             * 此时，因为不存在右子树，所以直接用左子树代替要移除的节点.所以左子树所有的level--,height保持不变
             */
            List<Node<K, V>> leftTree = iteratorTree(true, remNode);
            for (Node<K, V> node : leftTree) {
                node.level--;
            }
            //右分之不存在,所以只需要处理左分支
            remLeft.parent = remParent;
            if (remParent != null && (remNode == remParent.left)) {
                remParent.left = remLeft;
            } else if (remParent != null && (remNode == remParent.right)) {
                remParent.right = remLeft;
            } else {
                root = remLeft;
            }
            //remLeft的height无需改变
            adjustParentHeight(remLeft);
        } else {
            //左右分之都不存在,则只需要处理parent
            if (remParent != null && (remNode == remParent.left)) {
                remParent.left = null;
                remParent.leftHeight = 0;
            } else if (remParent != null && (remNode == remParent.right)) {
                remParent.right = null;
                remParent.rightHeight = 0;
            } else {
                //remParent 是null，也就是root节点
                root = null;
            }
            //此时不需要调整level，只需要调整height
            adjustParentHeight(remParent);
        }

        adjustTree();
        size--;
    }


    /**
     * 当节点的height发生改变时，调整对应parent...parent的height
     *
     * @param child
     */
    private void adjustParentHeight(Node child) {
        if (child == null) {
            return;
        }
        Node parent = child.parent;
        while (parent != null) {
            if (child == parent.left) {
                parent.leftHeight = Math.max(child.leftHeight, child.rightHeight) + 1;
            } else {
                parent.rightHeight = Math.max(child.leftHeight, child.rightHeight) + 1;
            }
            child = parent;
            parent = child.parent;
        }
    }

    /**
     * 找到rootNode的右子树的最小值
     *
     * @param rootNode
     * @return
     */
    private Node<K, V> minNodeOfRightTree(Node<K, V> rootNode) {
        if (rootNode == null) {
            return null;
        }
        Node<K, V> minNode = null;
        Node<K, V> temp = rootNode.right;
        while (temp != null) {
            minNode = temp;
            temp = temp.left;
        }

        return minNode;
    }

    /**
     * 找到rootNode的左子树的最大值
     *
     * @param rootNode
     * @return
     */
    private Node<K, V> maxNodeOfLeftTree(Node<K, V> rootNode) {
        if (rootNode == null) {
            return null;
        }
        Node<K, V> maxNode = null;
        Node<K, V> temp = rootNode.left;
        while (temp != null) {
            maxNode = temp;
            temp = temp.right;
        }

        return maxNode;
    }

    /**
     * 查找某个节点
     *
     * @param key
     * @return
     */
    private Node<K, V> node(K key) {
        if (key == null) {
            throw (new NullPointerException("key should not be null"));
        }

        Node<K, V> node = null;
        Comparable<Object> k = (Comparable<Object>) key;
        Node<K, V> temp = root;
        while (temp != null) {
            int cmp = k.compareTo(temp.key);
            if (cmp < 0) {
                temp = temp.left;
            } else if (cmp > 0) {
                temp = temp.right;
            } else {
                node = temp;
                break;
            }
        }
        return node;
    }

    /**
     * 如果添加的是root节点，则不需要调整
     * 如果是更新原有节点，也不需要调整
     * 只有新增了叶子节点，并且存在失衡节点才需要调整
     *
     */
    private void adjustTree() {
        Node<K, V> unBalanceNode = unbalanceNode();
        /*
         * 找到了失衡节点unBalanceNode,需要开始处理调整
         * 调整规则:
         * 1,查看是失衡原因位于失衡节点的左子树还是右子树:也就是比较左右子树的高度
         * 2,若位于左子树,则进行左子树右旋操作,rotateRight
         * 3,若位于右子树,则进行右子树左旋操作,rotateLeft
         *
         *  if(l == l.parent.)
         *
         *
         *   //v.parent.right = v.left;
         *   v.left.parent = v.parent;
         *
         *   v.right = unBalanceNode;
         *   v.left = unBalanceNode;
         *   v.parent = unBalanceNode.parent;
         *
         *   unBalanceNode.left.parent = v;
         *   unBalanceNode.parent = v;
         *   unBalanceNode.left = null;
         * 3,若位于右子树,则找到失衡节点的右子树的最小值r,作如下处理
         *    r.parent.left = r.right;
         *    r.right.parent = r.parent;
         *
         *    r.left = unBalanceNode;
         *    r.right = unBalanceNode.right;
         *    r.parent = unBalanceNode.parent;
         *
         *    unBalanceNode.parent = r;
         *    unBalanceNode.left.parent = r;
         * 4,旋转一次后再次检测二叉树是否平衡,若不平衡则重复步骤
         */
        while (unBalanceNode != null) {
            System.out.println("unBalanceNode:" + unBalanceNode);
            //如果unBalanceNode is null 二叉树中没有失衡节点,所以不需要进行旋转
            if ((unBalanceNode.leftHeight - unBalanceNode.rightHeight) > 1) {
                rotateRight(unBalanceNode);
            } else if ((unBalanceNode.leftHeight - unBalanceNode.rightHeight) < -1) {
                rotateLeft(unBalanceNode);
            }
            unBalanceNode = unbalanceNode();
        }
    }

    /**
     * 在插入node时调整每个node的左右子树的高度
     * 如果插入的是root节点,则level和height为0,不需要修改
     * 如果插入的是新的非root节点，也只可能是叶子节点,则需要修改对应的height和level
     * 1, 所插入的节点本身的height默认为0
     * 2, 所插入节点的level=parent.level+1;
     * 3, parent的height要修改,如果是增加的左子节点,那么左子树++. 如果增加的是右子节点,那么右子树++;
     * 4, parent.parent...的level不需要修改,至于左右子树的高度是否需要修改,则需要看情况
     * <1>如果插入的是左子节点,并且右子节点为null,那么parent.parent...的对应子树高度需要修改
     * <2>如果插入的是右子节点,并且左子节点为null,那么parent.parent...的对应子树高度需要修改
     *
     * @param childNode
     * @param parentNode
     */
    private void increaseHeight(Node<K, V> childNode, Node<K, V> parentNode) {
        Node<K, V> child = childNode;
        Node<K, V> parent = parentNode;

        if (child == null || parent == null) {
            throw (new NullPointerException("child or parent should not be null"));
        }

        /*
         * 1,3 调节新加入的节点的level:
         * 节点的level是parent+1
         */
        childNode.level = (parentNode.level + 1);

        /*
         * 2 修改parent以及parent.parent...的对应子树高度
         */
//        if ((child == parent.left && parent.right != null)) {
//            /*
//             * 如果添加的为parent的左分支,并且右分支也存在,
//             * 那么说明parent.parent...的height和level不需要再调节
//             * 只需要将该parent对应的height++即可
//             */
//            parent.leftHeight++;
//        } else if ((child == parent.right && parent.left != null)) {
//            parent.rightHeight++;
//        } else {
            /*
             * 4, 经历过上述的判断条件后,可以确定,该parent在之前是叶子节点,也就是没有子节点
             * 所以在添加子节点时,对应的height++;
             */

        Comparable<Object> childKey = (Comparable<Object>) child.key;
        Comparable<Object> parentKey = (Comparable<Object>) parent.key;
        int cmp = childKey.compareTo(parentKey);
        if (cmp < 0) {
            //位于parent的左子树，所以left的高度++
            parent.leftHeight++;
        } else {
            //位于parent的右子树，所以right的高度++
            parent.rightHeight++;
        }

        child = parent;
        parent = child.parent;
        while (parent != null) {
            childKey = (Comparable<Object>) child.key;
            parentKey = (Comparable<Object>) parent.key;
            cmp = childKey.compareTo(parentKey);
            if (cmp < 0) {
                //位于parent的左子树，所以left的高度++
                parent.leftHeight = Math.max(child.leftHeight, child.rightHeight) + 1;
            } else {
                //位于parent的右子树，所以right的高度++
                parent.rightHeight = Math.max(child.leftHeight, child.rightHeight) + 1;
            }
            child = parent;
            parent = child.parent;
        }
//            do {
//                /*
//                * 必须要有一次循环，因为parent增加了一个子叶子节点，所以其height肯定要增加
//                */
//                Comparable<Object> childKey = (Comparable<Object>) child.key;
//                Comparable<Object> parentKey = (Comparable<Object>) parent.key;
//                int cmp = childKey.compareTo(parentKey);
//                if (cmp < 0) {
//                    //位于parent的左子树，所以left的高度++
//                    parent.leftHeight++;
//                } else {
//                    //位于parent的右子树，所以right的高度++
//                    parent.rightHeight++;
//                }
//
//                /*
//                * 当该节点的左右子树等高时，不需要修改parent.parent对应的子树的高度
//                * 也就是说节点的height取决于left和right的最大值
//                * 如果当前节点的子树高度修改之后，
//                * 其parent的节点的左右子树高度一致，那就表示parent.parent的子树高度已经正常，不需要修改
//                *
//                */
//                if (parent.leftHeight == parent.rightHeight) {
//                    break;
//                }
//                child = parent;
//                parent = child.parent;
//            } while (parent != null);
//        }
    }


    /**
     * 找到最低失衡节点
     * 寻找方法:
     * 1,比较节点的左右子树高度:leftHeight和rightHeight
     * 2,确定该节点是所有失衡节点中level最大的,也就是最低失衡节点
     *
     * @return
     */
    private Node<K, V> unbalanceNode() {

        Node<K, V> temp = root;
        Node<K, V> tempParent = null;
        Node<K, V> lastBadNode = null;
        /*
         * 找到二叉树中的最小的节点：tempParent
         */
        while (temp != null) {
            tempParent = temp;
            temp = temp.left;
        }


        while (tempParent != null) {
            /*
             * 计算每个节点左右子树的高度差,找到失衡节点
             */
            int subValue = Math.abs(tempParent.leftHeight - tempParent.rightHeight);
            if (subValue > 1 && (lastBadNode == null || lastBadNode.level < tempParent.level)) {
                //高度差满足,并且遍历到的level是最大的,也就是位置最低
                lastBadNode = tempParent;
            }
            tempParent = nextNode(tempParent);
        }
        return lastBadNode;
    }

    /**
     * 在旋转之前,先调整各个节点的height.
     * 所需要调整的有三部分, 最低失衡节点,极值节点,极值节点的parent.parent..
     * 调整规则:
     * 1,badNode的height调整
     * 如果是右子树左旋,leftHeight保持不变,rightHeight => 0
     * 如果是左子树右旋,rightHeight保持不变,leftHeight => 0
     * 2,exNode的height调整
     * 如果是右子树左旋,leftHeight => badNode.leftHeight + 1, rightHeight => badNode.rightHeight - 1
     * TODO 是不是有问题
     * 如果是左子树右旋,rightHeight => badNode.rightHeight + 1, leftHeight => badNode.leftHeight - 1
     * 3,exNode的parent-->root
     * 所有节点对应的子树的height-1;
     *
     * @param exNode       极值节点
     * @param badNode      最低失衡节点
     * @param isRotateLeft 是否是右子树左旋
     */
    private void adjustHeightWhenRotate(Node<K, V> exNode, Node<K, V> badNode, boolean isRotateLeft) {
        if (isRotateLeft) {
            if (exNode != exNode.parent.right) {
                exNode.parent.leftHeight = exNode.rightHeight;
            } else {
                exNode.parent.rightHeight = exNode.rightHeight;
            }

            Node<K, V> child = exNode.parent;
            Node<K, V> parent = child.parent;
            while (parent != null) {
                if (child == parent.left) {
                    parent.leftHeight = Math.max(child.leftHeight, child.rightHeight) + 1;
                } else {
                    parent.rightHeight = Math.max(child.leftHeight, child.rightHeight) + 1;
                }

                child = parent;
                parent = child.parent;
            }

            exNode.leftHeight = badNode.leftHeight + 1;
            exNode.rightHeight = badNode.rightHeight;
            badNode.rightHeight = 0;
        } else {
            if (exNode != exNode.parent.left) {
                exNode.parent.rightHeight = exNode.leftHeight;
            } else {
                exNode.parent.leftHeight = exNode.leftHeight;
            }

            Node<K, V> child = exNode.parent;
            Node<K, V> parent = child.parent;
            while (parent != null) {
                if (child == parent.left) {
                    parent.leftHeight = Math.max(child.leftHeight, child.rightHeight) + 1;
                } else {
                    parent.rightHeight = Math.max(child.leftHeight, child.rightHeight) + 1;
                }

                child = parent;
                parent = child.parent;
            }
            exNode.rightHeight = badNode.rightHeight + 1;
            exNode.leftHeight = badNode.leftHeight;
            badNode.leftHeight = 0;
        }

//        if (isRotateLeft) {
//            exNode.leftHeight = badNode.leftHeight + 1;
//            exNode.rightHeight = badNode.rightHeight - 1;
//        }else {
//            exNode.rightHeight = badNode.rightHeight + 1;
//            exNode.leftHeight = badNode.leftHeight - 1;
//        }
//
//        Node<K,V> child = exNode;
//        Node<K,V> parent = child.parent;
//        while(parent != null) {
//            if (child == parent.left) {
//                parent.leftHeight--;
//            }else {
//                parent.rightHeight--;
//            }
//
//            child = parent;
//            parent = child.parent;
//        }
//        badNode.rightHeight = 0;
    }

    /**
     * 需要调整四个部分
     * 1,exNode极值点本身的level=>badNode.level
     * 2,badNode的level +1
     * 3,badNode的所有子节点的level++
     * 4,exNode的所有子节点的level --
     *
     * @param exNode       失衡极值点,
     * @param badNode      最低失衡节点
     * @param isRotateLeft 是否是右子树左旋
     */
    private void adjustLevelWhenRotate(Node<K, V> exNode, Node<K, V> badNode, boolean isRotateLeft) {

        //调用必须是在调整之前
        exNode.level = badNode.level;
        badNode.level += 1;

        //isRotateLeft为true表示右子树左旋,代表失衡节点的左子树需要调整
        List<Node<K, V>> leftTree = iteratorTree(isRotateLeft, badNode);
        for (Node<K, V> node : leftTree) {
            if (node != null) {
                node.level++;
            }
        }

        //右子树中的最小值exNode为极值点,所以exNode只有右子树
        List<Node<K, V>> ex_leftTree = iteratorTree(!isRotateLeft, exNode);
        for (Node<K, V> node : ex_leftTree) {
            if (node != null) {
                node.level--;
            }
        }
//
//
//        if (isRotateLeft) {
//            //右子树左旋,代表失衡节点的左子树需要调整
//            List<Node<K, V>> leftTree = iteratorTree(true, badNode);
//            for (Node<K, V> node : leftTree) {
//                if (node != null) {
//                    node.level++;
//                }
//            }
//
//            //右子树中的最小值exNode为极值点,所以exNode只有右子树
//            List<Node<K, V>> ex_leftTree = iteratorTree(false, exNode);
//            for (Node<K, V> node : ex_leftTree) {
//                if (node != null) {
//                    node.level--;
//                }
//            }
//        } else {
//            //左子树右旋
//            List<Node<K, V>> leftTree = iteratorTree(false, badNode);
//            for (Node<K, V> node : leftTree) {
//                if (node != null) {
//                    node.level++;
//                }
//            }
//
//            //左子树中的最大值exNode为极值点,所以exNode只有左子树
//            List<Node<K, V>> ex_leftTree = iteratorTree(true, exNode);
//            for (Node<K, V> node : ex_leftTree) {
//                if (node != null) {
//                    node.level--;
//                }
//            }
//        }

    }

    /**
     * @param leftTree
     * @param rootNode
     * @return
     */
    private List<Node<K, V>> iteratorTree(boolean leftTree, Node<K, V> rootNode) {
        List<Node<K, V>> treeList = new ArrayList<>();
        if (leftTree) {
            //遍历左子树
            Node<K, V> temp = rootNode.left;
            Node<K, V> minNode = null;
            while (temp != null) {
                minNode = temp;
                temp = temp.left;
            }

            //左子树的所有节点都小于rootNode
            while (minNode != null && minNode != rootNode) {
                treeList.add(minNode);
                minNode = nextNode(minNode);
            }
        } else {
            //遍历右子树
            Node<K, V> temp = rootNode.right;
            Node<K, V> maxNode = null;
            while (temp != null) {
                maxNode = temp;
                temp = temp.right;
            }

            while (maxNode != null && maxNode != rootNode) {
                treeList.add(maxNode);
                maxNode = prevNode(maxNode);
            }
        }
        return treeList;
    }


    /**
     * 找到上一个Node
     * 也就是找到比自己小的节点
     *
     * @param node
     * @return
     */
    private Node<K, V> prevNode(Node<K, V> node) {
        /*
        * 找到节点左子树的最大值
        */
        Node<K, V> temp = node.left;
        Node<K, V> tempParent = null;

        while (temp != null) {
            tempParent = temp;
            temp = temp.right;
        }

        /*
        * 节点左子树为空，所以去寻找节点作为右子树的最大的parent
        */
        if (tempParent == null) {
            temp = node;
            tempParent = temp.parent;
            while (tempParent != null && temp == tempParent.left) {
                temp = tempParent;
                tempParent = temp.parent;
            }
        }

//        if (node.left != null) {
//            return node.left;
//        }
//        Node<K, V> child = node;
//        Node<K, V> parent = node.parent;
//
//        /*
//        * 节点左子节点不存在，则寻找节点作为某个
//        */
//        while (parent != null && (child != parent.right)) {
//            child = parent;
//            parent = child.parent;
//        }

        return tempParent;
    }

    /**
     * 找到node的下一个node
     *
     * @param node
     * @return
     */
    private Node<K, V> nextNode(Node<K, V> node) {
        Node<K, V> temp = node.right;
        Node<K, V> tempParent = null;

        /*找到节点的右子树的最小值，即为节点的nextNode*/
        while (temp != null) {
            tempParent = temp;
            temp = temp.left;
        }

        if (tempParent == null) {
            //说明节点的右子树不存在，此时就要从parent中寻找，找到节点作为左子树的parent
            temp = node;
            tempParent = node.parent;
            while (tempParent != null && temp == tempParent.right) {
                temp = tempParent;
                tempParent = temp.parent;
            }
        }
        return tempParent;
    }

    /**
     * 向左旋转,代表失衡原因位于右子树
     * 规则永远是调整最低失衡节点badNode以及极值点r及其周围节点的关系
     * 1,找最低失衡节点的最小值节点minNode
     * 2,对r的节点以及parent进行赋值
     * r.left ==> badNode
     * r.parent ==> badNode.parent
     * 如果r是badNode的右子节点,那么r.right不需要修改
     * 如果r不是badNode的右子节点,那么r.right需要赋值
     * r.right ==> badNode.right;
     * badNode.right.parent ==> r
     * r.parent.left ==> r.right;
     * r.right.parent ==> r.parent;
     * <p>
     * 3,对badNode进行赋值
     * badNode.parent==>r;
     * badNode.right ==> null;
     * badNode.parent ==> r
     *
     * @param unBalanceNode
     */
    private void rotateLeft(Node<K, V> unBalanceNode) {

        Node<K, V> temp = unBalanceNode.right;
        Node<K, V> minNode = null;
        while (temp != null) {
            minNode = temp;
            temp = temp.left;
        }

        if (minNode == null) {
            //右子树为null, 不需要向左旋转
            return;
        }

        adjustLevelWhenRotate(minNode, unBalanceNode, true);
        adjustHeightWhenRotate(minNode, unBalanceNode, true);

        if (minNode != unBalanceNode.right) {
            minNode.parent.left = minNode.right;
            if (minNode.right != null) {
                minNode.right.parent = minNode.parent;
            }
            minNode.right = unBalanceNode.right;
            unBalanceNode.right.parent = minNode;
        }

        if (unBalanceNode.parent != null) {
            if (unBalanceNode.parent.left == unBalanceNode) {
                unBalanceNode.parent.left = minNode;
            } else {
                unBalanceNode.parent.right = minNode;
            }
        } else {
            root = minNode;
        }

        minNode.left = unBalanceNode;
        minNode.parent = unBalanceNode.parent;
        unBalanceNode.parent = minNode;
        unBalanceNode.right = null;
    }


    /**
     * 向右旋转,左子树失衡,右旋
     * 第一：找到失衡节点的左子树的最大值l
     * 第二：l代替失衡节点，也就是整个左子树右旋
     * 确认一点:l（左子树极值）因为是最大值,所以绝对不存在右子节点
     * 旋转时要调整哪些点?
     * 最低失衡节点badNode,左子树极值l,以及这两个节点的子节点和parent节点
     * 1,确定l的左右子节点以及parent:
     * 失衡节点badNode大于l,所以badNode设置为l的右子节点.
     * l的左子节点是原先badNode的左子节点
     * (排除掉l,如果原先badNode的左子节点是l,那么l的左子节点不变,依旧是自己原先的信息)
     * l的parent是badNode的parent
     * <p>
     * 2,处理l原先的左子节点 以及原先的l.parent
     * 如果l位于l.parent的左子节点,那就和<1>重合,也就是l.parent是badNode.不需要处理左子节点
     * 如果l位于l.parent的右子节点,那么l的左子节点是l.parent的右子节点
     * <p>
     * 伪代码如下:
     * if(l == badNode.left) {
     * l.right = badNode;
     * l.parent = badNode.parent;
     * badNode.left = null;
     * badNode.parent = l;
     * <p>
     * if(l.parent == null) {
     * root = l;
     * return;
     * }
     * if (l.parent.left == badNode) {
     * l.parent.left = l;
     * }else {
     * l.parent.right = l;
     * }
     * <p>
     * }else {
     * <p>
     * //处理l的parent和左子节点
     * l_parent = l.parent;
     * l_left = l.left;
     * l_parent.right = l.left;
     * l.left.parent = l_parent;
     * <p>
     * //重新为l的左右子节点和paren进行赋值
     * l.right = badNode;
     * l.left = badNode.left;
     * l.parent = badNode.parent;
     * badNode.left.parent = l;
     * badNode.parent = l;
     * <p>
     * if(l.parent == null) {
     * root = l;
     * return;
     * }
     * if (l.parent.left == badNode) {
     * l.parent.left = l;
     * }else {
     * l.parent.right = l;
     * }
     * }
     *
     * @param unBalanceNode
     */
    private void rotateRight(Node<K, V> unBalanceNode) {

        // TODO: 19-4-2 1,调整节点 2,调整height和level 

        Node<K, V> temp = unBalanceNode.left;
        Node<K, V> maxNode = null;
        while (temp != null) {
            maxNode = temp;
            temp = temp.right;
        }

        if (maxNode == null) {
            //左子树为null, 不需要向右旋转
            return;
        }

        adjustLevelWhenRotate(maxNode, unBalanceNode, false);
        adjustHeightWhenRotate(maxNode, unBalanceNode, false);

        /*
         * 管理maxNode的left节点和parent节点
         */
        if (maxNode != unBalanceNode.left) {
            maxNode.parent.right = maxNode.left;
            if (maxNode.left != null) {
                maxNode.left.parent = maxNode.parent;
            }
            maxNode.left = unBalanceNode.left;
            unBalanceNode.left.parent = maxNode;
        }

        /*
         * 管理unBalanceNode的parent节点
         */
        if (unBalanceNode.parent != null) {
            if (unBalanceNode.parent.left == unBalanceNode) {
                unBalanceNode.parent.left = maxNode;
            } else {
                unBalanceNode.parent.right = maxNode;
            }
        } else {
            root = maxNode;
        }

        maxNode.right = unBalanceNode;
        maxNode.parent = unBalanceNode.parent;
        unBalanceNode.left = null;
        unBalanceNode.parent = maxNode;




        /*
         * 调整失衡节点的右子树的level,失衡节点右子树的height不需要调整,左子树的height为null
         * 调整minNode的rightHeight: unBalance.rightHeight + 1
         *
         */
//        maxNode.level = unBalanceNode.level;
//        maxNode.rightHeight = unBalanceNode.rightHeight + 1;
//        Node<K, V> unBalanceRightTree = unBalanceNode;
//        while (unBalanceRightTree != null) {
//            unBalanceRightTree.level++;
//            unBalanceRightTree = nextNode(unBalanceRightTree);
//        }
    }


    public int size() {
        return size;
    }


    class Node<K, V> {
        Node<K, V> parent;
        Node<K, V> left;
        Node<K, V> right;
        K key;
        V value;
        int leftHeight;//左子树的深度
        int rightHeight;//右子树的深度
        int level;//从root节点为0开始,依次累加

        Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            leftHeight = 0;
            rightHeight = 0;
            level = 0;
        }

        @Override
        public String toString() {
            return "Key:" + key
                    + ", value: " + value
                    + ", leftHeight: " + leftHeight
                    + ", rightHeight: " + rightHeight
                    + ", level: " + level;
        }
    }

    public TreeBalance.TreeKeyIterator treeIterator() {
        return new TreeKeyIterator();
    }

    public class TreeKeyIterator {


        TreeBalance.Node curNode;//curNode是遍历至最小的


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
                TreeBalance.Node left = curNode;
                TreeBalance.Node leftParent = null;
                while (left != null) {
                    leftParent = left;
                    left = left.left;
                }
                curNode = leftParent;
            } else {

                //每个节点的下一个，首先找寻他的右子树，如果右子树是空，则找寻该节点位于哪个节点的左子树中
                TreeBalance.Node temp = curNode.right;
                TreeBalance.Node tempParent = null;
                while (temp != null) {
                    tempParent = temp;
                    temp = temp.left;
                }

                if (tempParent != null) {
                    curNode = tempParent;

                } else {
                    //没有右分支，此时要看是父节点的左子节点还是右子节点

                    TreeBalance.Node parent = curNode.parent;
                    TreeBalance.Node child = curNode;

                    while (parent != null && child == parent.right) {
                        child = parent;
                        parent = child.parent;
                    }

                    curNode = parent;
                }


                cnt++;
            }
            if (curNode != null) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
                System.out.println("curNode = " + curNode.toString());
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
            }
            return curNode == null ? null : curNode.value;
        }


    }
}



