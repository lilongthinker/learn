#learn
=====

##something-about-sem 
the sem ppt

##git的一些命令
git push; put all the local responsoty to the remote
##设计工具需求畅想
幻想这么一种工具 用 纯文本来描述图形就像markdown这种语言一样，数据和展现分开，这样的好处
* 图形的维护不是那么的复杂了
* 多人维护同一个图形（设计）文档成为一种可能（虽然EA已经支持这种类似的东西，但是它的文档描述实在是太复杂了）

##GPS工具的另类应用
GPS可以提供这么一种东西，可以通过授权，让指定的人实时知道自己的位置
* 提供权限，可以被对方发现地里位置 －－ 针对妻子
* 收费项目，可以将自己的位置设置为指定的地理位置 －－ 针对老公
* 可以针对这点来防止孩子丢失 －－ 针对看管小孩子的人

##groovy unit code
    /**
    * 获取当前时间
    */
    protected static Date now() {
        return new Date();
    }
    /**
    * 将指定字符串重复若干遍，并返回重复后的结果
    */
    def timesString(def times, def ch) {
        StringBuilder stringBuilder = new StringBuilder()
        times.times { stringBuilder.append(ch) }
        return stringBuilder.toString()
    }
    
    
    

    
