package dataclean;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/cookieTest")
public class CookiesGet extends HttpServlet {
    public static void main(String[] args) {
        new CookiesGet();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        boolean flag = false;//首次登陆无lastName cookie
        response.setContentType("text/html;charset=utf-8");
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                //获取cookie的name
                String name = cookie.getName();
                if ("lastName".equals(name)) {
                    flag = true;
                    //1. 获取上次登录的时间
                    String value = cookie.getValue();
                    //1.1 cookie的value进行url解码
                    String decode_value = URLDecoder.decode(value, "utf-8");
                    response.getWriter().write("欢迎回来，上次访问时间是" + decode_value);
                    //2、设置Cookie的value
                    //2.1获取当前的时间值，重新设置cookie的值，并重新发送
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String format_date = simpleDateFormat.format(date);
                    //2.2使用url编码
                    String str_date = URLEncoder.encode(format_date, "utf-8");
//                    2.3 设置cookie的值
                    cookie.setValue(str_date);
                    //2.4 设置cookie的存活时间 一个月
                    cookie.setMaxAge(60 * 60 * 24 * 30);
                    //2.5 写回cookie
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        //首次登录的情况
        if (cookies == null || cookies.length == 0 || flag == false) {
            //1 打印信息
            response.getWriter().write("首次访问，欢迎您");
            //2 获取当前时间
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年mm月dd日 HH：mm:ss");
            String format_date = simpleDateFormat.format(date);
            //3、将新的value使用url编码，写回response
            String str_date = URLEncoder.encode(format_date, "utf-8");
            Cookie cookie = new Cookie("lastName", str_date);
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}

