
<#--ddl mysql 转 hive-->

drop table if exists ${table.TABLE_SCHEMA}.${table.TABLE_NAME};
create table if not exists ${table.TABLE_SCHEMA}.${table.TABLE_NAME}(
${table.LIST_COLUMN}
)
comment '${table.TABLE_COMMENT}'
partitioned by(dt string)
row format delimited fields terminated by '\001' STORED AS TEXTFILE;


<#--<#list table.LIST_COLUMN_NAME as COLUMN_NAME>-->
<#--${COLUMN_NAME}-->
<#--</#list>-->