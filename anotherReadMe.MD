#参考文档
1. 文档首页 http://dba.taobao.net:9999/wiki/index.php/DRC_INDEX
2. 客户端文档 http://dba.taobao.net:9999/wiki/index.php/DRCClient_self

#项目文档

1. 项目首页 http://twiki.corp.taobao.com/bin/view/Taobao_engine_p4p/AdProjNewDisp?spm=0.0.0.0.QJ9Pfp

#现在存在的问题
2. 多机房的实现方案                        需要与#道济#确认方案* 已实现,待测试
3. >*日志级别的问题  需要平衡日志的量和运维时排查问题的依据,目前在trans里存在大量的重复记录日志*
4. >*同一个事务数据合并的逻辑，是否会有顺序问题,目前的合并(针对相同表，相同操作类型，相同的objectFieldName值)是直接拿后面的数据覆盖前面的数据
5. 类目变更，原系统发送300和600消息，目前知发送300消息，但会在变更字段里面列出类目 －－ 不知道这种发送方式能否满足新系统要求 需要sqlbolt来自行判定类目变更*；据道济反馈目前已经停用600消息
6. >*sqlbolt要考虑lunaactivitybidword里面的数据的变更 在lunaactivitybidword导致的300消息里面添加flag字段表明其是活动宝贝;已经解决，统一都查一次活动词表
7. 目前的failover只实现了一个活跃节点的维护，需要把节点个数设置为可配置;  该需求本期不予实现；已经实现了
8. dispatcher客户端使用direct memory的问题



#上线注意事项
1. manager.host 需要按照线上的环境来进行配置，负责人是drc的pe （杰睿）
2. 将线上的数据库的信息在DRC端进行注册，然后根据拿到的结果配置trans.drc.groupname，和trans.dbs(数据库名字列表)
3. 配置正确的config server地址。
4. 申请客户端所在组，获得分组名(group), 和密码(password)
5. trans运行机器应在drc服务的ip白名单里面
5. 提供所需分库名(database)
6. 正确配置需要表(table)和字段名(column)


#运维注意事项
0. 需要注意的用例 当数据库fall down，程序只要不重启，就不会出问题。
1. 在TDDL主备切换的时候，DRC需要同时切换，否则，drc从主库读取binlog，strorm通过备库取数据会存在时间差，会导致db和引擎的数据不一致
3. drc是根据数据库的名字来进行监控的，一定要保证配置文件里数据库名字的列表和所要监控的数据库的名字一致
4. 运维模式
    1. 设置manual_starting_point的值为某一时间，运行程序；程序运行后manual_starting_point字段应该为空，heartbeat_time值为最新的一次心跳的时间
    2. 设置manual_starting_point的值为空，heartbeat_time值为某一时间－－时间A，程序运行后，会从时间A开始追消息
    3. 没有db_name+cluster_name对应的记录, 则程序以manual_starting_point为空，startpoint来运行
    4. 设置manual_starting_point的值为空，heartbeat_time值为空，则程序会从当前时间开始追消息，但是不会在heartbeat_time记录最新的一条消息的时间，这种模式不符合程序运行的要求；
5. 关于追消息的问题
    通过修改manual_starting_point 可实现追消息的功能
    其值最早是3天内，这个取决于dba删除binlog的时间，DRC与dba的初步沟通binlog保留时间为3～7天
6. trans节点在运行状态时会开启一个固定的端口（默认配置是6090），pe需要在所有的trans节点上监控该端口，有且仅有1台机器启动该端口说明程序运行状态正常，否则表明程序出问题了.
7. tps的限制
    目前tps的限制主要是单个的drc客户端对单个库的tps限制在6000～8000 TPS；目前数据库的tps大约在 150 ～ 300tps


#常见错误

问题１：

  errorlog

>05-07 10:45:55  INFO thread-drc-subway_dev_p22-cm3 - [SubwayMessageListener.java:notifyRuntimeLog:355]- [ERROR] dbName=subway_dev_p22,[Error] Bad http response Server 10.232.41.39:25993/task/start internal error: ERROR Seek timestamp error got by task ClusterUT-subway_dev_p22-0000167072 got by task ClusterUT-subway_dev_p22-0000167072
>05-07 10:45:55 ERROR thread-drc-subway_dev_p22-cm3 - [SubwayConsumeThread.java:uncaughtException:233]- thead thread-drc-subway_dev_p22-cm3 exception###########################
>java.lang.RuntimeException: com.taobao.drc.client.HttpBadResponseException: Server 10.232.41.39:25993/task/start internal error: ERROR Seek timestamp error got by task ClusterUT-subway_dev_p22-0000167072
>Caused by: com.taobao.drc.client.HttpBadResponseException: Server 10.232.41.39:25993/task/start internal error: ERROR Seek timestamp error got by task ClusterUT-subway_dev_p22-0000167072

  原因：时间不在数据库能够监控的范围内

