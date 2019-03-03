package test;

import java.util.ArrayList;
import java.util.Arrays;

public class SortTest {
    int[] s = {-4,0,0,2,0,4,4,6,0,8,8,10,8,12,12,14};

    @org.junit.Test
    public  void find(){
        System.out.println(Arrays.toString(s));
        find(14);
        System.out.println(Arrays.toString(s));
    }
    public int find(int x){
        if(s[x] < 0)//s[x]为负数时,说明 x 为该子集合的代表(也即树根), 且s[x]的值表示树的高度
               return x;
        else
         return s[x] = find(s[x]);//使用了路径压缩,让查找路径上的所有顶点都指向了树根(代表节点)
                     //return find(s[x]); 没有使用 路径压缩
    }



    /*
     * 获取数组a中最大值
     *
     * 参数说明：
     *     a -- 数组
     *     n -- 数组长度
     */
    int get_max(int a[], int n)
    {
        int i, max;

        max = a[0];
        for (i = 1; i < n; i++)
            if (a[i] > max)
                max = a[i];
        return max;
    }

    /*
     * 对数组按照"某个位数"进行排序(桶排序)
     *
     * 参数说明：
     *     a -- 数组
     *     n -- 数组长度
     *     exp -- 指数。对数组a按照该指数进行排序。
     *
     * 例如，对于数组a={50, 3, 542, 745, 2014, 154, 63, 616}；
     *    (01) 当exp=1表示按照"个位"对数组a进行排序
     *    (02) 当exp=10表示按照"十位"对数组a进行排序
     *    (03) 当exp=100表示按照"百位"对数组a进行排序
     *    ...
     */
    void count_sort(int a[], int n, int exp, int[] output)
    {
        int i;
        int[] buckets = {0,0,0,0,0,0,0,0,0,0};

        // 将数据出现的次数存储在buckets[]中
        for (i = 0; i < n; i++)
            //分别取个十百等等，比如：123/1(等于123)%10(等于0.3)等于3.
            buckets[ (a[i]/exp)%10 ]++;

        // 更改buckets[i]。目的是让更改后的buckets[i]的值，是该数据在output[]中的位置。
        for (i = 1; i < 10; i++)
            //计算位数的数组所载位置，本身的下标加上前一个数组的下标等于自身真是的下标
            buckets[i] += buckets[i - 1];

        // 将数据存储到临时数组output[]中
        for (i = n - 1; i >= 0; i--)
        {
            //取出每一个位数的下标减1等于真实数组的所在位置
            output[buckets[ (a[i]/exp)%10 ] - 1] = a[i];
            buckets[ (a[i]/exp)%10 ]--;
        }

        // 将排序好的数据赋值给a[]
        for (i = 0; i < n; i++)
            a[i] = output[i];
    }

    /*
     * 基数排序
     *
     * 参数说明：
     *     a -- 数组
     *     n -- 数组长度
     */
    @org.junit.Test
    public void radix_sort()
    {
        int exp;    // 指数。当对数组按各位进行排序时，exp=1；按十位进行排序时，exp=10；...
        int a[] = new int[]{123,435,6,678,0,34,679,234,456};
        int n = a.length - 1;
        System.out.println(Arrays.toString(a));
        int max = get_max(a, n);    // 数组a中的最大值
        int output[] = {0,0,0,0,0,0,0,0,0,0};
        // 从个位开始，对数组a按"指数"进行排序
        for (exp = 1; max/exp > 0; exp *= 10) {
            count_sort(a, n, exp, output);
        }
        System.out.println(Arrays.toString(a));
    }

    /**
     * 基数排序
     */
    @org.junit.Test
    public void radixSort(){
        int[] a = {8,5,3,7,9,3};
        System.out.println(Arrays.toString(a));
        radixSort(a,a.length);
        System.out.println(Arrays.toString(a));
    }

    private void radixSort(int[] a, int length) {
        final int BUCKETS = 256;
        ArrayList<Integer>[] buckets = new ArrayList[BUCKETS];
        for (int i = 0 ; i < BUCKETS; i++) {
            buckets[i] = new ArrayList<Integer>();
        }
        for (int i = length; i >= 0 ; i--) {
            for (int s: a) {
                buckets[s].add(s);
            }
            int idx = 0;
            for ( ArrayList<Integer> thisBuckets : buckets) {
                 for (int s : thisBuckets){
                     a[idx] = s;
                 }
                 thisBuckets.clear();
            }
        }
    }

