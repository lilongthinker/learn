package collection

import com.google.common.base.Function
import com.google.common.collect.ImmutableListMultimap
import com.google.common.collect.ImmutableSet
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

interface PDO {
    Long getCustId();
}

class AdgroupDO implements PDO {
    Long custId;
}

List<AdgroupDO> list = [new AdgroupDO(custId: 1),new AdgroupDO(custId: 1),new AdgroupDO(custId: 2),new AdgroupDO(custId: 3)]

Function<PDO,Long> pdoGroupByCustIdFunction = new Function<PDO, Long>() {
    @Override
    Long apply(PDO input) {
        return input.custId
    }
}

ImmutableListMultimap<Long,AdgroupDO> adgroupMap = Multimaps.index(ImmutableSet.copyOf(list),pdoGroupByCustIdFunction);

println adgroupMap


