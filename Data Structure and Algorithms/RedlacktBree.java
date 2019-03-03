package test;

import com.snw.supplyChain.utils.CheckUserFilter;
import com.sun.org.apache.regexp.internal.RE;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 红黑树
 * @param <T>
 */
public class RedlacktBree<T extends Comparable> {
    private static final int BLACK = 1;
    private static final int RED = 0;
    //右子树保存红黑树真正的根，左子树为null节点（nullNode），
    private RedBlackNode<T> header;
    //指示一个null引用
    private RedBlackNode<T> nullNode;
    private RedBlackNode<T> current;//记录当前节点
    private RedBlackNode<T> parent;//记录父节点
    private RedBlackNode<T> grand;//记录爷爷节点
    private RedBlackNode<T> great;//记录爷爷的父节点

    public RedlacktBree(){
        //
        nullNode = new RedBlackNode(null);
        nullNode.left = nullNode.right = nullNode;
        header = new RedBlackNode(null);
        header.left = header.right = nullNode;
    }

    /**
     * 旋转，
     * @param item
     * @param parent
     * @return
     */
    private RedBlackNode<T> rotate1(T item, RedBlackNode<T> parent){
        //大于父节点
        if(compare(item,parent) < 0){
            return parent.left = compare(item,parent.left) < 0 ?
                    rotateWithLeftChild(parent.left):
                    rotateWithRightChild(parent.left);
        } else {
            return parent.right = compare(item,parent.right) < 0 ?
                    rotateWithLeftChild(parent.right):
                    rotateWithRightChild(parent.right);
        }
    }
    /**
     * 旋转，
     * @param item
     * @param parent
     * @return
     */
    private RedBlackNode<T> rotate(T item, RedBlackNode<T> parent){
        //great左下（↙），小于父节点
        if(compare(item, parent) < 0) {
            //grand左下(↙)，小于父节点的左子树
            if(compare(item, parent.left) < 0) {
                //father右下(↘)，大于父节点的左子树的左子树，那么就会形成一个之字形
                if(compare(item, parent.left.left) > 0){
                    //路径为右 + 左一字形。情况2，将其转换为情况1，然后复用情况1的代码，完成双旋转
                    parent.left.left = rotateWithRightChild(parent.left.left);
                }
                //father左下(↘)，路径为左 + 左一字形。情况1.
                parent.left = rotateWithLeftChild(parent.left);
            }
            //grand右下（↘）
            else {
                //father左下（↙）
                if(compare(item, parent.left.right) < 0) {
                    //路径为左 + 右之字形.情况3，将其转换为情况4
                    parent.left.right = rotateWithLeftChild(parent.left.right);
                }
                //father右下（↘）//路径为左 + 右一字形，情况4
                parent.left = rotateWithRightChild(parent.left);
            }
            //实际上都是返回了great的左儿子，也就是旋转之后最上层的节点
            return parent.left;
        }
        //great右下（↘）.情况完全跟上面镜像。
        else {
            //grand左下(↙)
            if(compare(item, parent.right) < 0) {
                //father左下(↙)
                if(compare(item, parent.right.left) > 0){
                    //路径为右 + 左之字形。情况2.将其转换为情况1
                    parent.right.left = rotateWithRightChild(parent.right.left);
                }
                //路径为右 + 左一字形。情况1，
                parent.right = rotateWithLeftChild(parent.right);
            }
            //grand右下（↘）
            else {
                //father左下（↙）
                if(compare(item, parent.right.right) < 0) {
                    //路径为右 + 右之字形.情况3，将其转换为情况4
                    parent.right.right = rotateWithLeftChild(parent.right.right);
                }
                //father右下（↘）
                //路径为右 + 右一字形，情况4
                parent.right = rotateWithRightChild(parent.right);
            }
            //实际上都是返回了great的右儿子，也就是旋转之后最上层的节点
            return parent.right;
        }
    }

