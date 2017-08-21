package nom.ignorance.test.test.io.netty.nettyprotocol.message;

import lombok.Data;

/**
 * Created by huangzhangting on 2017/8/21.
 */
@Data
public class NettyMessage {
    private Header header;
    private Object body;
}
