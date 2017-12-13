package nom.ignorance.test.test.concurrenttest.forkjoin;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RecursiveTask;

/**
 * Created by huangzhangting on 2017/12/13.
 */
@Slf4j
public class ComputeTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 2;

    private int start;
    private int end;

    public ComputeTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        boolean canCompute = (end - start) <= THRESHOLD;
        if(canCompute){
            log.info("开始计算，start={}，end={}", start, end);
            for(int i=start; i<=end; i++){
                sum += i;
            }
        }else{
            int middle = (end + start)/2;
            ComputeTask leftResult = new ComputeTask(start, middle);
            ComputeTask rightResult = new ComputeTask(middle + 1, end);
            //分割子任务
            leftResult.fork();
            rightResult.fork();
            //合并结果
            sum = leftResult.join() + rightResult.join();
        }

        return sum;
    }
}
