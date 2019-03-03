package test;

import java.util.Random;
import java.util.Stack;

public class RedBlackTree<T extends Comparable> {
    public static void main(String[] args) {
        RedBlackTree<Integer> t = new RedBlackTree<Integer>();
        Random r = new Random(100);
        Integer[] arr = {10,85,15,70,20,60,30,50,8,9};
        //Integer[] arr = {10,15,20};
        for(int i = 0;i < arr.length;++i) {
            t.insert(arr[i]);

        }
//        for(int i = 0;i < 100;++i) {
//            int num = r.nextInt(100);
//            t.insert(num);
////            t.insert(arr[i]);
//
//        }
        t.printTree();
        System.out.println("----------------");
        t.delete(20);
        t.printTree();
    }

    public static final int BLACK = 1;
    public static final int RED = 0;

    private Node<T> header;
    private Node<T> nullNode;


    /**
     * 红黑树的节点类对象
     * 相比于普通的二叉搜索树，只是多了一个色域，非黑既红。
     * @author 25040
     *
     * @param <T>
     */
    private static class Node<T>{
        private T element;
        private Node<T> left;
        private Node<T> right;
        private int color;

        public Node(T element, Node<T> left, Node<T> right, int color) {
            super();
            this.element = element;
            this.left = left;
            this.right = right;
            this.color = color;
        }
    }

    /**
     * 构造红黑树
     */
    public RedBlackTree() {
        this.nullNode = new Node<T>(null,null,null,BLACK);
        this.nullNode.left = this.nullNode.right = nullNode;//现在看来这里的这个设计实在是太妙了。
        this.header = new Node<T>(null,null,null,BLACK);
        this.header.left = this.header.right = nullNode;
    }




    private Node<T> current;
    private Node<T> father;
    private Node<T> grand;
    private Node<T> great;

    /**
     * nullNode节点模拟null，每次插入开始，nullNode的element置为x
     * header节点，为了根节点发生旋转后返回和编程上的方便性。并且定义他的element值为负无穷大。
     * 所以，整棵红黑树都挂在header的右边。compare函数，定义了当比较节点为header，永远返回1。
     *
     * 自顶向下的插入例程。
     * 其核心思想和步骤是：
     * 1. 使用上面四个私有类成员来记录向下搜索迭代的路径。
     *    分别记录了当前节点，父节点，祖父节点和曾祖父节点。一开始四个变量都在header上，
     * 2. 从header节点递归向下，通过compare判断路径。同时记录父链路径。最后每次循环判断当前节点的两个儿子是否为红色。是则处理下。
     * 3. 循环直到compare返回0，也就是达到null节点或者在红黑树种已经找到了它，判断是否是重复节点，如果是，则执行步骤4，否则返回。
     * 4. 生成新节点，插入到合适的位置，然后最后进行一次handleReorient函数，常数时间。
     * @param x
     */
    public void insert(T x) {
        current = father = grand = header;
        nullNode.element = x;//用来判断是否迭代到了叶节点。

        //向下迭代，发现如果有节点具有两个红色儿子，则执行一些操作。
        while(compare(x, current) != 0) {
            //保存迭代向下的路径
            great = grand;
            grand = father;
            father = current;
            //找到合适的向下方向。由于一开始在header上，所以这样，由于compare(x,header)=1,所以整棵红黑树都在header的右儿子
            if(compare(x, current) < 0)
                current = current.left;
            else
                current = current.right;

            //检查当前节点两个儿子是否都是红色的。
            if(current.left.color == RED && current.right.color == RED) {
                handleReorient(x);
            }
        }
        nullNode.element = null;
        //判断是否迭代到了null节点。
        if(current.element != null) {
            return;
        }
        //生成新节点并且插入到指定位置。
        current = new Node<T>(x, nullNode, nullNode,RED);
        if(compare(x, father) < 0) {
            father.left = current;
        }
        else {
            father.right = current;
        }
        handleReorient(x);

    }

