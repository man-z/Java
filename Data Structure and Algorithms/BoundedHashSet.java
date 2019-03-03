package test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class BoundedHashSet<T  extends Comparable> {
    private final Set<T> set;
    private final Semaphore sem;
    public BoundedHashSet(int bound){
        this.set = Collections.synchronizedSet(new HashSet<T>());
        sem = new Semaphore(bound);
    }
    public boolean add(T o) throws InterruptedException {
        sem.acquire();
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return wasAdded;
        } finally {
            if(!wasAdded){
                sem.release();
            }
        }
    }

    public boolean remove(Object o){
        boolean remove = set.remove(o);
        if(remove){
            sem.release();
        }
        return  remove;
    }

    public static void main(String[] args) {
        final BoundedHashSet<Integer> nh = new BoundedHashSet<Integer>(2);
        try {
            nh.add(1);

            System.out.println("执行插入完毕！");
//            nh.add(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    nh.add(3);
//                    nh.remove(1);
                    nh.add(4);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        t.start();
        System.out.println("所有插入完成！");
    }
}
