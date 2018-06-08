package nom.ignorance.test.test.sort;

/**
 * 堆定义：完全二叉树（用数组来表示堆）
 * 大顶堆：（一般用来升序排序）
 * a[i] >= a[2i+1] && a[i] >= a[2i+2]
 *
 * 小顶堆：（一般用来降序排序）
 * a[i] <= a[2i+1] && a[i] <= a[2i+2]
 *
 *
 * 升序堆排序步骤
 * 1、用数组表示堆
 * 2、升序，采用大顶堆
 * 3、构造堆，然后头尾交换
 * 4、排除已交换过的尾部数据，然后循环第三步
 *
 * */
public class HeapSortTest {
    int a[] = {7, 8, 3, 2, 10, 6, 7, 5};

}

/**
 * 构造大顶堆
 * 1、自左向右，自下而上，调整元素
 * 2、找到最后一个非叶子节点，坐标 = a.length/2-1
 *
 * */