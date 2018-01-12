package nom.ignorance.test.test.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Test {
    public static void main(String[] args) {
        EventFactory<LongEvent> eventFactory = new LongEventFactory();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        int ringBufferSize = 1024 * 1024; // RingBuffer 大小，必须是 2 的 N 次方；
        Disruptor<LongEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, threadFactory,
                ProducerType.SINGLE, new YieldingWaitStrategy());
        EventHandler<LongEvent> handler = new LongEventHandler();
        disruptor.handleEventsWith(handler);
        disruptor.start();

        publishEvent(disruptor);

        disruptor.shutdown();

    }

    public static void publishEvent(Disruptor<LongEvent> disruptor){
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        long sequence = ringBuffer.next();

        try {
            LongEvent event = ringBuffer.get(sequence);//获取该序号对应的事件对象；
            long data = 23;//获取要通过事件传递的业务数据；
            event.setValue(data);
        } finally{
            ringBuffer.publish(sequence);//发布事件；
        }
    }
}
