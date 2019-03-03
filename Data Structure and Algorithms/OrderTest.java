package com.snw.test;

import com.snw.supplyChain.service.InquiryPriceOrderService;
import com.snw.supplyChain.utils.DateFormatUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.*;

//@RunWith(SpringJUnit4ClassRunner.class)     //琛ㄧず缁ф壙浜哠pringJUnit4ClassRunner绫�
//@ContextConfiguration(locations = {"classpath:application-*.xml"})
public class OrderTest {


    @Test
    public void math(){

        double v = Math.random();
        System.out.println(v);
        double v1 = v * 101;
        System.out.println(v1);
        System.out.println(Math.floor(v1));
    }
//    @Autowired
//	private InquiryPriceOrderService inquiryPriceOrderService;
    int[] s = {-4,0,0,2,0,4,4,6,0,8,8,10,8,12,12,14};
    public int find(int x){

         //s[x]涓鸿礋鏁版椂,璇存槑 x 涓鸿瀛愰泦鍚堢殑浠ｈ〃(涔熷嵆鏍戞牴), 涓攕[x]鐨勫�艰〃绀烘爲鐨勯珮搴�
         if(s[x] < 0){
             System.out.println("鏈�缁堣繑鍥炵殑涓嬫爣锛�"+x+"");
             return x;
         }
         else {
             System.out.println("s[x]鐨勪笅鏍囦负锛�"+x);
             return s[x] = find(s[x]);//浣跨敤浜嗚矾寰勫帇缂�,璁╂煡鎵捐矾寰勪笂鐨勬墍鏈夐《鐐归兘鎸囧悜浜嗘爲鏍�(浠ｈ〃鑺傜偣)
         }
             //return find(s[x]); 娌℃湁浣跨敤 璺緞鍘嬬缉
     }
     @Test
     public  void find(){
         System.out.println(Arrays.toString(s));
        find(15);
         System.out.println(Arrays.toString(s));

     }




    @Test
    public  void radixSort() {
        String[] arr = new String[]{"064", "008", "000", "001", "343", "010","0022","2323","0001"};
        System.out.println(Arrays.toString(arr));
        radixSort(arr, 4);
        System.out.println(Arrays.toString(arr));
    }
    public void radixSort(String [] arr, int maxLen)
    {
        final int BUCKETS = 256;
        ArrayList<String> [] wordsByLength = new ArrayList[maxLen + 1];
        ArrayList<String> [] buckets = new ArrayList[BUCKETS];

        for (int i = 0; i < wordsByLength.length; i++)
            wordsByLength[i] = new ArrayList();
        for (int i = 0; i < BUCKETS; i++)
            buckets[i] = new ArrayList();
        for (String s : arr)
            wordsByLength[s.length()].add(s);

        int idx = 0;
        //鏍规嵁瀛楃涓查暱搴︽《鎺掑簭
        for (ArrayList<String> wordList : wordsByLength)
            for (String s : wordList)
                arr[idx++] = s;

        int startIndex = arr.length;
        for (int pos = maxLen - 1; pos >= 0; pos--)
        {
            startIndex -= wordsByLength[pos + 1].size();

            for (int i = startIndex; i < arr.length; i++)
                buckets[arr[i].charAt(pos)].add(arr[i]);

            idx = startIndex;
            for (ArrayList<String> thisBucket : buckets)
            {
                for (String s : thisBucket)
                    arr[idx++] = s;
                thisBucket.clear();
            }
        }
    }



    public void strSort(String[] a, int stringLen){
        final int BUCKETS = 256;
        ArrayList<String>[] buckets = new ArrayList[BUCKETS];
        for (int i = 0; i < BUCKETS; i++) {
            buckets[i] = new ArrayList<String>();
        }
        for (int pos = stringLen - 1; pos >= 0; pos--) {
            for ( String s : a){
                buckets[s.charAt(pos)].add(s);
            }
            int idx = 0;
            for(ArrayList<String> thisBucket:buckets){
                for(String s :thisBucket){
                    a[idx++] = s;
                }
                thisBucket.clear();
            }
        }
    }


