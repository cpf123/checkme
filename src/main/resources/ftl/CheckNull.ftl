
<#-- 空值检查-->

select a.cnt_all,
       a.cnt_null,
       a.cnt_str_null,
       a.cnt_null/a.cnt_all as null_rate,
       a.cnt_str_null/a.cnt_all as str_null_rate
  from (select count(0) as cnt_all,
               count(if(${dml.aggregate_col} is null,1,null)) as cnt_null,
               count(if(${dml.aggregate_col}="",1,null)) as cnt_str_null
          from ${table.TABLE_SCHEMA}.${table.TABLE_NAME}
         where dt=date_add(current_date,-1)
        ) a;

