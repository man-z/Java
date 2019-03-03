package test;

/**
 * 匹配堆
 */
public class PairingHeap<T extends Comparable> {

    private  PairNode<T> root;
    private int theSize;
    private PairNode<T>[] treeArray = new PairNode[5];

    public interface Position<T>{
        T getValue();
    }
    private static class PairNode<T> implements Position<T>{

        public T element;
        public PairNode<T> leftChild;
        public PairNode<T> nextSibling;
        public PairNode<T> prev;
        public PairNode(T theElement){
            element = theElement;
            leftChild = nextSibling = prev = null;
        }
        public T getValue() {
            return element;
        }
    }

    public Position<T> insert(T x){
        PairNode<T> newNode = new PairNode<T>(x);
        if(root == null){
            root =  newNode;
        } else {
            root = compareAndLink(root,newNode);
        }
        theSize++;
        return newNode;
    }

    /**
     *
     * @param first root树
     * @param second 新插入节点
     * @return
     */
    private PairNode<T> compareAndLink(PairNode<T> first, PairNode<T> second) {
        if(second == null){
            return first;
        }
        //大于树根
        if(second.element.compareTo(first.element) < 0){
            //关联匹配链
            //新根为second
            second.prev = first.prev;
            first.prev = second;
            first.nextSibling = second.leftChild;
            if(first.nextSibling != null){
                first.nextSibling.prev = first;
            }
            second.leftChild = first;
            return second;

        } else {
            //
            second.prev = first;
            first.nextSibling = second.leftChild;
            if(first.nextSibling != null){
                first.nextSibling.prev = first;
            }
            second.nextSibling = first.leftChild;
            if(second.nextSibling != null){
                second.nextSibling.prev = second;
            }
            first.leftChild = second;
            return first;
        }
    }
    public void decreaseKey(Position<T> pos, T newVal){
        PairNode<T> p = (PairNode<T>) pos;
        if(p == null || p.element == null || p.element.compareTo(newVal) < 0){
            throw new IllegalArgumentException();
        }
        p.element = newVal;
        if(p != root){
            if(p.nextSibling != null){
                p.nextSibling.prev = p.prev;
            }
            if(p.prev.leftChild == p){
                p.prev.nextSibling = p.nextSibling;
            } else {
                p.prev.nextSibling = p.nextSibling;
            }
            p.nextSibling = null;
            root = compareAndLink(root,p);
        }
    }

    public T deleteMin(){
        if(theSize == 0){
            throw new IllegalArgumentException();
        }
        T x = findMin();
        root.element = null;
        if(root.leftChild == null){
            root = null;
        } else {
            root = combineSiblings(root.leftChild);
        }
        theSize--;
        return x;
    }

    private PairNode<T> combineSiblings(PairNode<T> firstSibling) {
        if(firstSibling.nextSibling == null){
            return firstSibling;
        }
        int numSiblings = 0;
        for (;firstSibling != null;numSiblings++){
            treeArray = doubleIfFull(treeArray,numSiblings);
            treeArray[numSiblings] = firstSibling;
            firstSibling.prev.nextSibling = null;
            firstSibling = firstSibling.nextSibling;
        }
        treeArray = doubleIfFull(treeArray,numSiblings);
        treeArray[numSiblings] = null;
        int i = 0;
        for (;i + 1 < numSiblings;i+=2){
            treeArray[i] = compareAndLink(treeArray[i],treeArray[i+2]);
        }
        int j = i - 2;
        if(j == numSiblings - 3){
            treeArray[j] = compareAndLink(treeArray[i],treeArray[j + 2]);
        }
        for (;j >= 2; j -= 2){
            treeArray[j - 2] = compareAndLink(treeArray[j - 2],treeArray[j]);
        }
        return treeArray[0];
    }

    private PairNode<T>[] doubleIfFull(PairNode<T>[] array, int index) {
        if(index == array.length){
            PairNode<T>[] oldArray = array;
            array = new PairNode[index * 2];
            for (int i = 0; i < index; i++) {
                array[i] = oldArray[i];
            }
        }
        return array;
    }

    private T findMin() {
        return root.element;
    }

    public static void main(String[] args) {
        PairingHeap<Integer> tree = new PairingHeap<Integer>();
        Integer[] arr = {10,85,15,70,20,60,30,50,8,9};
        for(int i = 0;i < arr.length;++i) {
            tree.insert(arr[i]);
        }
        tree.printTree(tree.root,1);
        tree.decreaseKey(tree.root,1);
        System.out.println("==============================");
        tree.printTree(tree.root,1);
    }
    /**
     * 递归中序打印红黑树的信息
     */
    private void printTree(PairNode<T> t,int tabNum) {
        while (t != null){
            System.out.println(t.element);
            if(t.leftChild != null){
                t = t.leftChild;
            } else if(t.nextSibling != null){
                t = t.nextSibling;
            } else {
                break;
            }
        }
    }
}