    /**
     * 右旋转--顺时针
     * @param tree
     * @return
     */
    private RedBlackNode<T> rotateWithLeftChild(RedBlackNode<T> tree){
//        RedBlackNode<T> k1 = tree.left;
//
//        tree.left = k1.right;
//        k1.right = tree;
//
//        return k1;
        RedBlackNode<T> leftTree = tree.left;
        tree.left = leftTree.right;
        leftTree.right = tree;
        return leftTree;
    }

    /**
     * 左旋转--逆时针
     * @param tree
     * @return
     */
    private RedBlackNode<T> rotateWithRightChild(RedBlackNode<T> tree){
//        RedBlackNode<T> k2 = tree.right;
//
//        tree.right = k2.left;
//        k2.left = tree;
//
//        return k2;
        //把父节点右子树取出来，这课右子树是旋转后的新根
        RedBlackNode<T> rightTree = tree.right;
        //父节点的右子树等于，新根的左子树
        tree.right = rightTree.left;
        //把父节点关联到新根的左子树
        rightTree.left = tree;
        return rightTree;
    }

    /**
     * 自顶向下插入
     * @param item
     */
    public void insert(T item){
        //header的初始化方式，这么看来，header的左右子树默认都等于nullNode
        //header = new RedBlackNode(null);
        //header.left = header.right = nullNode;
        current = parent = grand = header;
        //nullNode的初始化方式：
        //nullNode = new RedBlackNode(null);
        //nullNode.left = nullNode.right = nullNode;
        nullNode.element = item;
        //找到插入的地方,最开始是header的右子树，红黑树的根，所以所有的节点都在header的右子树上
        while (compare(item,current) != 0){
            //这里是记录父节点，祖父节点，以方便后面判断时进行旋转--自顶向下寻找
            great = grand;
            grand = parent;
            parent = current;
            //找到当前节点合适的插入位置
//            if(compare(item, current) < 0)
//                current = current.left;
//            else
//                current = current.right;
            current = compare(item,current) < 0 ?
                    current.left:current.right;
            //这里的当前节点实际是新插入的节点的父节点，所以判断父节点的两个儿子节点是否都是红色的，然后进行一番调整
            //这里的current（当前节点）是新插入节点的父节点
            if(current.left.color == RED && current.right.color == RED){
                handleReorient(item);
            }
        }
        //这里为间歇新的等于设置header的左子树无穷大，
        // 如果不置空，那么凡是没有左子树或者右子树的节点务必会是其上次插入的旧值
        if(current != nullNode){
            return;
        }
        nullNode.element = null;
        current = new RedBlackNode<T>(item,nullNode,nullNode);
        if(compare(item,parent) > 0){
            parent.right = current;
        } else {
            parent.left = current;
        }
         handleReorient(item);
    }

