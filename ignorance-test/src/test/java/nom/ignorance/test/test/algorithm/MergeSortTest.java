package nom.ignorance.test.test.algorithm;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Random;

public class MergeSortTest {

    private void mergeArray(int[] arr, int first, int middle, int last, int[] temp){
        int i = first;
        int j = middle + 1;
        int k = 0;
        while (i <= middle && j <= last){
            if(arr[i] <= arr[j]){
                temp[k++] = arr[i++];
            }else{
                temp[k++] = arr[j++];
            }
        }
        while (i <= middle){
            temp[k++] = arr[i++];
        }
        while (j <= last){
            temp[k++] = arr[j++];
        }
        for(i=0; i<k; i++){
            arr[first + i] = temp[i];
        }
    }

    public void mergeSort(int[] arr, int first, int last, int[] temp){
        if(first < last) {
            int middle = (first + last) / 2;
            mergeSort(arr, first, middle, temp);
            mergeSort(arr, middle + 1, last, temp);
            mergeArray(arr, first, middle, last, temp);
        }
    }


    @Test
    public void test(){
        int n = 30;
        int[] arr = new int[n];
        int[] temp = new int[n];
        Random r = new Random();
        for(int i=0; i<n; i++){
            arr[i] = r.nextInt(n);
        }

        if(n < 1001) {
            System.out.println(JSON.toJSONString(arr));
        }

        long startTime = System.currentTimeMillis();
        mergeSort(arr, 0, n-1, temp);
        System.out.println("cost time(ms): " + (System.currentTimeMillis() - startTime));

        if(n < 1001) {
            System.out.println(JSON.toJSONString(arr));
        }

    }
}
