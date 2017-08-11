package nom.ignorance.test.test.serialization;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 2017/8/10.
 */
public class MsgPackTest {

    public static void main(String[] args) throws Exception{
        List<String> list = new ArrayList<String>(){{
            add("hzt");
            add("23123");
            add("msg pack");
            add("hzt 0oio");
        }};

        //序列化
        MessagePack pack = new MessagePack();
        byte[] bytes = pack.write(list);

        //反序列化
        List<String> data = pack.read(bytes, Templates.tList(Templates.TString));
        System.out.println(data);
    }
}
