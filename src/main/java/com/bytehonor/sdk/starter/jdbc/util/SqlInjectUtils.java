package com.bytehonor.sdk.starter.jdbc.util;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.lang.spring.creator.StringCreator;
import com.bytehonor.sdk.lang.spring.util.StringObject;

public class SqlInjectUtils {

    private static final char TA = '\'';

    private static final char LK = '%';

    public static String like(String src, boolean left, boolean right) {
        if (src == null) {
            return "";
        }
        String raw = src.trim();
        if (StringObject.isEmpty(raw)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (left && LK != raw.charAt(0)) {
            sb.append(LK);
        }
        sb.append(raw);
        if (right && LK != raw.charAt(raw.length() - 1)) {
            sb.append(LK);
        }
        return sb.toString();
    }

    public static String escape(String src) {
        if (StringObject.isEmpty(src)) {
            return src;
        }
        int len = src.length();
        char[] target = new char[len * 2];
        boolean find = false;
        int at = 0;
        target[at++] = TA;
        for (int i = 0; i < len; i++) {
            if (TA == src.charAt(i)) {
                find = true;
                target[at++] = '\\';
                target[at++] = TA;
            } else {
                target[at++] = src.charAt(i);
            }
        }
        target[at++] = TA;
        if (find) {
            return new String(target, 0, at);
        }
        return StringCreator.create().append(TA).append(src).append(TA).toString();
    }

    public static String column(String src) {
        if (StringObject.isEmpty(src)) {
            return src;
        }
        int len = src.length();
        char[] target = new char[len * 2];
        boolean find = false;
        int at = 0;
        for (int i = 0; i < len; i++) {
            if (';' == src.charAt(i)) {
                find = true;
                continue;
            }
            target[at++] = src.charAt(i);
        }
        if (find) {
            return new String(target, 0, at);
        }
        return src;
    }

    public static int[] listArray(List<Integer> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new int[0];
        }
        int size = list.size();
        int arr[] = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

//    public static void main(String[] args) {
//        List<Object> args1 = new ArrayList<Object>();
//        List<Integer> argTypes = new ArrayList<Integer>();
//        
//        Object[] arr1 = args1.toArray();
//        int[] arr2 = listArray(argTypes);
//        
//        if (arr1 == null) {
//            System.out.println("xxxx");
//        }
//        if (arr1.length == arr2.length) {
//            System.out.println("==" + arr1.length);
//        }
//    }
}
