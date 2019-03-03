package test;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 后缀数组
 */
public class SuffixArray {

    /**
     * 计算最长公共前缀
     * @param s1
     * @param s2
     * @return
     */
    public static int computeLCP(String s1, String s2){
        int i = 0;
        while (i < s1.length() && i < s2.length() && s1.charAt(i) == s2.charAt(i)){
            i++;
        }
        return i;
    }

    /**
     * 创建后缀数组
     * @param str 需要生成后缀数组的字符串
     * @param SA 记录子字符串的下标
     * @param LCP 公共最长前缀
     */
    public static void createSuffixArray(String str, int[] SA, int[] LCP){
        //根据字符串长度截取 一共截取str长度次
        if(str.length() != SA.length || str.length() != LCP.length){
            throw new IllegalArgumentException();
        }
        int n = str.length();
        //生成后缀数组
        String[] suffix = new String[n];
        for (int i = 0;i<n;i++){
            suffix[i] = str.substring(i);
        }
        Arrays.sort(suffix);
        //记录每一个后缀在字符串出现的位置
        for (int i = 0; i < n;i++){
            //字符串的总长度减去每一个后缀的长度，等于这个后缀在这个字符串的下标
            SA[i] = n - suffix[i].length();
        }
        //计算每一个后缀的最长公共前缀
        for (int i = 1; i<n;i++){
            LCP[i] = computeLCP(suffix[i-1],suffix[i]);
        }
    }

    public static void main(String[] args) {
        String s = "BRACADABRA$$RACADABRA";
//        String s = "banana";
        int[] SA = new int[s.length()];
        int[] LCP = new int[s.length()];
        createSuffixArray(s,SA,LCP);
        System.out.println(s+"字符串长度为："+s.length());
        int n = s.length();
        String[] suffix = new String[n];
        for (int i = 0;i<n;i++){
            suffix[i] = s.substring(i);
        }
        Arrays.sort(suffix);
        for (int i = 0; i < s.length(); i++){
            System.out.println(suffix[i]+"--->在字符串："+s+"的下标为："+SA[i]+"--->最长公共前缀为："+LCP[i]);
        }
        System.out.println("下标："+Arrays.toString(SA));
        System.out.println("LCP:" + Arrays.toString(LCP));
    }
}
