package Excel;


import java.io.*;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * 使用例子
 * ReadExcel excel = new ReadExcel("D:\\myexcel.xlsx");
 * String[] firstLine = excel.readLine(0);//得到第一行数据
 * String[] firstLine = excel.readLine(1);//得到第二行数据
 */
public class UA {

    private static String pathname;
    private static Workbook workbook;
    private static Sheet outsheet1;

    private static HSSFSheet hssfSheet;//.xls
    private static XSSFSheet xssfSheet;//.xlsx

    public static void main(String[] args) throws Exception {

/*        String s="Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1 (compatible; Baiduspider-render/2.0; +http://www.baidu.com/search/spider.html)";
        Browser browser = getBrowserUA(s);
        OperatingSystem operatingSystem = getOperatingSystemUA(s);
        UserAgent userAgent = getUA(s);
        System.out.println(browser.getName());*/
        String excel = "/Users/Bloomberg/Downloads/54033187_AAD5A5581FEA91CC375B65B6F021D2B4.out.xlsx";
        UA ua = new UA();
        ua.ReadExcel(excel);
        ua.WriteExcel("/Users/Bloomberg/Downloads/result.xlsx");


        //获取最后一行的num，即总行数。此处从0开始
        int maxRow = xssfSheet.getLastRowNum();
        for (int row = 0; row <= maxRow; row++) {
            //获取最后单元格num，即总单元格数 ***注意：此处从1开始计数***
//            int maxRol = xssfSheet.getRow(row).getLastCellNum();
            XSSFCell cell = xssfSheet.getRow(row).getCell(0);
            String[] strings = readLine(row);
            String s = strings[0];
            Browser browser = getBrowserUA(s);
            OperatingSystem operatingSystem = getOperatingSystemUA(s);
            UserAgent userAgent = getUA(s);


            String[] outlist = new String[10];
            outlist[0] = s;
            outlist[1] = strings[1];
            outlist[2] = (browser.getName());
//            System.out.println(s);
            outlist[3] = (browser.getBrowserType().toString());
            outlist[4] = (browser.getGroup().toString());
            outlist[5] = (browser.getManufacturer().toString());

            outlist[6] = (userAgent.getBrowserVersion() + "");

            outlist[7] = (operatingSystem.getName());
            outlist[8] = (operatingSystem.getGroup().toString());
            outlist[9] = (operatingSystem.getManufacturer().toString());
            System.out.println(outlist[1]);
            write(outlist, row);
        }


    }

    public static UserAgent getUA(String s) {
        return UserAgent.parseUserAgentString(s);
    }

    public static Browser getBrowserUA(String s) {
        UserAgent userAgent = UserAgent.parseUserAgentString(s);
        return userAgent.getBrowser();
    }

    public static OperatingSystem getOperatingSystemUA(String s) {
        UserAgent userAgent = UserAgent.parseUserAgentString(s);
        return userAgent.getOperatingSystem();
    }

    public int getAllRowNumber() {
        return xssfSheet.getLastRowNum();
    }

    /*读取 excel 下标为 rowNumber 的那一行的全部数据*/
    public static String[] readLine(int rowNumber) {
        XSSFRow row = xssfSheet.getRow(rowNumber);
        if (row != null) {
            String[] resultStr = new String[row.getLastCellNum()];
            for (int i = 0; i < row.getLastCellNum(); i++) {
                resultStr[i] = row.getCell(i).toString();
            }
            return resultStr;
        }
        return null;
    }

    public void ReadExcel(String excelPath) throws Exception {
        String fileType = excelPath.substring(excelPath.lastIndexOf(".") + 1, excelPath.length());
        // 创建工作文档对象
        InputStream in = new FileInputStream(excelPath);
        HSSFWorkbook hssfWorkbook = null;//.xls
        XSSFWorkbook xssfWorkbook = null;//.xlsx
        //根据后缀创建读取不同类型的excel
        if (fileType.equals("xls")) {
            hssfWorkbook = new HSSFWorkbook(in);//它是专门读取.xls的
        } else if (fileType.equals("xlsx")) {
            xssfWorkbook = new XSSFWorkbook(in);//它是专门读取.xlsx的
        } else {
            throw new Exception("文档格式后缀不正确!!！");
        }
        /*这里默认只读取第 1 个sheet*/
        if (hssfWorkbook != null) {
            hssfSheet = hssfWorkbook.getSheetAt(0);
        } else if (xssfWorkbook != null) {
            xssfSheet = xssfWorkbook.getSheetAt(0);
        }
    }


    /**
     * 使用栗子
     * WriteExcel excel = new WriteExcel("D:\\myexcel.xlsx");
     * excel.write(new String[]{"1","2"}, 0);//在第1行第1个单元格写入1,第一行第二个单元格写入2
     */
    public static void write(String[] writeStrings, int rowNumber) throws Exception {
        //将内容写入指定的行号中
        Row row = outsheet1.createRow(rowNumber);
        //遍历整行中的列序号
        for (int j = 0; j < writeStrings.length; j++) {
            //根据行指定列坐标j,然后在单元格中写入数据
            Cell cell = row.createCell(j);
            cell.setCellValue(writeStrings[j]);
        }
        OutputStream stream = new FileOutputStream(pathname);
        workbook.write(stream);
        stream.close();
    }

    public void WriteExcel(String excelPath) throws Exception {
        //在excelPath中需要指定具体的文件名(需要带上.xls或.xlsx的后缀)
        pathname = excelPath;
        String fileType = excelPath.substring(excelPath.lastIndexOf(".") + 1, excelPath.length());
        //创建文档对象
        if (fileType.equals("xls")) {
            //如果是.xls,就new HSSFWorkbook()
            workbook = new HSSFWorkbook();
        } else if (fileType.equals("xlsx")) {
            //如果是.xlsx,就new XSSFWorkbook()
            workbook = new XSSFWorkbook();
        } else {
            throw new Exception("文档格式后缀不正确!!！");
        }
        // 创建表sheet
        outsheet1 = workbook.createSheet("sheet1");

    }
}