    public void raidxSort(String[] arr, int stringLen) {
        final int BUCKETS = 256;
        ArrayList<ArrayList<String>> buckets = new ArrayList();


        for(int i = 0; i < BUCKETS; i++)
            buckets.add(new ArrayList<String>());

        for (int pos = stringLen - 1; pos >= 0; pos--)// 涓�鍏辫繘琛宻tringLen瓒熸帓搴�
        {
            for (String s : arr)
                buckets.get(s.charAt(pos) - 'a').add(s);// 鏍规嵁姣旇緝鐨勫瓧绗﹀皢 瀛楃涓叉斁鍒扮浉搴旂殑妗朵腑

            int idx = 0;
            for (ArrayList<String> listStr : buckets){
                for (String str : listStr)
                    arr[idx++] = str;//灏嗘《涓殑鏁版嵁鏀惧洖鍒板師鏁扮粍涓�
                listStr.clear();//娓呯┖妗朵腑鐨勬暟鎹�,浠ヤ究涓嬩竴瓒熸帓搴忓仛鍑嗗
            }
        }
    }


    @Test
    public void raidxSort() {
        String[] arr = { "alpqd","alpqa", "wyzm","cmed",};
        System.out.println(Arrays.toString(arr));
        strSort(arr,4);
//        raidxSort(arr, arr.length);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 璁℃暟鍩烘暟鎺掑簭
     */
    @Test
    public void numSort(){
        int[] a = {2,8,4,4,6,3,10,9,1,5,8,7,7,0};
        System.out.println(Arrays.toString(a));
        int [] b = new int[a.length];
        sort(a,b,10);
//        myNumSort(a,5);
//        radixSort(a,a.length,4);
        System.out.println(Arrays.toString(a));
        System.out.println("b:"+Arrays.toString(b));
    }

    public static int[] sort(int[] a, int[] b, int k) {
        int len=a.length;
        //鏁扮粍c鐢ㄦ潵璁板綍寰呮帓搴忔暟缁刟涓�肩瓑浜巌鐨勫厓绱犵殑涓暟.锛岃鏁颁笉鏄牴鎹暟缁勪笅鏍囪鏁帮紝鎵�浠ヨ姣斿師鏉ョ殑鏁扮粍澶т簬1
        int[]c=new int[k+1];
        for(int i=0;i<len;i++){
            c[a[i]]++;//缁熻搴忓垪涓悇涓厓绱犲嚭鐜扮殑娆℃暟.
        }
        System.out.println("a鏁扮粍涓厓绱犵殑涓暟锛�"+Arrays.toString(c));
        //鑾峰彇鏁扮粍鍏冪礌涓墍鍦ㄧ殑浣嶇疆锛屽洜涓烘槸鏍规嵁妗舵帓搴忕殑锛屾墍浠ユ槸鏈夊簭鐨勪竴涓鏁帮紝閭ｄ箞c[i]鑲畾鏄ぇ浜庣瓑浜庡畠鍓嶉潰鐨勫厓绱犳墍浠[i]+c[i-1]灏辩‘瀹氫簡c[i]鍦ㄦ暟缁勪腑鐨勪綅缃�
        for(int i=1;i<=k;i++){
            c[i]=c[i]+c[i-1];//缁熻灏忎簬绛変簬i鐨勫厓绱犱釜鏁般��
        }
        System.out.println("c鏁扮粍涓厓绱犵殑鎵�鍦ㄤ綅缃細"+Arrays.toString(c));
        for(int j=0;j<len;j++){
            int val=a[j];//鍙栧嚭j浣嶇疆涓婄殑鍊�
            int pos=c[val];//鏍规嵁鍊肩殑澶у皬鑾峰彇c涓殑浣嶇疆淇℃伅
            //鐢变簬鏁扮粍鏄粠0寮�濮嬶紝鎵�浠ユ瘡涓厓绱犻兘瑕佸噺1
            b[pos-1]=val;//灏嗗�兼斁鍏os-1浣嶇疆澶�
            //閬垮厤鏁扮粍鏈夐噸澶嶇殑鍏冪礌锛屽鏋滀笉鍑�1锛岄偅涔堝悗闈㈢殑鍏冪礌浼氳鐩栧墠闈㈢殑鍏冪礌锛屼粠鑰屽鑷村厓绱犱涪澶�
            c[val]--;//浣嶇疆鏁板噺涓�
        }
        System.out.println("c锛�"+Arrays.toString(c));
        return b;
    }


    public void countSort(int[] a,int numLen){
        final int BUCKETS = 800;
        int n = a.length;
        int[] buff = new int[n];
        int[] in = a;
        int[] out = buff;
        for (int i = 0; i < numLen; i++) {
            int[] count = new int[BUCKETS+1];
            for (int j = 0; j < n; j++) {
                count[in[i]+1]++;
            }
            for (int b = 1; b <= BUCKETS ; b++) {
                count[b] += count[b-1];
            }
            for (int j = 0; j < n; j++) {
                out[count[in[j]]++] = in[j];
            }
            int[] tmp = in;
            in = out;
            out = tmp;
        }
        if(numLen % 2 == 1){
            for (int i = 0; i < a.length; i++) {
                out[i] = in[i];
            }
        }
    }

    /**
     *
     * @param a
     * @param numLen 寰幆鐨勬鏁帮紝鏍规嵁鏁扮粍涓渶澶х殑鍊硷紝鎴栬�呮槸鏈�澶х殑闀垮害
     */
    public  void myNumSort(int[] a,int numLen){
        //鍒濆鍖栧崄涓《锛屼互渚夸簬瀵逛綅鏁板瓨鍌紝鐢变簬鏁板瓧涓殑涓轰綅鏁版渶澶ф槸鍙湁10锛屽324锛屼綅鏁板垎鍒负3锛�2锛�4
        ArrayList<Integer>[] buckets = new ArrayList[10];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<Integer>();
        }
        //鍏堜互涓綅鎺掑簭锛岀劧鍚庡崄浣嶇櫨浣嶇瓑
        int ext = 1;
        //杩欓噷寰幆鐨勬鏁版槸鏍规嵁鏁扮粍涓渶澶х殑鍊�
        for (int i = 0; i < numLen; i++) {
            for (int n:a) {
                //鑾峰彇鏁板瓧鐨勪綅鏁�
                int inde = (n / ext) % 10;
                buckets[inde].add(n);
            }
            int index = 0;
            //鎶奲uckets鏁扮粍涓殑copy鍒板師鏈夌殑鏁扮粍涓紝杩欐椂buckets宸茬粡鎺掑ソ搴忥紝鍥犱负閬嶅巻鏁板瓧鏄粠0寮�濮嬶紝鑰屽湪瀛樺偍buckets鐨勬椂鍊欙紝鏄互鏁扮粍涓嬫爣杩涜瀛樺偍鐨�
            for (ArrayList<Integer> num : buckets) {
                if(num != null && !num.isEmpty()){
                    for (int n:num) {
                        a[index++] = n;
                    }
                    num.clear();
                }
            }
            ext *= 10;
        }
    }
    // 鍚勪綅瑁呴�氭硶

