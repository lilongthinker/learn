package trans

/**
 * User: dingyue
 * Date: 13-3-14
 * Time: 下午4:38
 */

class ConfigTrans {
    static Map<String, String> tableObjctIdFieldMap = new HashMap<String,String>()
    static {
        tableObjctIdFieldMap["LUNAADGROUP"] = "ID"
    }

    public static String getObjctIdField(String tableName) {
        return tableObjctIdFieldMap[tableName]
    }

}

class Operation {
    String tablename// LUNAADGROUP...
    String action //ADD,DELETE,UPDATE
    Long objctId
    String getDistinctKey(){
        return "$tablename~$action~$objctId"
    }
}

String log = "LUNAADGROUP:UPDATE;ID-1,PRICE-1,ONLINESTATE-1,OFFLINETYPE-1"

class Util {
    static Operation parseLog(String log) {
        List<String> stringList = log.split(":")
        String tableName = stringList[0]
        String action = stringList[1].split(";")[0]
        Map<String, Object> attributeMap = new HashMap<>()
        stringList[1].split(";")[1].split(",").each { it ->
            List<String> keyandvalue = it.split("-");
            attributeMap.put(keyandvalue[0], keyandvalue[1])
        }
        Long objId = Long.valueOf(attributeMap.get(ConfigTrans.getObjctIdField(tableName)))
        return new Operation(tablename:tableName,action:action,objctId: objId)
    }
}
Operation operation = Util.parseLog(log)
println operation.getDistinctKey()

Map<String,Operation> map = new HashMap<String,Operation>();
map.put(operation.getDistinctKey(),operation);

println map.values()
