package test;

import java.util.Random;

/**
 * treap树
 */
public class TreapTree<T extends Comparable> {
    private TreapNode<T> nullNode;
    private static class TreapNode<T extends Comparable>{
        T element;
        TreapNode<T> left;
        TreapNode<T> right;
        int priority;
        private static Random randomObj = new Random();
        TreapNode(T theElement){
            this(theElement,null,null);
        }
        TreapNode(T theElement, TreapNode<T> lt, TreapNode<T> rt){
            element = theElement;
            left = lt;
            right = rt;
            priority = randomObj.nextInt();
        }
    }
    private  TreapNode<T> insert(T x, TreapNode<T> t){
        if(t == nullNode){
            return new TreapNode<T>(x,null,null);
        }
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0){
            t.left = insert(x,t.left);
            if(t.left.priority < t.priority){
                t = rotateWithLeftChild(t);
            }
        }  else if(compareResult > 0){
            t.right = insert(x,t.right);
            if(t.right.priority < t.priority){
                t = rotateWithRightChid(t);
            }
        }
        return t;
    }

    private TreapNode<T> remove(T x,TreapNode<T> t){
        if(t != nullNode){
            int compareResult = x.compareTo(t.element);
            if(compareResult < 0){
                t.left = remove(x ,t.left);
            } else if (compareResult > 0){
                t.right = remove(x,t.right);
            } else {
                if(t.left.priority < t.right.priority){
                    t = rotateWithLeftChild(t);
                } else {
                    t = rotateWithRightChid(t);
                }
                if(t != nullNode){
                    t = remove(x,t);
                } else {
                    t.left = nullNode;
                }
            }
        }
        return t;
    }

    /**
     * 右旋转--顺时针
     * @param tree
     * @return
     */
    private TreapNode<T> rotateWithLeftChild(TreapNode<T> tree){
//        RedBlackNode<T> k1 = tree.left;
//
//        tree.left = k1.right;
//        k1.right = tree;
//
//        return k1;
        TreapNode<T> leftTree = tree.left;
        tree.left = leftTree.right;
        leftTree.right = tree;
        return leftTree;
    }

    /**
     * 左旋转--逆时针
     * @param tree
     * @return
     */
    private TreapNode<T> rotateWithRightChid(TreapNode<T> tree){
//        RedBlackNode<T> k2 = tree.right;
//
//        tree.right = k2.left;
//        k2.left = tree;
//
//        return k2;
        //把父节点右子树取出来，这课右子树是旋转后的新根
        TreapNode<T> rightTree = tree.right;
        //父节点的右子树等于，新根的左子树
        tree.right = rightTree.left;
        //把父节点关联到新根的左子树
        rightTree.left = tree;
        return rightTree;
    }

    public static void main(String[] args) {
        TreapTree<Integer> tree = new TreapTree<Integer>();
        Integer[] arr = {10,85,15,70,20,60,30,50,8,9};
        for(int i = 0;i < arr.length;++i) {
//            tree.insert(arr[i]);

        }
    }
}