    public int[] radixSort(int[] A, int n, int numLen) {
        int length = n;
        int divisor = 1;// 瀹氫箟姣忎竴杞殑闄ゆ暟锛�1,10,100...
        int[][] bucket = new int[10][length];// 瀹氫箟浜�10涓《锛屼互闃叉瘡涓�浣嶉兘涓�鏍峰叏閮ㄦ斁鍏ヤ竴涓《涓�
        int[] count = new int[10];// 缁熻姣忎釜妗朵腑瀹為檯瀛樻斁鐨勫厓绱犱釜鏁�
        int digit;// 鑾峰彇鍏冪礌涓搴斾綅涓婄殑鏁板瓧锛屽嵆瑁呭叆閭ｄ釜妗�
        for (int i = 1; i <= numLen; i++) {// 缁忚繃4娆¤閫氭搷浣滐紝鎺掑簭瀹屾垚
            for (int temp : A) {// 璁＄畻鍏ユ《
                digit = (temp / divisor) % 10;
                bucket[digit][count[digit]++] = temp;
            }
            int k = 0;// 琚帓搴忔暟缁勭殑涓嬫爣
            for (int b = 0; b < 10; b++) {// 浠�0鍒�9鍙锋《鎸夌収椤哄簭鍙栧嚭
                if (count[b] == 0)// 濡傛灉杩欎釜妗朵腑娌℃湁鍏冪礌鏀惧叆锛岄偅涔堣烦杩�
                    continue;
                for (int w = 0; w < count[b]; w++) {
                    A[k++] = bucket[b][w];
                }
                count[b] = 0;// 妗朵腑鐨勫厓绱犲凡缁忓叏閮ㄥ彇鍑猴紝璁℃暟鍣ㄥ綊闆�
            }
            divisor *= 10;
        }
        return A;
    }

    @Test
     public void testInq(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("寮�濮嬫棩鏈燂細"+sdf.format(DateFormatUtil.getBeginDayOfWeek())+"缁撴潫鏃ユ湡锛�"+sdf.format(DateFormatUtil.getEndDayOfWeek()));
        System.out.println("寮�濮嬫棩鏈燂細"+sdf.format(DateFormatUtil.getBeginDayOfMonth())+"缁撴潫鏃ユ湡锛�"+sdf.format(DateFormatUtil.getEndDayOfMonth()));
        //         inquiryPriceOrderService.testName("鑰佸紶");
     }


}