    /**
     * 当向下迭代的过程中，如果发现某个儿子都是红色，我们就需要进行一波操作
     *
     * 具体步骤为：
     * 1. 将current节点的两个儿子置为黑色，current置为红色，这样的操作并不会改变红黑树的性质4，
     *     但是当current的父节点也就是father也是红色的，就会破坏性质3。
     *     所以，当father为红色时，执行步骤2.否则退出函数。
     *
     * 2. 当father为红色时，破坏了红黑性，需要修复。也就是进行适当的旋转和重新上色。分为以下几种情况
     *    PS:x为current，P为father, G为grand, E为great.
     *    当x,p,g为左一字形。simple left rotate(g),然后将其G置为红色，P置为黑色
     *    当x,p,g为左之字形（即路径形状为先左后右），double left rotate(g)然后g置为红色，x置为黑色。然后x引用改为P.children;
     *    当x,p,g为右之字形（即路径形状为先右后左），double right rotate(g)然后g置为红色，x置为黑色，然后x引用改为P.children.
     *    当x,p,g为右一字形，simple left rotate(g)然后g置为红色，P置为黑色。
     * @param x
     */
    private void handleReorient(T x) {
        //步骤1
        current.left.color = current.right.color = BLACK;
        current.color = RED;

        //判断
        if(father.color == RED) {
            //步骤2
            grand.color = RED;
            current = rotate(x, great);
            current.color = BLACK;
        }
        //将根节点置为黑色
        header.right.color = BLACK;

    }

    /**
     * @version 1
     * 根据great,grand,father和great生成的路径来判断怎么样来做那种旋转方式。
     * 由于x,p,g变换后的子树必须重新和great正确连接。great又有两个儿子，因此，共计8种情况。
     *
     * 所有路径均从great开始。注意到所有的双旋转都是由单旋转构成的。
     * 我的天，这太可怕了，虽然代码意义很明确，但是在这么多的条件下，确实很容易写错。
     *
     * @version 2
     * 精简可以合并的代码
     *
     *
     * @param x
     * @return
     */
    private Node<T> rotate(T x, Node<T> parent) {
        //great左下（↙），小于父节点
        if(compare(x, parent) < 0) {
            //grand左下(↙)，小于父节点的左子树
            if(compare(x, parent.left) < 0) {
                //father右下(↘)，大于父节点的左子树的左子树，那么就会形成一个之字形
                if(compare(x, parent.left.left) > 0){
                    //路径为右 + 左一字形。情况2，将其转换为情况1，然后复用情况1的代码，完成双旋转
                    parent.left.left = simpleRotateWithRightChild(parent.left.left);
                }
                //father左下(↘)，路径为左 + 左一字形。情况1.
                parent.left = simpleRotateWithLeftChild(parent.left);
            }
            //grand右下（↘）
            else {
                //father左下（↙）
                if(compare(x, parent.left.right) < 0) {
                    //路径为左 + 右之字形.情况3，将其转换为情况4
                    parent.left.right = simpleRotateWithLeftChild(parent.left.right);
                }
                //father右下（↘）//路径为左 + 右一字形，情况4
                parent.left = simpleRotateWithRightChild(parent.left);
            }
            //实际上都是返回了great的左儿子，也就是旋转之后最上层的节点
            return parent.left;
        }
        //great右下（↘）.情况完全跟上面镜像。
        else {
            //grand左下(↙)
            if(compare(x, parent.right) < 0) {
                //father左下(↙)
                if(compare(x, parent.right.left) > 0){
                    //路径为右 + 左之字形。情况2.将其转换为情况1
                    parent.right.left = simpleRotateWithRightChild(parent.right.left);
                }
                //路径为右 + 左一字形。情况1，
                parent.right = simpleRotateWithLeftChild(parent.right);
            }
            //grand右下（↘）
            else {
                //father左下（↙）
                if(compare(x, parent.right.right) < 0) {
                    //路径为右 + 右之字形.情况3，将其转换为情况4
                    parent.right.right = simpleRotateWithLeftChild(parent.right.right);
                }
                //father右下（↘）
                //路径为右 + 右一字形，情况4
                parent.right = simpleRotateWithRightChild(parent.right);
            }
            //实际上都是返回了great的右儿子，也就是旋转之后最上层的节点
            return parent.right;
        }
    }


    /**
     * 节点的右单旋转。
     * @return
     */
    private Node<T> simpleRotateWithRightChild(Node<T> k1) {
        Node<T> k2 = k1.right;

        k1.right = k2.left;
        k2.left = k1;

        return k2;
    }

    /**
     * 节点左单旋转。
     * @return
     */
    private Node<T> simpleRotateWithLeftChild(Node<T> k2) {
        Node<T> k1 = k2.left;

        k2.left = k1.right;
        k1.right = k2;

        return k1;
    }