问题２：

  errorlog

>05-17 16:50:52  INFO thread-zkFailover-EventThread - [SubwayMessageListener.java:notifyRuntimeLog:381]- [INFO] dbName=subway_dev_g,Service stopped.
>java.lang.NullPointerException
>at com.taobao.drc.client.impl.DRCClientImpl.run(DRCClientImpl.java:565)
>at java.lang.Thread.run(Thread.java:662)

  原因:

问题３：

  errorlog

>    status: Avaiable
 
  原因:

#code review的问题和答案
1. transDataFilterMap的目的 提供表和字段的过滤（白名单）
2. 监控表的白名单和黑名单       目前只有白名单
3. 监控字段的白名单和黑名单     目前只有白名单
8. zookeeper标识不同的服务集群 通过最初创建的永久目录的名字来区分不同的应用
1. 表过滤的实现在哪里？
2. 当前记录的时间戳
3. 当trans节点失去与zookeeper Server的联系后，如何处理？ 这种情况pe监控通过监控在业务逻辑中启动端口，如果该端口启动的机器数超过1，则触发报警. 与道济确认是否有别的方案用来做监控
4. 日志打印的问题如何处理；包括钱塘江项目里的日志
     1. 线程号
     2. 日志的全量文件
          2.1 日志级别
5. [FINISH]不用configserver的原因
     1. 跨机房的原因导致抖动比zookeeper频繁，而zookeeper是局域网的实现
6. [FINISH]TaskExecutor 里面failoverMap 去掉
7. [FINISH]代码里面添加注释需要补充
     接口的功能
     接口的方法的功能
8. IFailover.close()何时能否执行即TaskExecutor.destroy()方法何时执行 需要验证是否在kill -15 杀掉的时候会否触发执行
9. [FINISH]DistributeNode.close()方法放到 IFailover.close()里面
10. [FINISH]zookeeper的根目录名 /trans-task  放到配置文件里面，上线前需要申请 目前引擎的zookeeper是没有作权限限制，所有节点均有写权限


#一些需要注意的问题
1. drc监测到大事务
    drc会做切分 与@密云 确认
2. 对浮点型数据的精度处理没有问题,因为是按照String来处理的  @佑丹 确认
3. dba批量操作数据库时的消息突增，如何处理？过滤还是？
    DRC目前对于这个事情是不处理的，因为无法确定是手工还是程序的操作，可能是个隐患  @密云 确认
4. 目前trans要求所有的表名的后缀 "_\d{4}"             @鼎岳 确认
5. 关于数据库里的date类型，统一的数据格式:2013-04-02 17:41:25      @佑丹

#改进点
1. [finish]多个节点运行

