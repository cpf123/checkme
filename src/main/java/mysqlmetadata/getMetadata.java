package mysqlmetadata;

import utils.JDBCUtil;

import java.sql.*;

public class getMetadata {
    private static Connection con = null;
    private static final PreparedStatement psta = null;
    private static final ResultSet res = null;
    String columnName;
    String columnType;
    private static final String database = "test";
    private static final String table = "company_financing_info";

    public static void main(String[] args) {
        new getMetadata().getColumn();
//        new MysqlMetadata.getMetadata().getPrimaryKey();
//        new MysqlMetadata.getMetadata().getUniqueIndex();
//        new MysqlMetadata.getMetadata().getAllIndex();
//        new MysqlMetadata.getMetadata().getforeignKey();
    }

    /**
     * catalog : 类别名称，因为存储在此数据库中，所以它必须匹配类别名称。该参数为 “” 则检索没有类别的描述，为 null 则表示该类别名称不应用于缩小搜索范围
     * schema : 模式名称，因为存储在此数据库中，所以它必须匹配模式名称。该参数为 “” 则检索那些没有模式的描述，为 null 则表示该模式名称不应用于缩小搜索范围
     * table : 表名称，因为存储在此数据库中，所以它必须匹配表名称
     * unique : 该参数为 true 时，仅返回惟一值的索引；该参数为 false 时，返回所有索引，不管它们是否惟一
     * approximate : 该参数为 true 时，允许结果是接近的数据值或这些数据值以外的值；该参数为 false 时，要求结果是精确结果
     *
     * @return
     */
    public ResultSet getColumn() {
        try {
            con = JDBCUtil.getConnection();
            DatabaseMetaData dm = con.getMetaData();
//            System.out.println(con.getCatalog()); // 当前连接的数据库
            ResultSet colRet = dm.getColumns(database, null, table, null);
            while (colRet.next()) {
                columnName = colRet.getString("COLUMN_NAME"); //列名称
                columnType = colRet.getString("TYPE_NAME");//字段数据类型
                String data_type = colRet.getString("DATA_TYPE");//来自 java.sql.Types 的 SQL 类型
                int datasize = colRet.getInt("COLUMN_SIZE");//列的大小
                int digits = colRet.getInt("DECIMAL_DIGITS");//小数部分的位数
                int nullable = colRet.getInt("NULLABLE");//是否允许使用 NULL。
                String collation_name = colRet.getString("REMARKS");//列注释
                String column_def = colRet.getString("COLUMN_DEF");//列默认值
                System.out.println(column_def + " " + columnName + " " + collation_name + " " + columnType + " " + datasize + " " + digits + " " + nullable);
            }
//            ResultSetMetaData md = colRet.getMetaData();//返回结果集的元数据
//            while (colRet.next()) {
//                for (int i = 1; i <= md.getColumnCount(); i++) {
//                    System.out.println(md.getColumnName(i) + "==" + colRet.getObject(i));
//                }
////                String column_name = colRet.getString("COLUMN_NAME");// 索引对应的列名
////                String index_name = colRet.getString("INDEX_NAME");// 索引
////                String type = colRet.getString("NON_UNIQUE");// 索引不唯一：true
////                System.out.println(column_name+" "+index_name+" "+type);
//
//            }
            return colRet;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null)
                    res.close();
                if (psta != null)
                    psta.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ResultSet getTables() {
        try {
            con = JDBCUtil.getConnection();
            DatabaseMetaData dm = con.getMetaData();
            ResultSet tableSet = dm.getTables(database, database, null, new String[]{"TABLE"});

            while (tableSet.next()) {//循环输出数据库中的表名
                System.out.println(tableSet.getString("TABLE_NAME"));

            }
            return tableSet;
//            System.out.println("获取指定的数据库的所有表的类型");
//            ResultSet rs1 = dm.getTables(database, null, null, null);
//            while (rs1.next())
//            {
//                System.out.println();
//                System.out.println("数据库名:"+ rs1.getString(1));
//                System.out.println("表名: "+rs1.getString(3));
//                System.out.println("类型: "+rs1.getString(4));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null)
                    res.close();
                if (psta != null)
                    psta.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ResultSet getPrimaryKey() {
        try {
            con = JDBCUtil.getConnection();
            DatabaseMetaData dm = con.getMetaData();
            ResultSet primaryKeyResultSet = dm.getPrimaryKeys(database, null, table);
            while (primaryKeyResultSet.next()) {
                String primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");
                String pk_name = primaryKeyResultSet.getString("PK_NAME");
                System.out.println(primaryKeyColumnName + " " + pk_name);
            }
            return primaryKeyResultSet;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null)
                    res.close();
                if (psta != null)
                    psta.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ResultSet getforeignKey() {
        try {
            con = JDBCUtil.getConnection();
            DatabaseMetaData dm = con.getMetaData();
            dm.getIndexInfo(null, database, table, true, false);//仅返回惟一值的索引
            ResultSet foreignKeyResultSet = dm.getImportedKeys(database, null, table);
            while (foreignKeyResultSet.next()) {
                String fkColumnName = foreignKeyResultSet.getString("FKCOLUMN_NAM");
                String pkTablenName = foreignKeyResultSet.getString("PKTABLE_NAME");
                String pkColumnName = foreignKeyResultSet.getString("PKCOLUMN_NAME");
            }
            return foreignKeyResultSet;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null)
                    res.close();
                if (psta != null)
                    psta.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ResultSet getUniqueIndex() {
        try {
            con = JDBCUtil.getConnection();
            DatabaseMetaData dm = con.getMetaData();
            ResultSet indexInfoResultSet = dm.getIndexInfo(null, database, table, true, false);//仅返回惟一值的索引
            ResultSetMetaData md = indexInfoResultSet.getMetaData();//返回结果集的元数据
            while (indexInfoResultSet.next()) {
//                for (int i = 1; i <= md.getColumnCount(); i++) {
//                    System.out.println(md.getColumnName(i) + "==" + indexInfoResultSet.getObject(i));
//                }
                String column_name = indexInfoResultSet.getString("COLUMN_NAME");// 索引对应的列名
                String index_name = indexInfoResultSet.getString("INDEX_NAME");// 索引
                String type = indexInfoResultSet.getString("NON_UNIQUE");// 索引不唯一：true
                System.out.println(column_name + " " + index_name + " " + type);

            }
            return indexInfoResultSet;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null)
                    res.close();
                if (psta != null)
                    psta.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ResultSet getAllIndex() {
        try {
            con = JDBCUtil.getConnection();
            DatabaseMetaData dm = con.getMetaData();
            ResultSet indexInfoResultSet = dm.getIndexInfo(null, database, table, false, false);//仅返回不惟一值的索引 精确值
            ResultSetMetaData md = indexInfoResultSet.getMetaData();//返回结果集的元数据
            while (indexInfoResultSet.next()) {
//                for (int i = 1; i <= md.getColumnCount(); i++) {
//                    System.out.println(md.getColumnName(i) + "==" + indexInfoResultSet.getObject(i));
//                }
                String column_name = indexInfoResultSet.getString("COLUMN_NAME");// 索引对应的列名
                String index_name = indexInfoResultSet.getString("INDEX_NAME");// 索引
                String type = indexInfoResultSet.getString("NON_UNIQUE");// 索引不唯一：true
                System.out.println(column_name + " " + index_name + " " + type);

            }
            return indexInfoResultSet;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null)
                    res.close();
                if (psta != null)
                    psta.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
