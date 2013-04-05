package collection

import com.google.common.collect.Sets

import static com.google.common.collect.Sets.*

/**
 * User: dingyue
 * Date: 13-4-5
 * Time: 下午3:15
 */


HashSet setA = newHashSet(1, 2, 3, 4, 5);
HashSet setB = newHashSet(4, 5, 6, 7, 8);

SetView union = Sets.union(setA, setB);
System.out.println("union:");
for (Integer integer : union)
    System.out.println(integer);

SetView difference = Sets.difference(setA, setB);
System.out.println("difference:");
for (Integer integer : difference)
    System.out.println(integer);

SetView intersection = Sets.intersection(setA, setB);
System.out.println("intersection:");
for (Integer integer : intersection)
    System.out.println(integer);