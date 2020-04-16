package com.jxyzh11.springbootdemo.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author sunny
 * @date 2018/12/1
 */
public class DataUtil {

    private static Logger logger = LoggerFactory.getLogger(DataUtil.class);

    private static Random random = new Random();

    /**
     * 获取不重复的n个list元素
     *
     * @param source 源数据
     * @param count  取的索引的个数
     * @author sunny
     * @date 2018/12/1
     */
    public static <T> List<T> getNonRepeatRandomData(List<T> source, int count) {
        if (CollectionUtils.isEmpty(source)) {
            return Lists.newArrayList();
        }
        int length = source.size();
        List<Integer> indexList = new ArrayList<Integer>(length);
        for (int i = 0; i < length; i++) {
            indexList.add(i);
        }
        if (count > source.size()) {
            return source;
        }
        List<T> result = new ArrayList<T>(count);
        while (result.size() < count) {
            int index = random.nextInt(indexList.size());
            int val = indexList.get(index);
            result.add(source.get(val));
            indexList.remove(index);
        }
        return result;
    }

    public static byte[] getLong(long data) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {
            dos.writeLong(data);
            return bos.toByteArray();
        } catch (IOException e) {
            logger.error("getLong io error", e);
            return null;
        }
    }

    public static int getRandomNum(int numSize) {
        return random.nextInt(numSize);
    }

    public static void main(String[] args) {
        List<Integer> test = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            test.add(i);
        }

        long begin = System.currentTimeMillis();
        for (int k = 0; k < 20000; k++) {
            List<Integer> data = getNonRepeatRandomData(test, 1);
//            System.out.println(data);
            Set<Integer> t1 = new HashSet<Integer>(data.size());
            for (Integer datum : data) {
                if (!t1.add(datum)) {
                    System.out.println("重复了");
                }
            }
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - begin));

    }
}
