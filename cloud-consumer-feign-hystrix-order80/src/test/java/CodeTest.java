import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * @author lisw
 * @create 2020/11/16
 */
public class CodeTest {
    public static void main(String[] args) {
//        int[][] A = {{1,2,3},{4,5,6}};
//        converse(A);
//        toBinary(1);
//        reSort(12);
        int[] A = {12,24,8,32};
        int[] B = {13,25,32,11};
        advantageCount(A,B);
    }

    /**
     * leetcode 867. 转置矩阵
     * @param A
     * @return
     */
    public static int[][] converse(int[][] A){
       // System.out.println(A.length + "-----" + A[0].length);
        int col = A[0].length;
        int row = A.length;
        int[][] B = new int[col][row];
        for(int i = 0;i < row;i++){
            for(int j=0; j < col;j++){
                B[j][i] = A[i][j];
            }
        }
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                System.out.print(B[i][j] + " ");
            }
            System.out.println();
        }
        return B;
    }

    /**
     *  leetcode : 868. 二进制间距
     * @param n
     * @return
     */
    public static int toBinary(int n){
        String s = Integer.toBinaryString(n);
        int index1 = 0;
        int index2 = 0;
        int len = 0;
        char[] chars = s.toCharArray();
        System.out.println(chars);
        for (int i = 0; i < chars.length; i++) {
            if(chars[i] == '1'){
                index2 = index1;
                index1 = i ;
                int abs = Math.abs((index2 - index1));
                if(abs > len){
                    len = abs;
                }
            }
        }
        System.out.println(len);
        return len;
    }

    /**
     * 869. 重新排序得到 2 的幂
     * @param integer
     * @return
     */
    public static boolean reSort(Integer integer){
        String s = Integer.toString(integer);
        char[] chars = s.toCharArray();

        return false;
    }


    /**
     * 870. 优势洗牌
     */
    public static int[] advantageCount(int[] A,int[] B){
        int[] C = new int[A.length];
        int len = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < B.length;i++){
            map.put(i,A[i]);
            len = Math.abs(A[i] - B[i]);
            C[i] = A[i];
            for (int j = 0; j < A.length; j++) {
                int abs = Math.abs((A[j] - B[i]));
                if((len < abs) && !map.containsValue(A[j])){
                    len = abs;
                    C[i] = A[j];
                    map.put(i,A[j]);
                }
            }
        }
        for (int i: C) {
            System.out.println(i + " ");
        }
        System.out.println();
        return C;
    }

    /**
     * 872 叶子相似的树
     */
}
