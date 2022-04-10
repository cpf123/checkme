package leecode;

// 非严格先增后减数组查找最大值下标
public class o2 {
    public static void main(String[] args) {
        int[] array = {2, 3, 1, 2, 2, 6, 2, 2, 4, 2};
        int i = find(array);
        System.out.println(i);
    }
    public static int find(int[]a){
        int max=0;
        int index=0;
        for(int i=0;i<a.length;i++){
            if(a[i]>max){
                max=a[i];
                index=i;
            }
        }
        return index;
    }
}
