package nom.ignorance.test.test.guava;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.cache.*;
import com.google.common.collect.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by huangzhangting on 2017/12/12.
 */
public class GuavaTest {

    @Test
    public void test_joiner(){
        Joiner joiner = Joiner.on(" , ").skipNulls();
        String str = joiner.join("11", "33", null, "22", "55");
        System.out.println(str);

    }

    @Test
    public void test_splitter(){
        Splitter splitter = Splitter.on(",").trimResults();
        List<String> list = splitter.splitToList("11 , 33 , 22 , 55");
        System.out.println(list);
    }

    @Test
    public void test_multimap(){
        ListMultimap<String, Integer> multimap = ArrayListMultimap.create();
        multimap.put("1", 2);
        multimap.put("1", 1);
        multimap.put("1", 3);
        multimap.put("2", 3);

        System.out.println(multimap);
        System.out.println(multimap.get("1"));
    }

    @Test
    public void test_immutable(){
        ImmutableSet<Integer> set = ImmutableSet.of(1, 2, 3, 4);
        System.out.println(set);
    }

    @Test
    public void test_sets(){
        Set<Integer> set1 = Sets.newHashSet(1, 2, 3, 4, 5);
        Set<Integer> set2 = Sets.newHashSet(1, 2, 7, 4, 50);
        Sets.SetView<Integer> view = Sets.difference(set1, set2);
        System.out.println(view);
    }


    @Test
    public void test_cache() throws Exception{
        String key = "hzt";
        Cache<String, Object> cache = CacheBuilder.newBuilder()
                .maximumSize(20)
                .initialCapacity(5)
                .expireAfterWrite(20, TimeUnit.SECONDS) //设置失效时间，创建时间
                .expireAfterAccess(20, TimeUnit.HOURS) //设置失效时间，最后一次被访问
                .build();
        Object name = cache.get(key, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return "hzt_test_call";
            }
        });
        System.out.println(name);
        System.out.println(cache.asMap());
        cache.put(key, "put_test");
        System.out.println(cache.getIfPresent(key));

        test_loadingCache(key);
    }
    public void test_loadingCache(String key) throws Exception{
        LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
                .maximumSize(20) //设置大小，条目数
                .expireAfterWrite(20, TimeUnit.SECONDS) //设置失效时间，创建时间
                .expireAfterAccess(20, TimeUnit.HOURS) //设置时效时间，最后一次被访问
                .removalListener(new RemovalListener<String, Object>() { //移除缓存的监听器
                    public void onRemoval(RemovalNotification<String, Object> notification) {
                        System.out.println("有缓存数据被移除了");
                    }
                })
                .build(new CacheLoader<String, Object>() {//加载缓存

                    @Override
                    public Object load(String key) throws Exception {
                        return key + "_test_loader";
                    }
                });

        System.out.println(cache.asMap());
        System.out.println(cache.get(key));
        System.out.println(cache.asMap());

        cache.put(key, "put_test");
        System.out.println(cache.get(key));

    }


    @Test
    public void test_order(){
        List<Integer> list = Lists.newArrayList(1, 2, 4, 6, 7, 8);
        Ordering<Integer> ordering = Ordering.natural();
        System.out.println(ordering.min(list));
        System.out.println(ordering.max(list));

    }

    @Test
    public void test_coll(){
        List<User> userList = Lists.newArrayList(
                new User("1", 0)
                , new User("2", 1)
                , new User("3", 0)
                , new User("4", 1)
                , new User("5", 1)
        );

        List<User> newList = ImmutableList.copyOf(Collections2.filter(userList, new Predicate<User>() {
            @Override
            public boolean apply(User input) {
                return input.getSex() == 0;
            }
        }));

        System.out.println(userList);
        System.out.println(newList);
    }

}