    /**
     * case A-2下旋转后的重新上色处理。根据P，T，R（redredNodeWithBrother）形成的路径形状判断。
     * 通过观察可知，当路径为之字形时，只用改变X和P即可
     * 当路径为一字形，则需要翻转全部节点的颜色（P，T，X，R）
     *
     * @param father
     * @param brother
     * @param redNodeWithBrother
     * @param current
     */
    private void recolor(RedBlackNode<T> father, RedBlackNode<T> brother, RedBlackNode<T> redNodeWithBrother, RedBlackNode<T> current) {
        //判断路径的形状，这里可以使用异或判断左右关系是否相同
        current.color = RED;
        if((compare(brother.element, father) < 0) ^ (compare(redNodeWithBrother.element, brother) < 0)) {
            //之字形
            father.color = BLACK;
        }
        else {
            //一字形
            brother.color = RED;
            father.color = BLACK;
            redNodeWithBrother.color = BLACK;
        }
    }
    private void judgeAndFix(T x) {
        //确保当前节点是红色的
        if(current.color == RED){
            return;
        }
        //确保当前节点父节点也是红色的
        if(parent.color == BLACK){
            //由于红黑树不能连续两个几点是红色的，所以确保子节点是黑色，拿当前节点的兄弟节点
            RedBlackNode<T> node = current.element.compareTo(parent.left.element) < 0 ? parent.left : parent.right;
            node.color = BLACK;
            parent.color = RED;
            if(compare(x, grand) < 0) {
                if(compare(x, parent) > 0) {
                    grand.left = rotateWithLeftChild(grand.left);
                }
                else {
                    grand.left = rotateWithRightChild(grand.left);
                }
                //注意这里正确地重新设置好grand的值。
                grand = grand.left;
            }
            else {
                if(compare(x, parent) > 0) {
                    grand.right = rotateWithLeftChild(grand.right);
                }
                else {
                    grand.right = rotateWithRightChild(grand.right);
                }
                grand.right = grand;
            }
        }
        //父节点是红色的情况,如果当前删除节点的两个儿子是黑色的，null节点默认是黑色
        if(current.left.color == BLACK && current.right.color == BLACK){
            //如果当前要删除的节点的兄弟节点也有两个儿子
            RedBlackNode<T> node = compare(x, parent) < 0 ? parent.right : parent.left;
            //情况1，只需要翻转父子之间的颜色即可
            if(node.left.color == BLACK && node.right.color == BLACK){
                parent.color = BLACK;
                current.color = RED;
                node.color = RED;
            } else {
                //要删除的节点的兄弟节点的儿子其中一个是红色的
                //旋转的情况：左子树的之字形和一字型以及右子树的之字形和一字型旋转
                RedBlackNode<T> redNodeWithBrother = node.left.color == RED ? node.left : node.right;
                grand = rotate(redNodeWithBrother.element, grand);
                recolor(parent, node, redNodeWithBrother,current);
            }

        } else {
            return;
        }
    }


    //确保每次删除的节点都是红色节点
    public T delete(T item){
        header.right.color = RED;
        delete(header.right, header, header, item);
        header.right.color = BLACK;
        return null;
    }
    private void delete(RedBlackNode<T> root, RedBlackNode<T> rootFather, RedBlackNode<T> rootGrand, T x) {
        //使用类变量来传递不同函数之间的参数，避免混乱
        current = root;
        parent = rootFather;
        //bug 3 将该函数第三个实参名字设置为了grand，遮挡了类成员变量
        //grand = grand
        grand = rootGrand;

		/*
		//bug 1 应该在上层的delete来干这件事，因为该delete可能会用到递归两次。
		//将当前树的根节点涂成红色
		//root.color = RED;
		*/
        //开始向下迭代,直到迭代到nullNode。
        //递归删除
        while (current.element != null){
            nullNode.color = BLACK;
            judgeAndFix(x);
            //找到当前节点合适的插入位置
            int compareResult = compare(x, current);
            //找到删除的位置,
            if(compareResult == 0){
                if(current.left.element != null && current.right.element != null){
                    //用右子树的最小儿子替换删除的节点
                    current.element = findMin(current.right).element;
                    //递归删除
                    delete(current.right,current,parent,current.element);
                }//有一个儿子或者没有儿子
                else {
                    //叶子节点，没有儿子直接可以删除
                    if(current.left.element == null && current.right.element == null){
                        //删除的节点小于父节点
                        if(compare(current.element, parent) < 0){
                            parent.left = nullNode;
                        } else {//大于父节点
                            parent.right = nullNode;
                        }
                    } else {//有一个儿子
                        RedBlackNode<T> temp = current.left.element != null ? current.left : current.right;
                        current.element = temp.element;
                        current.left = current.right = nullNode;
                    }
                    break;
                }
            }
            //用于追溯路径
            grand = parent;
            parent = current;
            //向下迭代一次,判断x和当前节点,小于则向左，大于向右，等于不动作。
            current = compare(x,current) < 0 ?
                    current.left:current.right;
//            if(current.left.color == RED && current.right.color == RED){
//                handleReorient(current.element);
//            }
        }
        //bug 1
        //将当前子树的根节点重新上色为黑色。
        //root.color = BLACK;
        //return subTreeRoot;
    }
    /**
     *触发条件：
     * 1、在寻找插入的合适位置时，发现有几率成为新节点的父节点的左右子树都是红色，则进行旋转，确保叔叔节点不会是红色（父节点的兄弟节点）
     * 2、新值插入完后，如果发现某个节点儿子都是红色那么就需要进行调整，
     * @param item 当前新插入的节点
     */
    private void handleReorient(T item){
        //颜色转换，把当前插入的节点设置为红色，其两个儿子为黑色， 因为红黑树中，红色节点的子节点必须是黑色（不能连续两个节点相连）
        current.color = RED;
        current.left.color = BLACK;
        current.right.color = BLACK;
        //如果当前插入的节点的父节点也是红色，那么要做相应的处理，因为破坏了上面说的红黑树性质
        if(parent.color == RED){
            //把爷爷设置为红色节点
            grand.color = RED;
            //处理一字型或者之字形旋转start
//            //判断当前插入的节点位置是否合适，否则旋转
            if((compare(item,grand) < 0) !=
                    (compare(item,parent) < 0)){
                parent = rotate1(item,grand);
            }
            //把爷爷节点进行一次旋转
            current = rotate1(item,great);
            //处理一字型或者之字形旋转end
            //把当前节点设置为黑色节点
            current.color = BLACK;
        }
        header.right.color = BLACK;
    }

