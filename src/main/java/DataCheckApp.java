import entity.DML;
import entity.MysqlTable;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class DataCheckApp {
    private static final String database = "bdl";
    private static final String tablename = "bdl_qiye_company_staff";

    private static final String aggregate_col = "jdb_id";

    private static final String group_col = "jdb_id";

    private static final String CheckDistinct = "CheckDistinct.ftl";
    private static final String CheckNull = "CheckNull.ftl";
    private static final String ColumnDict = "ColumnDict.ftl";

    public static void main(String[] args) throws Exception {
//        new DataCheckApp().check(database, tablename, aggregate_col, CheckDistinct);
        System.out.print("================");
        new DataCheckApp().check(database, tablename, aggregate_col, CheckNull);
        System.out.print("================");
        new DataCheckApp().check(database, tablename, aggregate_col, group_col, ColumnDict);
        new DataCheckApp().check(database, tablename, aggregate_col, group_col, CheckDistinct);
    }

    public void check(String database, String tablename, String aggregate_col, String template) throws Exception {
        //获取项目根目录路径
        String path = Class.class.getClass().getResource("/").getPath(); // 获取resouce路径
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);

        cfg.setDirectoryForTemplateLoading(new File(path + "/ftl")); //需要文件夹绝对路径
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        Map root = new HashMap();
        MysqlTable mysqltable = new MysqlTable();
        mysqltable.setTABLE_SCHEMA(database);
        mysqltable.setTABLE_NAME(tablename);
        root.put("table", mysqltable);
        DML dml = new DML();
        dml.setAggregate_col(aggregate_col);
        root.put("dml", dml);
        Template temp = cfg.getTemplate(template);
        Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);
    }

    public void check(String database, String tablename, String aggregate_col, String group_col, String template) throws Exception {
        //获取项目根目录路径
        String path = Class.class.getClass().getResource("/").getPath(); // 获取resouce路径
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);

        cfg.setDirectoryForTemplateLoading(new File(path + "/ftl")); //需要文件夹绝对路径
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        Map root = new HashMap();
        MysqlTable mysqltable = new MysqlTable();
        mysqltable.setTABLE_SCHEMA(database);
        mysqltable.setTABLE_NAME(tablename);
        root.put("table", mysqltable);
        DML dml = new DML();
        dml.setAggregate_col(aggregate_col);
        dml.setGroup_col(group_col);
        root.put("dml", dml);
        Template temp = cfg.getTemplate(template);
        Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);
    }
}