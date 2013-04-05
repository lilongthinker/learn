package strings

import com.google.common.base.CharMatcher
import com.google.common.base.Joiner
import com.google.common.primitives.Ints

import static com.google.common.base.CharMatcher.inRange

/**
 * User: dingyue
 * Date: 13-4-5
 * Time: 下午3:35
 */

//CharMatcher.inRange('a', 'z').or(inRange('A', 'Z'));
a = {
    String string = CharMatcher.DIGIT.retainFrom("some text 89983 and more");
    println string;
}


b = {
    String string = CharMatcher.DIGIT.removeFrom("some text 89983 and more");
    println string;
}
c={
    String[] subdirs = ["usr","local","lib"];
    String directory = Joiner.on("/").join(subdirs);
    println directory;
}

d={
    int[] numbers = [ 1, 2, 3, 4, 5 ];
    String numbersAsString = Joiner.on(";");
    println numbersAsString;
}

a();
b();
c();
d();


e={
    int[] array = [ 1, 2, 3, 4, 5 ];
    int aa = 4;
    //================================================
    boolean hasA = false;
    for (int i : array) {
        if (i == aa) {
            hasA = true;
        }
    }
    println hasA
    //================================================
    boolean contains = Ints.contains(array, a);
    println contains;
}
e();














