package nom.ignorance.test.test.io.netty.nettyprotocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;

/**
 * Created by huangzhangting on 2017/8/21.
 */
public class MarshallingEncoderImpl {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    Marshaller marshaller;

    public MarshallingEncoderImpl() {

    }
}
