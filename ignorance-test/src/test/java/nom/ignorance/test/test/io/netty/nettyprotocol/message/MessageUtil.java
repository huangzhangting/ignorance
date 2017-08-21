package nom.ignorance.test.test.io.netty.nettyprotocol.message;

/**
 * Created by huangzhangting on 2017/8/21.
 */
public class MessageUtil {
    public static NettyMessage build(Byte type, Byte data){
        Header header = new Header();
        header.setType(type);

        NettyMessage message = new NettyMessage();
        message.setHeader(header);
        message.setBody(data);
        return message;
    }

    public static NettyMessage build(Byte type){
        return build(type, null);
    }

    public static NettyMessage build(MessageTypeEnum typeEnum){
        return build(typeEnum.getCode());
    }



    /** ========= 消息检查 ========== */
    public static boolean checkMsg(Object msg, MessageTypeEnum typeEnum){
        NettyMessage message = (NettyMessage)msg;
        return checkMessage(message, typeEnum);
    }
    public static boolean checkMessage(NettyMessage message, MessageTypeEnum typeEnum){
        return message.getHeader()!=null && message.getHeader().getType()==typeEnum.getCode();
    }

}