    private final int compare(T item, RedBlackNode<T> t){
        if(t == header){
            return 1;
        } else {
            return item.compareTo(t.element);
        }
    }

    class RedBlackNode<T extends  Comparable>{
        T element;
        RedBlackNode<T> left;
        RedBlackNode<T> right;
        int color;
        RedBlackNode(T theElement){
            this(theElement,null,null);
        }
        RedBlackNode(T theElement, RedBlackNode<T> lt, RedBlackNode<T> rt){
            element = theElement;
            left = lt;
            right = rt;
            color = RedlacktBree.BLACK;
        }
        RedBlackNode(T theElement, RedBlackNode<T> lt, RedBlackNode<T> rt, int color){
            element = theElement;
            left = lt;
            right = rt;
            this.color = color;
        }
    }
    /*
     * 前序遍历"伸展树"
     */
    private void preOrder(RedBlackNode<T> tree) {
        if(tree != null) {
            if(tree.color == RED){
                System.err.print(tree.element+" ");
            } else {
                System.out.print(tree.element+" ");
            }
            preOrder(tree.left);
            preOrder(tree.right);
        }
    }
    /**
     * 打印红黑树
     */
    public void printTree() {
        if(isEmpty()) {
            System.out.println("impty tree");
        }
        else {
            System.out.println("------------------");
            printTree(header.right, 1);
        }
    }
    private RedBlackNode<T> findMin(RedBlackNode<T> t) {
        //一直迭代直到到null节点。它就是该树的最小值。
        while(t.left.element != null) {
            t = t.left;
        }

        return t;
    }

    /**
     * 递归中序打印红黑树的信息
     */
    private void printTree(RedBlackNode<T> t,int tabNum) {
        if(t.element != null) {
            printTree(t.right, tabNum + 1);
            for(int i = 0;i < tabNum;++i){
                System.out.print("\t");
            }
            System.out.println(t.element + (t.color == 1? "(B)":"(R)"));
            printTree(t.left, tabNum + 1);
        }

    }
    /**
     * 判断红黑树是否为空
     * @return
     */
    private boolean isEmpty() {
        return header.right.equals(nullNode);
    }


    public static void main(String[] args) {
        Random r = new Random(100);
        RedlacktBree<Integer> tree = new RedlacktBree<Integer>();
        Integer[] arr = {10,85,15,70,20,60,30,50,8,9};
        for(int i = 0;i < arr.length;++i) {
            tree.insert(arr[i]);

        }
//        for (int i = 0; i<100; i++){
//            int num = r.nextInt(100);
//            tree.insert(num);
//        }
        tree.printTree();
        tree.delete(20);
        tree.printTree();
//        tree.preOrder(tree.header.right);
    }
}
