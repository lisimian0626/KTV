package com.bestarmedia.libcommon.util;

import java.util.Random;
import java.util.Set;

/**
 * Created by J Wong on 2016/6/28.
 */
public class RandomUtil {

    public static int getRandomNorepit(int RandMax, Set<Integer> RepitNums) {
        int num = getRandomExcept(RandMax, RepitNums);
        RepitNums.add(num);
        return num;
    }

    public static int getRandomExcept(int RandMax, Set<Integer> ExceptNums) {
        Random rand = new Random();
        while (true) {
            int num = rand.nextInt(RandMax);
            if (ExceptNums.contains(num)) {
                continue;
            } else {
                return num;
            }
        }
    }

    public static int[] randomArray(int min, int max, int n) {
        int len = max - min + 1;
        if (max < min || n > len) {
            return null;
        }
        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }
        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }

    public int runCount = 0;//用于记录方法运算次数

    public String[] randomStrings(String[] arr) {
        String[] arr2 = new String[arr.length];
        int count = arr.length;
        int cbRandCount = 0;// 索引
        int cbPosition = 0;// 位置
        int k = 0;
        do {
            runCount++;
            Random rand = new Random();
            int r = count - cbRandCount;
            cbPosition = rand.nextInt(r);
            arr2[k++] = arr[cbPosition];
            cbRandCount++;
            arr[cbPosition] = arr[r - 1];// 将最后一位数值赋值给已经被使用的cbPosition
        } while (cbRandCount < count);
        System.out.println("m3运算次数  = " + runCount);
        return arr2;
    }
}
