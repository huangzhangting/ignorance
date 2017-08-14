package nom.ignorance.test.test.io.netty.protobuf;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import nom.ignorance.test.test.io.netty.NtCommon;
import nom.ignorance.test.test.protobuf.model.SubscribeRespModel;

/**
 * Created by huangzhangting on 2017/8/14.
 */
public class SubReqClient {
    public void connect(int sendCount) throws Exception{
        NtCommon.connect(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                ch.pipeline().addLast(new ProtobufDecoder(SubscribeRespModel.SubscribeResp.getDefaultInstance()));
                ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                ch.pipeline().addLast(new ProtobufEncoder());
                ch.pipeline().addLast(new SubReqClientHandler(sendCount));
            }
        });
    }



    public static void main(String[] args){
        try {
            new SubReqClient().connect(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
