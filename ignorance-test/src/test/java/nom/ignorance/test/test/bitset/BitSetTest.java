package nom.ignorance.test.test.bitset;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.BitSet;

@Slf4j
public class BitSetTest {

    @Test
    public void test_1(){
        int[] nums = {6, 6, 8, 3, 7, 9, 7};
        int maxNum = 9;

        BitSet bitSet = new BitSet(maxNum);
        log.info(bitSet.size() + "");
        for(int i : nums){
            if(bitSet.get(i)){
                log.info("is exist：{}", i);
            }
            bitSet.set(i);
        }
        log.info(bitSet.toString());

        int i = bitSet.nextSetBit(0);
        System.out.println(i);

        System.out.println(bitSet.nextClearBit(3));

    }


    @Test
    public void test_2(){
        int i = 1;
        do{
            System.out.println(i);
        }while (++i != 5);

    }
}
