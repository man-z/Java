package com.snw.test;

import java.util.Random;

/**
 * 跳跃表
 * @param <T>
 */
public class SkipList<T> {
    // 最高层数，
    private final int MAX_LEVEL;
    // 当前层数
    private int listLevel;
    // 表头,表示所有层数的所有数据（key），
    // 那么listHead.forward就当前所有的层listHead.forward[i]以及listHead.forward[i].forward表示每一层的所有的数据
    private SkipListNode<T> listHead;
    // 表尾
    private SkipListNode<T> NIL;
    // 生成randomLevel用到的概率值，可以使用Math.E(2.7)
    private final double P;
    // 论文里给出的最佳概率值
    private static final double OPTIMAL_P = 0.25;
    public SkipList() {
        // 0.25, 15
        this(OPTIMAL_P, (int)Math.ceil(Math.log(Integer.MAX_VALUE) / Math.log(1 / OPTIMAL_P)) - 1);
    }

    public SkipList(double probability, int maxLevel) {
        P = probability;
        MAX_LEVEL = maxLevel;

        listLevel = 1;
        listHead = new SkipListNode<T>(Integer.MIN_VALUE, null, maxLevel);
        NIL = new SkipListNode<T>(Integer.MAX_VALUE, null, maxLevel);
        for (int i = listHead.forward.length - 1; i >= 0; i--) {
            listHead.forward[i] = NIL;
        }
    }
    public T search(int searchKey) {
        SkipListNode<T> curNode = listHead;
        System.out.println("总共层数："+listLevel);
        for (int i = listLevel-1; i >= 0; i--) {
            while (curNode.forward[i].key <= searchKey) {
                curNode = curNode.forward[i];
            }
        }

        if (curNode.key == searchKey) {
            return curNode.value;
        } else {
            return null;
        }
    }

    public void insert(int searchKey, T newValue) {
        //记录要更新的层
        SkipListNode<T>[] update = new SkipListNode[MAX_LEVEL];
        SkipListNode<T> curNode = listHead;

        for (int i = listLevel - 1; i >= 0; i--) {
            //循环每层的数据，从最高层找
            while (curNode.forward[i].key < searchKey) {
                curNode = curNode.forward[i];
            }
            // curNode.key < searchKey <= curNode.forward[i].key
            //把小于新值的节点存到更新数组中，如果没有比新值小的，那么把最底层放入开头节点放入数组(最终取到第一层)
            //把每一层小于新值的节点元素放入数组
            update[i] = curNode;
        }
        //从最高层，或者是从最接近新插入值的大小的层的第一个开始
        curNode = curNode.forward[0];

        if (curNode.key == searchKey) {
            curNode.value = newValue;
        } else {
            int lvl = randomLevel();

            if (listLevel < lvl) {
                for (int i = listLevel; i < lvl; i++) {
                    update[i] = listHead;
                }
                listLevel = lvl;
            }

            SkipListNode<T> newNode = new SkipListNode<T>(searchKey, newValue, lvl);

            for (int i = 0; i < lvl; i++) {
                //关联新值与层数，比如：如果新值是第三层，那么同时指向第一二层，类似于，新插入的节点，把父节点指向新节点，新节点指向下一个节点
                newNode.forward[i] = update[i].forward[i];
                update[i].forward[i] = newNode;
            }
        }
    }

    public void delete(int searchKey) {
        SkipListNode<T>[] update = new SkipListNode[MAX_LEVEL];
        SkipListNode<T> curNode = listHead;

        for (int i = listLevel - 1; i >= 0; i--) {
            while (curNode.forward[i].key < searchKey) {
                curNode = curNode.forward[i];
            }
            // curNode.key < searchKey <= curNode.forward[i].key
            //把所有小于要删除的key的节点元素所在的层添加到数组中，为后面对应层数进行删除
            update[i] = curNode;
        }

        curNode = curNode.forward[0];

        if (curNode.key == searchKey) {
            //从最底层开始删除
            for (int i = 0; i < listLevel; i++) {
                //如果当前层没有要删除的key跳出循环
                if (update[i].forward[i] != curNode) {
                    break;
                }
                update[i].forward[i] = curNode.forward[i];
            }
            //如果删除的是一个关键节点，并且删除后，删除的节点所在的层数已经是空的，那么就把层数减一
            //比如：3在5层，1-5层都与3关联，那么删除3时，都需要移除掉，同时检查移除掉后关联的所有层是否还有关键节点
            while (listLevel > 0 && listHead.forward[listLevel - 1] == NIL) {
                listLevel--;
            }
        }
    }

    private int randomLevel() {
        int lvl = 1;
        while (lvl < MAX_LEVEL && Math.random() < P) {
            lvl++;
        }
        return lvl;
    }

    public void print() {
        for (int i = listLevel - 1; i >= 0; i--) {
            System.out.println(i+1+"层");
            SkipListNode<T> curNode = listHead.forward[i];
            while (curNode != NIL) {
                System.out.print(curNode.key + "->");
                curNode = curNode.forward[i];
            }
            System.out.println("NIL");
        }
        for (int i = listLevel - 1; i >= 0; i--) {
            System.out.println(i+1+"层");
            SkipListNode<T> curNode = listHead.forward[i];
            while (curNode != NIL) {
                System.out.print("↓->");
                curNode = curNode.forward[i];
            }
            System.out.println("NIL");
        }
    }

    public static void main(String[] args) {
        SkipList<Integer> sl = new SkipList<Integer>();
        Random r = new Random(100);
        for(int i = 1 ;i<=100;i++){
            int key = r.nextInt(100);
            sl.insert(key, key);
        }
        sl.insert(5, 5);
        sl.insert(75, 5);
        sl.insert(85, 5);
        sl.insert(65, 5);
        sl.insert(55, 5);
        sl.insert(11, 11);
        sl.insert(10, 10);
        sl.insert(12, 12);
        sl.insert(20, 20);
        sl.insert(70, 70);
        sl.insert(8, 8);
        sl.insert(1, 1);
        sl.insert(100, 100);
        sl.insert(80, 80);
        sl.insert(60, 60);
        sl.insert(30, 30);
        System.out.println("查找到30的节点："+sl.search(30));
        sl.print();
        System.out.println("---");
//        sl.myDelete(30);
        sl.delete(50);
//        sl.delete(100);
        System.out.println("删除后的跳跃表：");
        sl.print();
    }

    class SkipListNode<T>{
        int key;
        T value;
        SkipListNode[] forward;

        public SkipListNode(int key, T value, int level) {
            this.key = key;
            this.value = value;
            this.forward = new SkipListNode[level];
        }
    }
}