    /**
     * 快速排序
     */
    @org.junit.Test
    public void quickSort(){
        int arr[]={7,4,8,3,5,12,23,2,1,56,89,15};
        System.out.println(Arrays.toString(arr));
        quickSort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }

    private void quickSort(int[] arr, int left, int right) {
        if(left < right){
            int pivot = median3(arr,left,right);
            System.out.println("枢纽元的值："+pivot);
            //这里j是为了避免枢纽元的值在分割的数据段中，同时pivot获取也是为了把枢纽元的值远离分割数据段中
            int i = left,j = right - 1;
            for(;;){
                while (arr[++i] < pivot){}
                while (j > left && arr[--j] > pivot){}
                //说明本次循环左边与右边没有交错
                if(i < j){
                    swap(arr,i,j);
                    System.out.println("交换位置后的数组："+Arrays.toString(arr));
                } else {
                    break;
                }
            }
            if (i < right) {
                //这里实际是把枢纽元的值与本次结束循环前i找到大于枢纽元的值进行交换，（说明左边与右边已经交错了）
                //如果枢纽元的值放在前面，那么开始寻找的时候应该是从后面寻找，以至于交错时，右边总是能寻找到小于枢纽元的值，以便于进行交换
                swap(arr, i, right - 1);
                System.out.println("排序完后，把i为："+i+"的位置与right-1："+(right-1)+"的位置交换，此时的j为："+j+"，交换后的数组："+Arrays.toString(arr));
            }
//            swap(arr,i,right - 1);
            //对左右两边再次进行分割和排序，左边的数据段结束位置为i，因为循环结束前i总能找到大于枢纽元的值，所以这里是±i然后进行再次分割和排序
            quickSort(arr,left,i-1);
            quickSort(arr,i+1,right);
        }
    }

