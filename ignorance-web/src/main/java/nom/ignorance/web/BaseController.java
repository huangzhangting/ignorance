package nom.ignorance.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangzhangting on 2017/7/13.
 */
@Controller
@RequestMapping("base")
public class BaseController {

    @RequestMapping("test")
    @ResponseBody
    public Object test(){
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 1);
        map.put("3", 1);
        return map;
    }
}
