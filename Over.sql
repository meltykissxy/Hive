--  开窗的规则:
-- 	在每个窗口中，会为每一条数据都开一个窗口
-- 	over(): 窗口的大小就是当前查询到的结果集的大小
-- 	over(partition by) :  窗口的大小就是每个分区中结果集的大小
-- 	over(order by ):  窗口的大小就是从结果集的开始位置到当前行
-- 	over(partition by order by):窗口的大小就是每个分区中从结果集的开始位置到当前行
--  查询在2017年4月份购买过的顾客及总人数
select
    name,
    count(*) `购买次数`,
    count(*) over() as `总人数`
from
    business
where
    month(orderdate) = '04'
    and
    year(orderdate) = '2017'
group by
    name;
-- 查询顾客的购买明细及月购买总额
select
    name,
    orderdate,
    cost,
    sum(cost) over(partition by month(orderdate)) as total_amount
from
    business;
-- 上述的场景, 将所有顾客的cost按照日期进行累加
select
    name,
    orderdate,
    cost,
    sum(cost) over(order by orderdate) sum_cost
from
    business;
-- 查看顾客上次的购买时间
select name,
       orderdate,
       cost,
       lag(orderdate,1,'1900-01-01') over(partition by name order by orderdate ) as prev_buy
from business;
-- 查询前20%时间的订单信息
select
    *
from
(
    select
        name,
        orderdate,
        cost,
        ntile(5) over(order by orderdate) gid
    from
        business
) t
where
    gid = 1;
