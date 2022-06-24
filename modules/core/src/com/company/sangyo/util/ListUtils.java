package com.company.sangyo.util;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ListUtils {

    /**
     * 差集 (list1 - list2)
     *
     * @param list1
     * @param list2
     * @return
     */
    public static <T> List getReduceList(List<T> list1, List<T> list2) {
        List<T> reduce = list1.stream().filter(item -> !list2.contains(item)).collect(toList());
        return reduce;
    }
}
