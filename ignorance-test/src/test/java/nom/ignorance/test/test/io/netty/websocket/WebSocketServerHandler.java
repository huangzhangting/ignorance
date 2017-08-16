package nom.ignorance.test.test.io.netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Created by huangzhangting on 2017/8/16.
 */
@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final String WS_URL = "ws://localhost:8080/ws";

    private WebSocketServerHandshaker handshaker;


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest){
            handleHttpRequest(ctx, (FullHttpRequest)msg);
        }else if(msg instanceof WebSocketFrame){
            handleWebSocketFrame(ctx, (WebSocketFrame)msg);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request){
        if(!request.decoderResult().isSuccess()
                || !"websocket".equals(request.headers().get("Upgrade"))){
            sendHttpResponse(ctx, request,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        // 构造握手响应返回
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(WS_URL, null, false);
        handshaker = factory.newHandshaker(request);
        if(handshaker==null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else{
            // 会添加WebSocket编解码器到ChannelPipeline
            handshaker.handshake(ctx.channel(), request);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        // 关闭指令
        if(frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame.retain());
            return;
        }
        // ping 消息
        if(frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 仅支持文本消息
        if(!(frame instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException(frame.getClass().getName()+" frame type not supported");
        }

        String data = ((TextWebSocketFrame) frame).text();
        log.info("{} receive data : {}", ctx.channel(), data);
        ctx.channel().write(new TextWebSocketFrame(data+"，欢迎使用netty websocket服务，现在时间："+new Date().toString()));
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, FullHttpResponse resp){
        if(resp.status().code() != 200){
            ByteBuf buf = Unpooled.copiedBuffer(resp.status().toString(), CharsetUtil.UTF_8);
            resp.content().writeBytes(buf);
            buf.release();
            HttpHeaderUtil.setContentLength(resp, resp.content().readableBytes());
        }

        ChannelFuture future = ctx.channel().writeAndFlush(resp);
        if(!HttpHeaderUtil.isKeepAlive(req) || resp.status().code() != 200){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
