package nom.ignorance.test.test.io.netty.nettyprotocol.message;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangzhangting on 2017/8/21.
 */
@Data
public final class Header {
    private int crcCode = 1;
    private int length;
    private long sessionId;
    private byte type;
    private byte priority;
    private Map<String, Object> attachment = new HashMap<>();
}