    private int median3(int[] arr, int left, int right) {
        int center = (left + right) / 2;
        if(arr[center] < arr[left]){
            swap(arr,left,center);
            System.out.println("枢纽元的值与左边交换后："+Arrays.toString(arr));
        }
        if(arr[right] < arr[left]){
            swap(arr,left,right);
            System.out.println("左边与右边交换后："+Arrays.toString(arr));
        }
        if(arr[right] < arr[center]){
            swap(arr,center,right);
            System.out.println("枢纽元与右边交换后："+Arrays.toString(arr));
        }
        swap(arr,center,right-1);
        System.out.println("把枢纽元放到数组倒数第二个位置："+Arrays.toString(arr));
        return arr[right-1];
    }
    private static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    /**
     * 归并排序
     */
    @org.junit.Test
    public void mergeSort(){
        int arr[]={7,4,8,3,5,12,23,2,1};
        System.out.println(Arrays.toString(arr));
        int[] tmpArr = new int[arr.length];
        myMergeSort(arr,tmpArr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
        Arrays.sort(arr);
    }

    /**
     *
     * @param a 原数组
     * @param tmpArr 临时数组，原数组最后的排序从该数组中copy
     * @param left 每个被拆分的数组左边的元素下标
     * @param right 每个被拆分的数组最后一个元素下标
     */
    public void myMergeSort(int[] a,int[] tmpArr,int left,int right){
        //表示还可以拆分，以至于拆分到，每个数组的第一个元素
        if(left < right){
            //左边加右边等于整个数组的长度，然后再对数组进行拆分
            int conten = (left + right) / 2;//计算出拆分后的右边元素最后一个位置,对于拆分后右边的数组(+1)是第一个元素的位置
            //继续拆左边数组，
            myMergeSort(a,tmpArr,left,conten);
            myMergeSort(a,tmpArr,conten +1,right);
            //由于是需要交替归并，所以要提供左右数组的起始和结束，因为归并会是：左右数组都会取、只获取左边、只获取右边
            myMerge(a,tmpArr,left,conten+1,right);
        }
    }

    /**
     *
     * @param a 原数组
     * @param tmpArr 临时数组
     * @param leftPos 取左边数组第一个元素的下标
     * @param rightPos 取右边数组第一个元素下标
     * @param rightEnd 右边元素最后一个下标
     */
    private void myMerge(int[] a, int[] tmpArr, int leftPos, int rightPos, int rightEnd) {
        //右边数组第一个位置减1等于左边数组最后一个位置
        int leftEnd = rightPos - 1;
        //临时数组的下标，根据拆分数组的第一个下标
        int tmpPos = leftPos;
        //计算本次排序需要copy的数组个数,数量是从第一个开始的，所以加1
        int numElements = rightEnd - leftPos + 1;
        while (leftPos <= leftEnd && rightPos <= rightEnd){
            if(a[leftPos] < a[rightPos]){
                tmpArr[tmpPos++] = a[leftPos++];
            } else {
                tmpArr[tmpPos++] = a[rightPos++];
            }
        }
        while (leftPos <= leftEnd){
                tmpArr[tmpPos++] = a[leftPos++];
        }
        while (rightPos <= rightEnd){
                tmpArr[tmpPos++] = a[rightPos++];
        }
        //把当前排序的结果从临时数组拷贝到原数组
        for (int i = 0; i < numElements; i++,rightEnd--) {
            a[rightEnd] = tmpArr[rightEnd];
        }
    }


    private void mergeSort(int[] a, int[] tmpArr, int left, int right) {
        if(left < right){
            int center = (left + right) / 2;
            mergeSort(a,tmpArr,left,center);
            mergeSort(a,tmpArr,center+1,right);
            merge(a,tmpArr,left,center+1,right);
        }
    }

    private void merge(int[] a, int[] tmpArr, int leftPos, int rightPos, int rightEnd) {
        int leftEnd = rightPos - 1;
        int tmpPos = leftPos;
        int numElements = rightEnd - leftPos + 1;
        while (leftPos <= leftEnd && rightPos <= rightEnd){
            if(a[leftPos] <= a[rightPos]){
                tmpArr[tmpPos++] = a[leftPos++];
            } else {
                tmpArr[tmpPos++] = a[rightPos++];
            }
        }
        while (leftPos <= leftEnd){
            tmpArr[tmpPos++] = a[leftPos++];
        }
        while (rightPos <= rightEnd){
            tmpArr[tmpPos++] = a[rightPos++];
        }
        for (int i = 0; i < numElements; i++,rightEnd--) {
            a[rightEnd] = tmpArr[rightEnd];
        }

    }


    /**
     *
     * @param a 堆数组
     * @param i 左子树，看成根节点
     * @param n 数组长度
     */
    public static void biuldHeap(int[] a ,int i, int n){
        int child;
        int tmp;
        for (tmp = a[i]; 2*i+1 < n; i=child) {
            child = 2*i+1;
            if(child != n-1 && a[child] < a[child+1]){
                child++;
            }
            if(tmp < a[child]){
                a[i]=a[child];
            }
        }
        a[i]=tmp;
    }



    /**
     *
     * @param a 数组
     * @param root 根节点
     * @param n 数组长
     */
    public static void   myBiuldHeap(int[] a, int root, int n){
        int child;//子节点
        int tmp;//记录根节点
        for (tmp = a[root];  2*root+1< n; root = child) {
            child = 2*root+1;
            if(a[child] < a[child+1]){
                child++;
            }
            if(tmp < a[child]){
                a[root] = a[child];//变换根节点
            }
        }
        a[root] = tmp;
    }

    /***
     * 调整堆
     * @param a 所有节点数组
     * @param root 根节点
     * @param n 数组大小
     */
    public static void myPercdown(int[] a,int root,int n){
        int r = a[root];
        int child;
        for (; 2*root+1 < n-1; root = child) {//n-1是只针对没有交换过的节点进行调整
            child = 2*root+1;
            if(a[child] < a[child+1]){
                child++;
            }
            if(r < a[child]){
                a[root] = a[child];
            }
        }
        a[root] = r;
    }


    /**
     * 堆排序
     */
    @org.junit.Test
    public void heapSort(){
        int arr[]={7,4,8,3,5,12,23};
        System.out.println(Arrays.toString(arr));
        heapSort(arr);
        System.out.println(Arrays.toString(arr));
    }
    //堆排序主方法
    public static void heapSort(int[] arry){
//        myBiuldHeap1(arry,0,arry.length);
//        System.out.println("构建后的数组："+Arrays.toString(arry));
        /**
         * 把arry看成一个破坏结构性质的最大（小）堆，arry.length/2-1获取这个堆的根元素，然后进行调整
         */
        for (int i = arry.length/2-1; i >= 0; i--) {
            myBiuldHeap(arry,i,arry.length);
            System.out.println("根节点的下标为："+i+"构建后的数组："+Arrays.toString(arry));
        }
        System.out.println("构建大顶堆："+Arrays.toString(arry));
        for(int i=arry.length-1;i>0;i--){
            int temp=arry[0];
            arry[0]=arry[i];
            arry[i]=temp;//将堆顶存到最后，最后一个元素存到堆顶
            myBiuldHeap(arry,0,i-1);//确定最后一个元素最大之后继续调整前面元素
//            myPercdown(arry,0,i-1);//确定最后一个元素最大之后继续调整前面元素
        }
    }

    public static void adjustHeap(int[] arr,int star,int len){//调整堆
        System.out.println("交换后的堆："+Arrays.toString(arr));
        int temp=arr[star];
        for(int i=star*2+1;i<len-1;i=i*2+1){
            if(i<len && arr[i]<arr[i+1]){i++;}
            if(temp>=arr[i]){break;}
            arr[star]=arr[i];
            star=i;
        }
        arr[star]=temp;
    }


    /**
     * 希尔排序
     */
    @org.junit.Test
    public void shellSort(){
        int[] a = {81,94,11,96,12,35,17,95,28,58,41,75,15};
        System.out.println("原数组："+Arrays.toString(a));
        System.out.println("数组大小："+a.length);
        int j;
        for (int gap = a.length/2; gap > 0 ; gap /= 2) {

            for (int i = gap; i < a.length; i++) {
                int tmp = a[i];
                for (j = i; j >= gap && tmp < a[j-gap] ; j -= gap) {
                    a[j] = a[j-gap];
                    System.out.println("                插入排序前："+Arrays.toString(a));
                }
                a[j] = tmp;
                //System.out.println("                插入排序后："+Arrays.toString(a));
            }
            System.out.println("gap="+gap+"时："+Arrays.toString(a));
        }
        System.out.println(Arrays.toString(a));
    }

    /**
     * 插入排序
     */
    @org.junit.Test
    public void insetSort(){
        int[] a = {81,94,11,96,12,35,17,95,28,58,41,75,15};
        System.out.println(Arrays.toString(a));
        int j;
        for (int p = 1; p < a.length ; p++) {
            int tmp = a[p];
            for (j = p; j > 0 && tmp < a[j-1] ; j --) {
                a[j] = a[j-1];
            }
            a[j] = tmp;
        }
        System.out.println(Arrays.toString(a));
    }

    @org.junit.Test
    public void testArray(){
        int[] is = new int[]{5,2,4,6,1,3};
        for (int i = 0; i < is.length; i++) {
            int key = is[i+1];
            int j = i;
            while(is[j] > key){
                is[i+1] = is[i];
                j -= j;
            }
            is[i+1] = key;
        }
        System.out.println(Arrays.toString(is));
    }

    @org.junit.Test
    public void b(){
        int[] is = new int[]{5,2,4,6,1,3};
        int i, j;
        int key;
        for(j = 1; j < is.length; j++)
        {
            key = is[j];
            i = j - 1;
            while(i >= 0 && is[i] < key)
            {
                is[i+1] = is[i];
                i--;
            }
            is[i+1] = key;
        }
        System.out.println(Arrays.toString(is));
    }

    /**
     * 计数基数排序
     */
    @org.junit.Test
    public void sort(){
        int[] a = {5,2,4,6,1,3};
        System.out.println(Arrays.toString(a));
        int[] b = new int[a.length];
        sort(a,b,6);
        System.out.println(Arrays.toString(b));
    }
    public int[] sort(int[] a, int[] b, int k) {
        int len=a.length;
        //数组c用来记录待排序数组a中值等于i的元素的个数.
        int[]c=new int[k+1];
        for(int i=0;i<len;i++){
            //c数组的下标代表a数组中的值，c数组中的元素，代表a数组中有几个相同的数字
            c[a[i]]++;//统计序列中各个元素出现的次数.
        }
        System.out.println("记录数组元素中个数数组，c："+Arrays.toString(c));
        for(int i=1;i<=k;i++){
            //算出元素的下标，c[i]+c[i-1]-->本身的个数加上前面的个数等于本身在数组中所在的位置，
            // 因为按照数组下标进行计数的c[i]肯定大于等于c[i-1]
            c[i]=c[i]+c[i-1];//统计小于等于i的元素个数。
        }
        System.out.println("统计小于等于i的元素个数，c："+Arrays.toString(c));
        for(int j=0;j<len;j++){
            int val=a[j];//取出j位置上的值
            int pos=c[val];//根据值的大小获取c中的位置信息
            //减1是因为数组是从0开始的，比如：pos等于，说明j的下标是5-1的位置
            b[pos-1]=val;//将值放入pos-1位置处
            c[val]--;//位置数减一
        }
        return b;

    }
}
