# Hive
Hive 实现脱敏以及ETL 过程（开启kerberos）
https://blog.csdn.net/u013850277/article/details/77345882
## 常用函数
[官网](https://cwiki.apache.org/confluence/display/Hive/LanguageManual+UDF)
常用日期函数
current_date：当前日期
current_timestamp：当前的日期加时间
unix_timestamp:返回当前或指定时间的时间戳	
from_unixtime：将时间戳转为日期格式
to_date：抽取日期部分
year：获取年
month：获取月
day：获取日
hour：获取时
minute：获取分
second：获取秒
weekofyear：当前时间是一年中的第几周
dayofmonth：当前时间是一个月中的第几天
months_between： 两个日期间的月份
add_months：日期加减月
datediff：两个日期相差的天数

一定要记住以下几个函数
date_add：日期加天数
date_sub：日期减天数
last_day：日期的当月的最后一天
next_day：日期的下一个周几是哪一天
date_format：按照指定的格式对日期进行格式化
get_json_object：获取json对象中的属性值



常用取整函数
round： 四舍五入
ceil：  向上取整
floor： 向下取整

常用字符串操作函数
upper： 转大写
lower： 转小写
length： 长度
trim：  前后去空格
lpad： 向左补齐，到指定长度
rpad：  向右补齐，到指定长度
regexp_replace： SELECT regexp_replace('100-200', '(\\d+)', 'num') ；
	使用正则表达式匹配目标字符串，匹配成功后替换！

集合操作
size： 集合中元素的个数
map_keys： 返回map中的key
map_values: 返回map中的value
array_contains: 判断array中是否包含某个元素
sort_array： 将array中的元素排序

-- Hive的内置函数
-- 三、日期相关函数
select `current_date`();
select `current_timestamp`();
select unix_timestamp(`current_date`());
select from_unixtime(1606694400);
select to_date(`current_timestamp`());
select year(`current_date`());
select weekofyear(`current_date`());
select dayofmonth(`current_date`());
select datediff(`current_date`(),'2020-10-30');
-- 以下6函数一定要掌握
select date_add(`current_date`(),2);
select date_sub(`current_date`(),5);
select next_day(`current_date`(),'fr');
select last_day(`current_date`());
select date_format(`current_date`(),'yyyy/MM/dd');
select get_json_object('{"id":1,"name":"zhangsan"}','$.name');

-- 取整函数
select round(3.7);//4
select ceil(3.1);//4
select `floor`(3.9);//3

select trim('   hello world    ');
select lpad('hi',6,'*');
select rpad('hi',6,'@');
SELECT regexp_replace('100-200', '(\\d+)', 'num');
select sort_array(split('z-w-d-a','-'));
