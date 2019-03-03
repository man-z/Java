package com.snw.test;

import java.util.Arrays;

/**
 * 矩阵乘法-->所用的算法思路：分治算法与动态规划(每次先保存好最优的子问题解决方案)
 */
public class MatrixMultiplication {

    public static void main(String[] args) {
        MatrixMultiplication mc = new MatrixMultiplication();
        int n = 5;
        int p[] = { 5,10,4,6,10};
//        int p[] = { 30, 35, 15, 5, 10, 20, 25 };
        int m[][] = new int[n][n];
        int s[][] = new int[n][n];
        int l = p.length-1;
        mc.MatrixChain(p, l,m, s);
        for(int[] a : m){
            System.out.println(Arrays.toString(a));
        }
//        for (int i = 1; i < n; i++) {
//            for (int j = 1; j < n; j++) {
//                System.out.print(m[i][j] + "\t");
//            }
//            System.out.println();
//        }
        System.out.println();
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {

                System.out.print(s[i][j]+" ");
            }
            System.out.println();
        }

        mc.Traceback( 1, 3, s);
    }

    /**
     * 动态规划--->矩阵乘法
     * @param p 矩阵数组
     * @param n 矩阵数组总数
     * @param m 记录最少乘积数
     * @param s
     */
    public static void MatrixChain(int[] p,int n, int[][] m, int[][] s) {
        for(int k = 1; k< n; k++){
            for(int left = 1; left <= n-k; left++){
                //当前循环的矩阵的最后的位置，即：如果循环第三个矩阵，那么就是从第一个矩i阵到i+k的矩阵位置
                int right = left + k;
                m[left][right] = Integer.MAX_VALUE;
                //求出矩阵的最小次数，即：如果循环第三个矩阵，那么就是求第一个矩i阵到i+k（right）的矩阵之间的最小次数
                for(int i = left; i<right; i++){
                    int thisCost = m[left][i] + m[i + 1][right] + p[left-1]*p[i]*p[right];
                    if(thisCost < m[left][right]){
                        //把每次的最小次数存到数组，
                        m[left][right] = thisCost;
                        s[left][right] = i;
                    }
                }
            }
        }
        //从第一个矩阵开始计算，这里是总的矩阵循环的次数
//        for(int k = 1; k < n;k++){
//            //左边矩阵小于右边矩阵才进行循环，循环到最后一个矩阵，在第一个矩阵到第n-当前所循环的矩阵之间循环
//            for(int left = 1;left <= n-k; left++){
//                //本次循环最后的矩阵，比如：如果当前循环的是第三个矩阵，那么就是a[1:3]、a[2:3]、a[3:3]
//                int right = left+k;
//                m[left][right] = Integer.MAX_VALUE;
//                //求每一个矩阵的最小次数（left到right之间的矩阵）
//                for(int i = left;i<right; i++){
//                    int thisCost = m[left][i] + m[i+1][right] + p[left - 1] * p[i] * p[right];
//                    if(thisCost < m[left][right]){
//                        m[left][right] = thisCost;
//                        s[left][right] = i;
//                    }
//                }
//            }
//        }
        //先把相邻的矩阵计算出来，比如：p[1:2],p[2:3],然后在计算p[1:3],p[1:4]
//        for(int r = 1;r < n; r++ ) {
//            //循环列每一行的每一列
//            for(int i = 1; i <= n-r; i++) {
//                //j表示从某矩阵到j矩阵，比如：p[1:3]那么j表示的是第三个矩阵
//                int j = i+r;
//                //计算当前矩阵的乘积次数，初始的乘积，比如：p[1:3]，默认用1(2,3)的乘积
//                m[i][j] = m[i-1][j] + p[i-1]*p[i]*p[j];
//                s[i][j] = i;
//                //找出从第一个矩阵到当前矩阵的最少次数
//                for(int k = i; k < j; k++) {
//                    //int p[] = { 5,10,4,6,10};
//                    //
//                    int t = m[i][k] + m[k+1][j] + p[i-1]*p[k]*p[j];
//                    if(t < m[i][j]) {
//                        m[i][j] = t;
//                        s[i][j] = k;
//                    }
//                }
//            }
//        }
        //先把相邻的矩阵计算出来，比如：p[1:2],p[2:3],然后在计算p[1:3],p[1:4]，n=2表示，第一个矩阵的乘积，A0*A1*A2
//        for(int r = 2;r <= n; r++ ) {
//            //n-r+1表示处理当前矩阵，n-r+1等于矩阵最后一个
//            for(int i = 1; i <= n-r+1; i++) {
//                //j表示从某矩阵到j矩阵，比如：p[1:3]那么j表示的是第三个矩阵
//                int j = i+r-1;
//                //计算当前矩阵的乘积次数，初始的乘积，比如：p[1:3]，默认用1(2,3)的乘积
//                m[i][j] = m[i+1][j] + p[i-1]*p[i]*p[j];
//                s[i][j] = i;
//                //找出从第一个矩阵到当前矩阵的最少次数
//                for(int k = i+1; k < j; k++) {
//                    //int p[] = { 5,10,4,6,10};
//                    //
//                    int t = m[i][k] + m[k+1][j] + p[i-1]*p[k]*p[j];
//                    if(t < m[i][j]) {
//                        m[i][j] = t;
//                        s[i][j] = k;
//                    }
//                }
//            }
//        }
    }

    public static void Traceback(int i, int j, int[][] s) {
        if(i == j) return;
        Traceback(i,s[i][j],s);
        Traceback(s[i][j] + 1,j,s);
        System.out.println("Multiply    A" + i + "," + s[i][j] + "and A" + (s[i][j] + 1) + "," + j);
    }





    public static void matrixChainOrder(int[] p) {  //p个矩阵，要用p.length+1个数字来表示。例如两个矩阵相乘要用p0*p1*p2表示，p1是p0的列，p2的行
        int[][] c = new int[p.length-1][p.length-1];   //保存
//        int[][] s = new int[p.length - 1][p.length-1];

        for (int i = 0; i < c.length; i++) {
            c[i][i]=0;  //矩阵链长度为0时，乘积为0,c[j][j+0];
        }


        for (int i = 1; i < p.length - 1; i++) {  // i 矩阵链的长度
            for (int j = 0; j < p.length-i-1; j++) {  //j从0到p.lenght-i-1，子问题的起始点   计算长度为i的矩阵链，所有的最优值。
                c[j][j+i]=Integer.MAX_VALUE;   //最大值。
                //求j到j+i这段长度乘积的最小值，  k是分割点,比如：矩阵a，b，c；可以分为：a(bc),(ab)c，求分段后的最小值，从第一个矩阵到当前的矩阵最小值
                for (int k = j; k < j+i; k++) {
                    int i1 = c[j][k];
                    int i2 = c[k + 1][j + i];
                    int i3 = p[j] * p[k + 1] * p[j + i + 1];
                    //k为分割点时，乘积的大小，p[j]*p[k+1] * p[j+i+1]表示是计算j,k+1,j+i+1这三个矩阵的次数
                    int value = c[j][k] + c[k + 1][j + i] + p[j]*p[k+1] * p[j+i+1];
                    if (value < c[j][j + i]) {
                        c[j][j+i]=value;
//                        s[j][j + i]=k;  //j到j+i这段矩阵链最优分割点为 k
                    }
                }
            }
        }


        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[1].length; j++) {
                System.out.print(c[i][j]+"      ");
            }
            System.out.println("");
        }
        System.out.println(c[1][2]);
    }
}
