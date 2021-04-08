### 配置表
#### common
配置|默认值|说明或用途
---|---|---
program.language|zh|设定程序语言
profiles.active| |多配置文件切换，如设为prd，则使用application-prd.properties，默认使用application.properties
print.alignment|center|打印对齐方式，可取left（-1）、center（0）、right（1）
print.border.flank|true|表格打印是否需要侧边框
print.border.transverse|true|表格打印是否需要行与行之间的分割线
print.border.vertical|true|表格打印是否需要列与列之间的分割线
print.cover|true|HBase行输出是否启用覆盖模式
print.explode|true|是否将含有换行符的文本换行显示
print.format|default|数据打印方式，default未表格输出
print.json.pretty|false|是否美化json输出
print.length|0|打印行数，0表示全部打印
print.linefeed|0|强制拆分长文本，0表示不拆分
print.padding|true|是否填充空白符以对齐
print.pageSize|20|每页打印行数
print.null2empty|false|是否将null值转为空字符串
print.render|0;32|打印颜色渲染
print.truncate|false|是否将长文本缩略显示
print.truncate.length|17|缩略为17个字符和...
mail.smtp.host| |邮件发件服务器地址
mail.smtp.port|25|邮件发件服务器端口
mail.sender.username| |邮件发件人用户名
mail.sender.password| |邮件发件人密码
mail.sender.name| |邮件发件人显示名称
mail.recipients| |邮件收件人

#### bigdata
配置|默认值|说明或用途
---|---|---
hbase.master| |hbase master地址
hdfs.enabled|true|是否启用hdfs作为存储
hdfs.ha|true|hdfs是否开启高可用
hdfs.namenode.address| |hdfs未开启高可用，提供namenode地址
hdfs.namenodes| |hdfs开启高可用，提供namenodes地址
hdfs.nameservice| |hdfs开启高可用，提供nameservice名称
hdfs.port| |hdfs端口
kafka.brokers| |kafka broker地址
kafka.consumer.poll.ms| |kafka消费者拉取间隔
kafka.group.id| |kafka消费者使用的订阅ID
kafka.maxRatePerPartition| |设定kafka消费者每个partition每秒钟拉取的数据条数
kafka.offset.config| |kafka offset配置，可选earliest、latest、none
kafka.topics| |kafka topics
redis.cluster.enabled|false|
redis.host|localhost|
redis.keyset| |
redis.port|6379|
redis.struct|list|
redis.timeout|200|
yarn.rm.address|localhost:8088|
spark.app.name|spark-application|
spark.master|local|
spark.streaming.seconds| |spark streaming拉取间隔
zookeeper.connection| |zookeeper地址（含端口）
zookeeper.port|2181|
zookeeper.quorum| |zookeeper地址