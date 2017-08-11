package nom.ignorance.test.test.io.netty.codec.example;

import lombok.Getter;
import lombok.Setter;
import org.msgpack.annotation.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by huangzhangting on 2017/8/11.
 */
@Getter
@Setter
//必须要有这个注解，不然使用不了msgpack
@Message
public class UserInfo {
    private static final Random RAN = new Random();

    private Integer id;
    private String name;
    private Integer age;


    public static List<UserInfo> userInfoList(int sendCount){
        List<UserInfo> list = new ArrayList<>();
        for(int i=0; i<sendCount; i++){
            UserInfo userInfo = new UserInfo();
            userInfo.setId(i);
            userInfo.setName("user-"+i);
            userInfo.setAge(RAN.nextInt(50));
            list.add(userInfo);
        }
        return list;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
