package nom.ignorance.test.test.commtest;

import com.google.common.collect.Lists;
import com.sun.istack.internal.NotNull;
import nom.ignorance.component.utils.DateUtils;
import nom.ignorance.component.utils.IoUtil;
import nom.ignorance.component.utils.http.HttpClientResult;
import nom.ignorance.component.utils.http.HttpClientUtil;
import org.junit.Test;

import java.io.File;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by huangzhangting on 17/4/27.
 */
public class CommTest {

    @Test
    public void test_Lambda(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("new Runnable");
            }
        });
        thread.run();

        thread = new Thread(() -> {
            System.out.println("use Lambda");
        });
        thread.run();
    }

    @Test
    public void test_Stream(){
        List<Integer> list = Lists.newArrayList(190,20,3,4);
        long l = list.stream().sequential().sorted().count();
        System.out.println(l);

        test_annotation(null);
    }
    private void test_annotation(@NotNull Integer id){
        System.out.println(id);
    }

    @Test
    public void test_time(){
        LocalDate date = LocalDate.now();
        System.out.println(date.toString());

        LocalTime time = LocalTime.now();
        System.out.println(time.toString());

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now.toString());

    }

    @Test
    public void test_11(){
        test_switch(2);

        System.out.println(Runtime.getRuntime().availableProcessors());
    }

    public void test_switch(int i){
        int result = 0;
        switch (i){
            case 1:
                result += 1;
            case 3:
                result += 3;
            case 2:
                result += 2;
        }
        System.out.println(result);
    }

    @Test
    public void test_list_clone(){
        ArrayList<String> list = new ArrayList<>();
        list.add("123");
        list.add("1231");
        list.add("1232");
        list.add("1233");
        list.add("1234");

        list.clone();
    }


    public static void main(String[] args) {
        String url = "https://mapi.wpai.com/mobile/app/constant";
        long time = 1000*5*60;
        Writer writer = IoUtil.getWriter("app_constant.txt");
        StringBuilder sb = new StringBuilder();
        while (true){
            HttpClientResult result = HttpClientUtil.get(url);

            sb.append("time=");
            sb.append(DateUtils.dateToString(new Date(), DateUtils.yyyy_MM_dd_HH_mm_ss));
            sb.append(", url=").append(url);
            sb.append(", data=").append(result.getData());
            sb.append("\n");

            IoUtil.writeFile(writer, sb.toString());

            try {
                Thread.sleep(time);
            } catch (Exception e) {
                e.printStackTrace();
            }

            sb.setLength(0);
        }
    }


    @Test
    public void test_Divide(){
        Integer n = 3;
        Integer m = 7;

        Object[] objects = {1, "45", 123, "90"};

    }
}
