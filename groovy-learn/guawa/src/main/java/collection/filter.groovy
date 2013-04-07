package collection

import com.google.common.collect.Lists

import static com.google.common.collect.Iterators.*
import static com.google.common.base.Predicates.*;

/**
 * User: dingyue
 * Date: 13-4-7
 * Time: 上午9:10
 */
List<String> names = Lists.newArrayList("Aleksander", "Jaran", "Integrasco", "Guava", "Java");

Iterable<String> filtered = filter(names, or(or(equalTo("Aleksander"),equalTo("Jaran")), lengthLessThan(5)));





