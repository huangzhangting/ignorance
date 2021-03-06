package nom.ignorance.test.test.io.netty.protobuf;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import nom.ignorance.test.test.io.netty.NtCommon;
import nom.ignorance.test.test.protobuf.model.SubscribeReqModel;

/**
 * Created by huangzhangting on 2017/8/11.
 */
public class SubReqServer {

    public void bind() throws Exception{
        NtCommon.bind(100, new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                /**
                 * TCP半包处理：ProtobufVarint32FrameDecoder
                 * 还可以继承 LineBasedFrameDecoder || ByteToMessageDecoder 自己处理半包消息
                 * ProtobufDecoder 只负责解码
                 *
                 */
                ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                ch.pipeline().addLast(new ProtobufDecoder(SubscribeReqModel.SubscribeReq.getDefaultInstance()));

                ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                ch.pipeline().addLast(new ProtobufEncoder());
                ch.pipeline().addLast(new SubReqServerHandler());
            }
        });
    }



    public static void main(String[] args) {
        try {
            new SubReqServer().bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
