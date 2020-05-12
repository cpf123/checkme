import utils.JDBCUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class TestGetTableApp {
    public static void main(String args[]) {
        Connection con = null;
        PreparedStatement psta = null;
        ResultSet res = null;
        String columnName;
        String columnType;
        String database = "fox";
        String table = "channel";

        try {
            con = JDBCUtil.getConnection();
            DatabaseMetaData dm = con.getMetaData();
//            System.out.println(con.getCatalog()); // 当前连接的数据库
            ResultSet tableSet = dm.getTables(database, database, null, new String[]{"TABLE"});
            while (tableSet.next()) {//循环输出数据库中的表名
                System.out.println(tableSet.getString("TABLE_NAME"));

            }

//            System.out.println("获取指定的数据库的所有表的类型");
//            ResultSet rs1 = dm.getTables(database, null, null, null);
//            while (rs1.next())
//            {
//                System.out.println();
//                System.out.println("数据库名:"+ rs1.getString(1));
//                System.out.println("表名: "+rs1.getString(3));
//                System.out.println("类型: "+rs1.getString(4));
//            }

            ResultSet colRet = dm.getColumns(database, null, table, null);
            while (colRet.next()) {//循环输出EMP表的列名以及其他信息
                columnName = colRet.getString("COLUMN_NAME");
                columnType = colRet.getString("TYPE_NAME");
                int datasize = colRet.getInt("COLUMN_SIZE");
                int digits = colRet.getInt("DECIMAL_DIGITS");
                int nullable = colRet.getInt("NULLABLE");
                String collation_name = colRet.getString("REMARKS");
//                String column_key = colRet.getString("PK_NAME");
                System.out.println(columnName + " " + collation_name + " " + columnType + " " + datasize + " " + digits + " " + nullable);
            }
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
    }

}