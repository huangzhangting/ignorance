package nom.ignorance.test.test.protobuf;

import nom.ignorance.test.test.protobuf.model.PersonModel;

/**
 * Created by huangzhangting on 2017/8/11.
 */
public class Test {
    public static void main(String[] args){
        PersonModel.Person person = PersonModel.Person.newBuilder()
                .setId(1).setName("hzt").setEmail("151515151@163.com")
                .build();

        System.out.println(person);
    }
}
