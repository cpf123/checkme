
<#--重复值检查 -->
<#--① 关键字段出现重复记录，比如主索引字段出现重复；范围小-->
<#--② 所有字段出现重复记录。范围大-->

<#--重复值的产生主要有两个原因，-->
<#--一是上游源数据造成的，-->
<#--二是数据准备脚本中的数据关联造成的。-->
<#--从数据准备角度来看，首先检查数据准备的脚本，判断使用的源表是否有重复记录，-->
<#--同时检查关联语句的正确性和严谨性，比如关联条件是否合理、是否有限定数据周期等等。-->
<#--比如：检查源表数据是否重复的SQL：-->

select a.cnt_all,
       a.cnt_aggr,
       if(a.cnt_all=a.cnt_aggr,true,false) as is_distinct
  from (select count(0) as cnt_all,
               count(distinct ${dml.aggregate_col}) as cnt_aggr
          from ${table.TABLE_SCHEMA}.${table.TABLE_NAME}
         where dt=date_add(current_date,-1)
       ) a;

<#--有重复值的id明细-->
select ${dml.aggregate_col}
  from ${table.TABLE_SCHEMA}.${table.TABLE_NAME}
 where dt=date_add(current_date,-1)
 group by ${dml.group_col}
having count(${dml.aggregate_col})>1
;