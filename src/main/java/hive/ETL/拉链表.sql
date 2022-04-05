-- 维度表
CREATE EXTERNAL TABLE ods.user_update (
  user_num STRING COMMENT '用户编号',
  mobile STRING COMMENT '手机号码',
  reg_date STRING COMMENT '注册日期'
COMMENT '每日用户资料更新表'
PARTITIONED BY (dt string)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/ods/user_update';
)

-- 拉链表
CREATE EXTERNAL TABLE dws.user_his (
  user_num STRING COMMENT '用户编号',
  mobile STRING COMMENT '手机号码',
  reg_date STRING COMMENT '用户编号',
  t_start_date ,
  t_end_date
COMMENT '用户资料拉链表'
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/dws/user_his';
)

-- dml
INSERT OVERWRITE TABLE dws.user_his
SELECT * FROM
(
    SELECT A.user_num,
           A.mobile,
           A.reg_date,
           A.t_start_time,
           CASE
                WHEN A.t_end_time = '9999-12-31' AND B.user_num IS  NULL THEN '2017-01-01'
                ELSE A.t_end_time --
           END AS t_end_time
    FROM dws.user_his AS A          -- 拉链表
    LEFT JOIN ods.user_update AS B  -- 维度表  情况一：维度表端有数据 有效 情况二：维度表端无数据 字段失效 备注失效时间
      ON A.user_num = B.user_num
UNION                              -- 合并  新增的改变的维度字段
    SELECT C.user_num,
           C.mobile,
           C.reg_date,
           '2017-01-02' AS t_start_time,
           '9999-12-31' AS t_end_time
    FROM ods.user_update AS C
) AST