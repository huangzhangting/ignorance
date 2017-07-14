package nom.ignorance.component.utils;

import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.util.*;


/**
 *
 * 对象拷贝工具类，方便DO/BO/VO转换，因为属性是一致的
 *
 * Created by huangzhangting on 2017/5/24.
 */
public class ObjectUtil {
    /**
     *
     * 拷贝对象
     *
     * @param source 源对象
     * @param cls 目标对象class
     * @param <S> 源对象
     * @param <T> 目标对象
     * @return 目标对象
     */
    @SuppressWarnings("unchecked")
    public static <S, T> T copy(S source, Class<T> cls){
        if(source == null || cls == null){
            return null;
        }
        if(isBasicType(source)){
            return (T)source;
        }
        try {
            T obj = cls.newInstance();
            BeanUtils.copyProperties(source, obj);
            return obj;
        }catch (Exception e){
            throw new RuntimeException("copy object error. "+e.getMessage(), e);
        }
    }
    /** 基础数据类型判断 */
    private static <S> boolean isBasicType(S source){
        if(source instanceof Boolean || source instanceof String
                || source instanceof Integer || source instanceof Long
                || source instanceof BigDecimal || source instanceof Double){
            return true;
        }
        return false;
    }

    /**
     *
     * 拷贝对象集合
     *
     * @param sourceCol 源对象集合
     * @param cls 目标对象class
     * @param <S> 源对象
     * @param <T> 目标对象
     * @return 目标对象集合
     */
    public static <S, T> List<T> copy4List(Collection<S> sourceCol, Class<T> cls){
        List<T> list = new ArrayList<>();
        if(sourceCol == null || cls == null){
            return list;
        }
        for(S source : sourceCol){
            T obj = copy(source, cls);
            if(obj != null){
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 对象转map，属性为key
     * @param object
     * @return
     */
    public static Map<String, Object> toMap(Object object){
        Map<String, Object> map = new HashMap<>();
        if(object == null){
            return map;
        }
        try {
            BeanInfo bean = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] pds = bean.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                String fieldName = pd.getName();
                if (!"class".equals(fieldName)) {
                    Object value = pd.getReadMethod().invoke(object);
                    if(value != null){
                        map.put(fieldName, value);
                    }
                }
            }
            return map;
        }catch (Exception e){
            throw new RuntimeException("object to map error. "+e.getMessage(), e);
        }
    }

    public static List<Map<String, String>> objToStrMapList(List<Map<String, Object>> dataList){
        if(dataList.isEmpty()){
            return new ArrayList<>();
        }
        List<Map<String, String>> list = new ArrayList<>();
        for(Map<String, Object> map : dataList){
            Map<String, String> m = new HashMap<>();
            for(Map.Entry<String, Object> entry : map.entrySet()){
                m.put(entry.getKey(), entry.getValue()==null?"":entry.getValue().toString());
            }
            list.add(m);
        }

        return list;
    }

}
