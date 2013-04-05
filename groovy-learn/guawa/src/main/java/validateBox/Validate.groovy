package validateBox

import com.taobao.ad.component.validatebox.ValidateBox

/**
 * User: dingyue
 * Date: 13-3-12
 * Time: 上午10:23
 */


ValidateBox vb = new ValidateBox()
        .stringLength("abc", 1, 10, "goodlengthvalidate1")
        .stringLength("", 1, 10, "bad1")
        .stringLength("abc", 1, 10, "goodlengthvalidate2")
        .stringLength("", 1, 10, "bad2")
if(!vb.validate()){
    println vb.getFailMsg()
}

//通用机制总结 （机制 及其缺点） 缓存、权限----异步化机制（高低安全级别的应用）
//单元测试计划
//流程以及相似流程的区别
//

