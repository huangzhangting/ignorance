package nom.ignorance.test.test.disruptor;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LongEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent longEvent, long sequence, boolean endOfBatch) throws Exception {
        log.info("处理事件--> event:{}, sequence:{}, endOfBatch:{}", longEvent, sequence, endOfBatch);
    }
}
