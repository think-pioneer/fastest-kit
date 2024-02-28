package xyz.think.fastest.http.internal;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Date: 2021/12/18
 */
final class Lists {
    private Lists() {
    }

    public static <K> List<K> newArrayList() {
        return new ArrayList();
    }

    public static <K> List<K> newLinkedList() {
        return new LinkedList();
    }

    public static <K> List<K> newLinkedList(Collection<K> c) {
        return new LinkedList(c);
    }

    public static <K> List<K> newArrayList(Collection<K> c) {
        return new ArrayList(c);
    }

    @SafeVarargs
    public static <K> List<K> newArrayList(K... elements) {
        List<K> result = new ArrayList();
        Collections.addAll(result, elements);
        return result;
    }

    public static <K> List<K> newArrayList(int size) {
        return new ArrayList(size);
    }

    public static <K> List<K> intersection(List<K> list1, List<K> list2) {
        Stream var10000 = list1.stream();
        list2.getClass();
        return (List)var10000.filter(list2::contains).collect(Collectors.toList());
    }

    public static <K> List<K> merge(Collection<K> l1, Collection<K> l2) {
        List<K> result = newArrayList(l1);
        result.addAll(l2);
        return result;
    }

    @SafeVarargs
    public static <T> List<T> merge(List<T> l1, BiPredicate<T, T> condition, List<T>... lists) {
        List<T> result = newArrayList((Collection)l1);
        Arrays.stream(lists).flatMap(Collection::stream).forEach((eachItem) -> {
            boolean exists = result.stream().anyMatch((e) -> {
                return condition.test(e, eachItem);
            });
            if (!exists) {
                result.add(eachItem);
            }

        });
        return result;
    }
}