    /**
     * 比较x和节点的值大小，如果t为根节点，那么x永远比较大。
     * @param x
     * @param t
     * @return
     */
    private final int compare(T x, Node<T> t) {
        if(t == header) {
            return 1;
        }
        else {
            return x.compareTo(t.element);
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

    /**
     * 递归中序打印红黑树的信息
     */
    private void printTree(Node<T> t,int tabNum) {
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

    /**
     * 自底向上插入
     * @param x
     */
    public void insertMIT(T x) {
        //使用堆栈来记录路径
        Stack<Node<T>> s = new Stack<Node<T>>();
        //使得可以用用compare = 0来判断循环终止的条件。
        nullNode.element = x;
        //引用当前正在迭代的节点
        Node<T> current = header;

        /**
         * 这里相当于实现了二叉树的非递归插入例程，即伪代码的Tree-insert
         * 找到当前插入的位置
         */
        while(!(compare(x, current) == 0)) {
            //压入堆栈，记录向下路径，header节点也被压入了堆栈
            s.push(current);
            //根据数的性质，判断左右
            current = compare(x, current) < 0 ? current.left : current.right;
        }
        nullNode.element = null;
        //判断是不是到达了null节点，还是中间就找到了等值的节点
        if(current.element != null) {
            //找到了等值节点，直接返回
            return;
        }
        /*
         * 伪代码对应的color[x] = red;
         */
        //创建新的节点，生成新节点，上色为红色，并且插入正确的父节点位置
        //bug 3 不能这样current = new Node<>(x, null, null, RED);
        current = new Node<T>(x, nullNode, nullNode, RED);
        Node<T> father = s.peek();//错误1，改之前是pop，破坏了father路径。
        if(compare(x, father) < 0) {
            father.left = current;
        }
        else {
            father.right = current;
        }

        /*
         * 自底向上的fix例程，其实我们关注地就是在如何不破坏性质4的情况下，向上修复性质3
         */
        //bug 2 ：当红黑树中的节点过少的时候，完全没必要fix，直接插入红节点，置黑根节点即可。
        //即红黑树插入第一个元素和第二个元素时根本没必要fix,注意到堆栈中还有header节点。
        //性质4：从节点到一个null引用的每一条路径必须包含相同数目的黑色节点
        //性质3：一个红色节点的子节点必须黑色的
        if(s.size() >= 3) {
            downToUpFix(current, s, x);
        }
        else {
            header.right.color = BLACK;
        }


    }

    /**
     * 自底向上的fix例程，其实我们关注地就是在如何不破坏性质4的情况下，向上修复性质3
     * 共计分为六种情况。左半边三种和右半边三种镜像对称。
     *
     * @param current 新插入节点的引用
     * @param s       回溯路径的堆栈
     * @param x       新插入节点的具体值
     */
    private void downToUpFix(Node<T> current,Stack<Node<T>> s,T x) {
        //判断是否向上到达了根节点或者当前节点是否继续红色
        boolean isEnd = (s.peek().color == RED);
        //bug 7 不应该无条件进入一次，应该检查当前插入的红色节点是否直接满足了红黑性，不用做任何变化
        //即只有插入节点的父亲也是红色的时候才需要进行变换
        while(isEnd) {
            Node<T> father = s.pop();
            Node<T> grand = s.pop();
            //x == left[p[p[x]]],这是情况为左半边的三种情况，右半边的三种情况
            //判断当前节点是否是爷爷节点的左子树中
            if(compare(x, grand) < 0) {
                Node<T> uncle = grand.right;
                //判断你的叔叔节点是否是红色
                if(uncle.color == RED) {
                    //case 1 重上色
                    father.color = BLACK;
                    uncle.color = BLACK;
                    grand.color = RED;
                    //continue向上修复
                    current = grand;
                    //去掉continue;
                }
                else {
                    //判断当前节点是否是父节点的右儿子
                    if(compare(x, father) > 0) {
                        //case 2 旋转，将现场情况变为情况3,注意这里旋转后，破坏了grand，father和current的正确路径关系
                        //bug 4 应该是它的左儿子或者右儿子。grand = simpleRotateWithLeftChild(father);
                        grand.left = simpleRotateWithRightChild(father);
                    }
                    //case 3 旋转并且重新上色, 并且正确连接到曾祖父的左节点或者右节点
                    grand.color = RED;//通过观察，我们发现爷爷节点总是要变成红色的。
                    Node<T> great = s.peek();//不要从堆栈中取出来

                    if(compare(x, great) < 0) {
                        great.left = simpleRotateWithLeftChild(grand);
                        great.left.color = BLACK;
                        current = great.left;
                    }
                    else {
                        great.right = simpleRotateWithLeftChild(grand);
                        great.right.color = BLACK;
                        //bug 6 还是得把current放在正确得位置上，虽然它是最后一步
                        current = great.right;
                    }
                }
            }
            //右半边3种情况，算法思路都一样，只需要左右互换。考查小心细致的时候到了
            else {
                Node<T> uncle = grand.left;
                //判断当前节点的叔叔节点是否为红色。
                if(uncle.color == RED) {
                    //case 4 重上色后继续向上
                    father.color = BLACK;
                    uncle.color = BLACK;
                    grand.color = RED;

                    //continue向上修复
                    current = grand;
                    //去掉continue
                }
                else {
                    //判断当前节点是否是父节点
                    if(compare(x, father) < 0) {
                        //case 5 旋转变成情况6.注意这里旋转后，破坏了father和current的正确路径关系
                        //bug 4 应该是它的左儿子或者右儿子。grand = simpleRotateWithLeftChild(father);
                        grand.right = simpleRotateWithLeftChild(father);
                    }
                    //case 6 旋转，重上色。然后把旋转后的子树链接到曾祖父节点合适的位置上。
                    grand.color = RED;
                    Node<T> great = s.peek();

                    if(compare(x, great) < 0) {
                        great.left = simpleRotateWithRightChild(grand);
                        great.left.color = BLACK;
                        current = great.left;
                    }else {
                        //bug 5 智障了，写成了simpleRotateWithLeftChild
                        great.right = simpleRotateWithRightChild(grand);
                        great.right.color = BLACK;
                        current = great.right;
                    }
                }
            }
            //判断现在的树满足红黑性
            isEnd =  !( current.element.equals(header.right.element)) ;
            if(isEnd) {
                if(current.color == RED) {
                    if(s.peek().color == BLACK){
                        isEnd = false;
                    }
                }
                else {
                    isEnd = false;
                }
            }
        }
        header.right.color = BLACK;
    }

    public void delete(T x) {
        header.right.color = RED;
        delete(header.right, header, header, x);
        header.right.color = BLACK;
    }
    private void delete(Node<T> root, Node<T> rootFather, Node<T> rootGrand, T x) {
        //使用类变量来传递不同函数之间的参数，避免混乱
        current = root;
        father = rootFather;
        //bug 3 将该函数第三个实参名字设置为了grand，遮挡了类成员变量
        //grand = grand
        grand = rootGrand;
		/*
		//bug 1 应该在上层的delete来干这件事，因为该delete可能会用到递归两次。
		//将当前树的根节点涂成红色
		//root.color = RED;
		*/
        //开始向下迭代,直到迭代到nullNode。
        while(current.element != null) {
            //
            //fix将x变为红色
            judgeAndFix(x);
            //因为，上面的变色操作，可能把null节点也变成了红色。后果未知，为了防止出现意外，强制变为黑色。
            nullNode.color = BLACK;

            //
            int compareResult = compare(x, current);
            if(compareResult == 0) {
                //如果是双儿子的情况
                if(current.left.element != null && current.right.element != null) {
                    //双儿子的情况下，先用当前节点的右子树的最大值来替换
                    current.element = findMin(current.right).element;

                    //bug 2 当红黑树只有三个元素的时候，会出错。修正，把delete的传参形式改下。
                    //追加了grand参数，这样就算特殊情况需要rotate的时候，不会出错。同时也不再需要返回值。
                    //current.right = delete(current.right, current, current.element)
                    delete(current.right, current, father, current.element);

                    //注意到，这里递归delete之后，current啥的都是完全混乱的，不能用，但是因为delete例程最多只会执行两轮，所以不受影响。
                    //这里完成后，直接break跳出了
                }
                //单儿子或者没儿子的情况,不能把单儿子和没儿子两种情况放在一起
                else {
                    //树叶，没儿子，直接干掉
                    if(current.left.element == null && current.right.element == null) {
                        if(compare(current.element, father) < 0) {
                            father.left = nullNode;
                        }
                        else {
                            father.right = nullNode;
                        }
                    }
                    //画图理解
                    //单儿子，这里注意到，单儿子的情况下，删除节点最多只有一个儿子。
                    //而且这种情况下，欲删除节点必然是黑色的，它的左儿子或者右儿子必然是红色的。
                    //直接拿左儿子或者右儿子的值来替换删除节点的值，再删除右儿子或者右儿子。
                    else {
                        Node<T> temp = current.left.element != null ? current.left : current.right;
                        current.element = temp.element;
                        current.left = current.right = nullNode;
                    }
                }
                //删除后直接退出
                break;
            }

            //用于追溯路径
            grand = father;
            father = current;


            //向下迭代一次,判断x和当前节点,小于则向左，大于向右，等于不动作。
            if(compareResult < 0) {
                current = current.left;
            }
            else {
                current = current.right;
            }

        }
        //bug 1
        //将当前子树的根节点重新上色为黑色。
        //root.color = BLACK;
        //return subTreeRoot;
    }
    private Node<T> findMin(Node<T> t) {
        //一直迭代直到到null节点。它就是该树的最小值。
        while(t.left.element != null) {
            t = t.left;
        }

        return t;
    }

    private void judgeAndFix(T x) {
        //情况B的幸运情况的特殊情况，不需要处理，下一轮。目的已达到，就此罢手
        if(current.color == RED) {
            return;
        }

        //情况B的不幸情况,只有此时，P节点才是黑色
        if(father.color == BLACK) {
            //先重上色
            Node<T> brother = compare(x, father) < 0 ? father.right : father.left;
            brother.color = BLACK;
            father.color = RED;
            //正确旋转并且重新连接,并且将current，father，grand之间重新理顺，确保正确。画图理解。
            if(compare(x, grand) < 0) {
                if(compare(x, father) > 0) {
                    grand.left = simpleRotateWithLeftChild(grand.left);
                }
                else {
                    grand.left = simpleRotateWithRightChild(grand.left);
                }
                //注意这里正确地重新设置好grand的值。
                grand = grand.left;
            }
            else {
                if(compare(x, father) > 0) {
                    grand.right = simpleRotateWithLeftChild(grand.right);
                }
                else {
                    grand.right = simpleRotateWithRightChild(grand.right);
                }
                grand.right = grand;
            }
        }

        //情况A和B的判断
        if(hasTowBlackChild(current)) {
            //情况A
            //获取当前节点的兄弟对象，判断它两个儿子的状态
            Node<T> brother = compare(x, father) < 0 ? father.right : father.left;
            if(hasTowBlackChild(brother)) {
                //如果T节点也有两个黑色的儿子，那么case A-1，重新上色即可
                father.color = BLACK;
                current.color = RED;
                brother.color = RED;
            }
            else {
                //如果T节点的儿子之一是红色的。case A-2.在这种情况下，又可以分为两种情况
                //如果P，T，R形成了一字形，只需要一次单旋转+重上色即可。
                //如果P,T,R形成了之字形，那么需要一次双旋转+重上色才行。
                //可以复用插入例程的rotate
                //获取brother节点中的红节点
                Node<T> redNodeWithBrother = brother.left.color == RED ? brother.left : brother.right;
                //由于是根据T节点的情况来进行
                grand = rotate(redNodeWithBrother.element, grand);
                //再根据R，T，P形成的路径来进行重新上色
                recolor(father, brother, redNodeWithBrother,current);

                //TEST
//                this.printTree();
            }
        }
        else {
            //情况B，先调到下一轮。
            return;
        }

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
    private void recolor(Node<T> father, Node<T> brother, Node<T> redNodeWithBrother, Node<T> current) {
        //判断路径的形状，这里可以使用异或判断左右关系是否相同
        if((compare(brother.element, father) < 0) ^ (compare(redNodeWithBrother.element, brother) < 0)) {
            //之字形
            current.color = RED;
            father.color = BLACK;
        }
        else {
            //一字形
            current.color = RED;
            brother.color = RED;
            father.color = BLACK;
            redNodeWithBrother.color = BLACK;
        }
    }

    /**
     * 判断当前节点是否有两个黑色的儿子。
     * 值得注意的是，这里的儿子也包括null节点，也就是说如果当前节点是树叶，那么一定会返回true
     * @return
     */
    private boolean hasTowBlackChild(Node<T> current) {
        return current.left.color == BLACK && current.right.color == BLACK;
    }

}
