package utils;

import org.quartz.CronExpression;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * crontab 测试
 */
public class QuartzTest {

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    public static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws ParseException {
        Date nextTime = df.parse(df2.format(new Date()) + " 00:00:00");
        Date to = new Date(nextTime.getTime() + 24*3600*1000);
        CronExpression expression;
        List<Date> crontimes = new ArrayList<>();
        expression = new CronExpression("0 50 6 1/1 * ? *");
        for(;nextTime.getTime()<=to.getTime();){
            nextTime = expression.getNextValidTimeAfter(nextTime);
            if(nextTime.getTime()>=to.getTime()) break;
            crontimes.add(nextTime);
        }
        for(int i=0;i<crontimes.size();i++){
            System.out.println(df.format(crontimes.get(i)));
        }
    }

}