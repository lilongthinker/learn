package base.isnull

import com.google.common.base.Objects
import com.google.common.base.Optional
import com.google.common.base.Strings
import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import com.google.common.primitives.Ints

import static com.google.common.base.Preconditions.checkArgument

class Person {
    String name;
    Integer age;
}

Optional optional = Optional.of(new Person(name: "lilong",age: 25))
println optional.isPresent()    // true
println optional.orNull().name  //lilong
println optional.get().name //lilong

println Strings.emptyToNull("")==null && Strings.emptyToNull("abc").equals("abc")
println Strings.isNullOrEmpty("")&&Strings.isNullOrEmpty(null)
println Strings.nullToEmpty(null).length()


//precondition

//下面的方案非常一般，但为什么不呢？
int i = -1
try{
    checkArgument(i >= 0, "Argument was %s but expected nonnegative", i);
}catch (IllegalArgumentException e){
    println e.getMessage()
}


//Ordering
Ordering<String> byLengthOrdering = new Ordering<String>() {
    public int compare(String left, String right) {
        return Ints.compare(left.length(), right.length());
    }
};

//if (Ordering.from(comparator).reverse().isOrdered()) { ... }


println ComparisonChain.start().compare(1,1).result()

println Objects.toStringHelper(new Person()).add("age",10).toString()




