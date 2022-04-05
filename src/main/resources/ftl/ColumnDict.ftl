
<#--获取某个字段的枚举值 字典-->

select distinct ${dml.aggregate_col}
  from ${table.TABLE_SCHEMA}.${table.TABLE_NAME}
 where dt=date_add(current_date,-1)
 group by ${dml.group_col}
 order by ${dml.group_col};

