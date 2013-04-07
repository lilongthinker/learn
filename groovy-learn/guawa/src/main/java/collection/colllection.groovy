package collection

import com.google.common.base.Function
import com.google.common.collect.ImmutableListMultimap
import com.google.common.collect.ImmutableSet
import com.google.common.collect.Lists
import com.google.common.collect.Multimaps

/**
 * User: dingyue
 * Date: 13-3-14
 */
List<String> strs = new ArrayList<String>();
strs.add("one");
strs.add("two")
strs.add("three")
strs.add("four")
strs.add("five")
strs.add("sixt")

ImmutableSet<String> digits = ImmutableSet.copyOf(strs)

Function<String,Integer> lengthFunction = new Function<String,Integer>(){
    @Override
    Integer apply(String input) {
        return input.length()
    }
}
ImmutableListMultimap<Integer,String> digitsByLength = Multimaps.index(digits,lengthFunction)

println digitsByLength
//output:
//        {3=[one, two], 5=[three], 4=[four, five, sixt]}

interface PDO {
    Long getCustId();
}

class AdgroupDO implements PDO {
    Long custId;
}

List<AdgroupDO> adgroupList = [
        new AdgroupDO(custId: 1),
        new AdgroupDO(custId: 1),
        new AdgroupDO(custId: 2),
        new AdgroupDO(custId: 3)
]

Function<PDO,Long> pdoGroupByCustIdFunction = new Function<PDO, Long>() {
    @Override
    Long apply(PDO input) {
        return input.custId
    }
}
////////////////////////////////////////group by ////////////////////////////////////
ImmutableListMultimap<Long,AdgroupDO> adgroupMap = Multimaps.index(ImmutableSet.copyOf(adgroupList),pdoGroupByCustIdFunction);

println adgroupMap
//output:
//        {1=[collection.AdgroupDO@f8395f, collection.AdgroupDO@7e9ce2], 2=[collection.AdgroupDO@6dbdc9], 3=[collection.AdgroupDO@c6eff5]}





////////////////////////////////transform
List<Long> custIdList  = Lists.transform(adgroupList, new Function<AdgroupDO, Long>() {//此处可以与上面使用同一个函数
    @Override
    Long apply(AdgroupDO input) {
        return input.getCustId();
    }
});

println custIdList         //output: [1, 1, 2, 3]


