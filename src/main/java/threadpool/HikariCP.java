package threadpool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.text.MessageFormat;

/**
 * mysql数据库连接池
 */
public class HikariCP {
    private static HikariDataSource getDataSource() throws SQLException {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false");
        config.setUsername("root");
        config.setPassword("root");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
    }

    public static void main(String[] args) throws SQLException {
        Add_Demo();
        ResultSet resultSet = simpleQuery("select * from test01;");
        System.out.println(resultSet.toString());
        printResultSet(resultSet);
    }
    /* 简单的新增 */
    public static int Add_Demo() {
        int result = -1;
        try {
            HikariDataSource dataSource = getDataSource();
            Connection connection = dataSource.getConnection();

            Statement statement = connection.createStatement();

            statement.execute("insert test01 values(NULL,9,1);", Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet != null) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }

            if (connection != null && !connection.isClosed())
                connection.close();
            if (dataSource != null && !dataSource.isClosed())
                dataSource.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /* 更新 */
    public static int update_dmeo() {
        int result = -1;
        try {

            HikariDataSource dataSource = getDataSource();
            Connection connection = dataSource.getConnection();

            Statement statement = connection.createStatement();
            result = statement.executeUpdate("update user set username = 'changed' where userid = 4");

            if (connection != null && !connection.isClosed())
                connection.close();
            if (dataSource != null && !dataSource.isClosed())
                dataSource.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ResultSet simpleQuery(String sql) {
        try {
            HikariDataSource dataSource = getDataSource();
            Connection connection = dataSource.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (connection != null && !connection.isClosed())
                connection.close();
            if (dataSource != null && !dataSource.isClosed())
                dataSource.close();

            return resultSet;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void printResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            ResultSetMetaData md = resultSet.getMetaData();// 获取键名
            int columnCount = md.getColumnCount();// 获取行的数量

            while (resultSet.next()) {
                StringBuilder builder = new StringBuilder();

                for (int i = 1; i <= columnCount; i++) {
                    String colName = md.getColumnName(i);
                    Object val = resultSet.getObject(i);

                    builder.append(MessageFormat.format("{0}:{1}", colName, val));
                }

                print(builder.toString());
            }
        }
    }

    public static void print(Object obj) {
        System.out.println(obj);
    }
}