>public interface DistributeNode {
>    //当前节点进入运行状态
>    void start();//TODO index(当前节点在zookeeper里的排序号),count(zookeeper中节点的个数) 作为参数 可以实现多个节点同时运行的功能，目前的实现只支持一个节点运行

2. [finish]dispatcher的客户端运行的时候，使用了大量的DirectMemory这个有待改进


#部署文档
##1. 依赖环境

    1.1. DRC
        1.1.1 应用的ip在DRC的白名单里面，可以找drc-pe申请
        1.1.2 申请账号 groupname，password （需要与drc-pe确认该账号是拥有你所监控库的权限，否则会HTTP response code: 201 Error: check group auth fail: AdEngine got by task）
    1.2. Zookeeper
        1.2.1 与引擎同学确认zookeeper的地址
        1.2.2 确认在zookeeper有创建永久目录的权限，该永久目录的名字是通过配置项来指定的（该永久目录的名字决定了所有的trans启动后所注册的位置）
        1.2.3 zookeeper要确保在一个机房一组，机房间不要公用，这样我们的trans才能保证在每个机房都有且仅有唯一一个trans节点在正常运行
    1.3. 数据库及对应的表
    ``
    CREATE TABLE `drc_starting_point` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `db_name` varchar(30) NOT NULL,
      `heartbeat_time` bigint(20) NOT NULL,
      `manual_starting_point` datetime DEFAULT NULL,
      `his_manual_starting_point` datetime DEFAULT NULL,
      `gmt_modified` datetime NOT NULL,
      `cluster_name` varchar(50) NOT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=gbk;
    ``
    --manual_starting_point 该字段值大部分时间应该为空值
##2. 安装easy环境
安装rpm包：t_libeasy-1.0.8-131.el5

##2. 安装imatch包
安装rpm包：t-imatch-dispatch2-client-2.0.4-35.el5.x86_64.rpm 

##3. 安装trans
安装rpm报：请获取最新版本

##4. 修改配置
\#机房相关的信息，需要根据机房不同来配置
cluster.name=cm3
trans.zk.host=10.237.8.88:2181,10.237.8.69:2181,10.237.8.70:2181
\#根据线上环境修改 drc服务集群
manager.host=http://yufa.drc.tbsite.net:8080/congo
##5. 启动
sudo -u ads sh /home/a/project/luna_trans/bin/run.sh

#测试的sql
use subway_g;
select * from lunatmpcustomer where custid = 3720583;
delete from lunatmpcustomer where custid = 3720583;
INSERT INTO lunatmpcustomer (CUSTID, NAME, DBKEY, OUTSIDEKEY, OUTSIDESTRID, OUTSIDENUMID, OUTSIDESTATE, LOCATION, IM, NICKNAME, PHONE, EMAIL, AREACD, ORGINDUSTRY, ADDRESS, ZIP, PAYEDFLAG, DELETEFLAG, LEVELCD, TYPECD, LASTSYNCDATE, CREATETIME, LASTUPDATETIME, DAILYLOGINTIME, CUSTSTATUS, REASON, MEMBERID) VALUES (3720583, '测试自动化账户', 'slave2', 'taobao', '3720584', 3720585, 1, '北京', '3720586', '3720587', NULL, NULL, '86', 649, NULL, NULL, 1, 0, 0, 1, '2012-09-05 18:02:35', '2012-09-05 18:02:35', '2013-04-02 11:21:43', '2012-09-05 18:02:35', 0, NULL, NULL);
update lunatmpcustomer set lastupdatetime = now() where custid = 3720583;

use subway_g;
select * from lunaactivitybidword where id = 100004838;
INSERT INTO lunaactivitybidword (ID, ACTIVITYID, CUSTID, ADGROUPID, ORIGINALWORD, CREATETIME, LASTUPDATETIME, CAMPAIGNID, MAXPRICE, ADTYPE) VALUES (100004838, 100004127, 1234456, 200000047648, 'Ip767357F7iKM708502204u450272t', '2011-11-30 17:51:38', '2011-11-30 17:51:38', 13323, 70, 0);
update lunaactivitybidword set lastupdatetime = now() where id = 100004838;
delete from lunaactivitybidword where id = 100004838;


use subway_p13;
select * from lunacampaign where id = 2000959;

delete from lunacampaign where id = 2000959;
INSERT INTO lunacampaign (ID, CUSTID, TITLE, STARTTIME, ENDTIME, ONLINESTATE, MATCHCFG, SETTLESTATE, SETTLEREASON, CREATETIME, LASTUPDATETIME, REALDEDUCTAMT, REALCLICKCOUNT, ENABLESMOOTH, CAMPAIGNTYPE, DEFAULTPRICE, ISCATMATCH, CATPRICE) VALUES (2000959, 1102000960, '默认推广计划', '2009-08-13 02:07:48', '2030-01-01 00:00:00', 1, 3, 1, NULL, '2009-08-13 02:07:48', '2012-10-09 00:00:00', 0, 0, 0, 0, NULL, NULL, NULL);
update lunacampaign set lastupdatetime = now() where id = 2000959;

update lunaplsetting set lastupdatetime = now() where campaignid = 2000959;
update lunatimesetting set lastupdatetime = now() where campaignid = 2000959;
update lunaareasetting set lastupdatetime = now() where campaignid = 2000959;

use subway_p13;

select * from lunaadgroup where id = 56263;
delete from lunaadgroup where id = 56263;
INSERT INTO lunaadgroup (ID, CAMPAIGNID, CUSTID, CATID, DEFAULTPRICE, ONLINESTATE, OUTSIDEKEY, OUTSIDESTRID, OUTSIDENUMID, OUTSIDESTATE, OUTSIDEPRICE, OUTSIDEEXPIRETIME, CREATETIME, LASTUPDATETIME, LASTSYNCDATE, REASON, OFFLINETYPE, NONSEARCHMAXPRICE, NONSEARCHISDEFAULTPRICE, GARBAGESTATE, GARBAGEUPDATETIME, EXTSTATUS, NONSEARCHSTATE) VALUES (56263, 2000959, 1102000960, '50014811 50014920 50015305', 10, 1, 'taobao', 'b753fe3d762c8fa0dd8c75e63af2ba65', 2245899564, 1, '5.00', '2050-01-01 00:00:00', '2009-03-16 22:49:00', '2011-07-28 00:02:06', '2011-07-28 00:00:07', NULL, 0, 50, 0, 0, '2010-08-03 00:09:02', NULL, NULL);
update lunaadgroup set lastupdatetime = now() where id = 56263;


use subway_p00;
INSERT INTO lunabidword_0005 (ID, ADGROUPID, CAMPAIGNID, CUSTID, ORIGINALWORD, NORMALWORD, MAXPRICE, ISDEFAULTPRICE, AUDITSTATE, AUDITTIME, AUDITDESC, CREATETIME, LASTUPDATETIME, ONLINE_FLAG, PLAN_DELETETIME, MATCHSCOPE, CAMPBIDWORDID) VALUES (162560, 22196, 2000117, 1102000133, '哥弟女装', '哥弟女装', 22, 0, 1, NULL, NULL, '2008-10-20 12:21:34', '2013-04-15 11:34:41', 0, NULL, 1, NULL);
select * from lunabidword_0005 where id = 162560;
update lunabidword_0005 set lastupdatetime = now() where id in ( 162560,162573,163001);
delete from lunabidword_0005 where id = 162560


use subway_p00;
select * from lunacreative where id = 2;
INSERT INTO lunacreative (ID, ADGROUPID, CAMPAIGNID, CUSTID, TITLE, DESCRIPTION, DISPLAYURL, LINKURL, IMGURL, ONLINESTATE, CREATETIME, LASTUPDATETIME, AUDITSTATE, AUDITTIME, AUDITDESC, LASTSYNCDATE, SUBTITLE, IMGSYNCFLAG, ELEMENTTID) VALUES (2, 2, 2177228, 1102277487, '图片空间异步', '', 'http://item.taobao.com/item.htm?id=1500001788052', 'http://item.taobao.com/item.htm?id=1500001788052', 'http://img04.daily.taobaocdn.net/imgextra/i1/T2uU8bXmNcXXb1upjX.jpg', 1, '2013-04-15 09:33:27', '2013-04-15 09:33:27', 1, '2013-04-15 09:33:27', '', '2013-04-15 09:33:27', '', 1, NULL);
update lunacreative set lastupdatetime = now() where id = 2;
delete from lunacreative where id = 2;

use subway_p13;
select * from lunacreativeelement where id = 44637454;
INSERT INTO lunacreativeelement (ID, CREATIVEID, CUSTID, CAMPAIGNID, ADGROUPID, CNAME, CVALUE, CTYPE, BIZTYPE, LASTUPDATETIME, LASTSYNCDATE, SYNCFLAG, MEDIAIMGID) VALUES (44637454, 200000046548, 1103723353, 1000035900, 200000057359, 'TITLE', 'kom1', 1, 6, '2012-09-26 14:52:13', '2012-09-26 14:52:13', NULL, NULL);
update lunacreativeelement set lastupdatetime = now() where id = 44637454;
delete from lunacreativeelement where id = 44637454;

use subway_p13;
select * from lunacatmatch where id = 507801;
delete from lunacatmatch where id = 507801;
INSERT INTO lunacatmatch (ID, ADGROUPID, CAMPAIGNID, CUSTID, MAXPRICE, ISDEFAULTPRICE, ONLINESTATE, CREATETIME, LASTUPDATETIME) VALUES (507801, 56402, 2000963, 1102000964, 12, 0, 1, '2009-04-16 15:04:40', '2009-08-13 01:39:03');
update lunacatmatch set lastupdatetime = now() where id = 507801;


#code review
1. http://svn.simba.taobao.com/svn/solar/trans/branches/br_trans-task_20120327 post-review --revision-range r1039:r1125
