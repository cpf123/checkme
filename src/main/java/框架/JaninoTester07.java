package 框架;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.IScriptEvaluator;
import org.codehaus.janino.ScriptEvaluator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class JaninoTester07 {
    public static void janino() throws InvocationTargetException, CompileException {
        IScriptEvaluator se = new ScriptEvaluator();
        se.setReturnType(String.class);
        se.cook("import 框架.BaseClass;\n"
                + "import 框架.DerivedClass;\n"
                + "BaseClass o=new DerivedClass(\"1\",\"join\");\n"
                + "return o.toString();\n");
        Object res = se.evaluate(new Object[0]);
        System.out.println(res);
    }
// 在Java脚本中自定义类与调用
    public static void main(String[] args) {

        Thread t = new Thread(()->{
            try {

                while(true) {
                    TimeUnit.SECONDS.sleep(1);
                    janino();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (CompileException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true); // 守护进程
        t.start();
        t.run();

        try {
            IScriptEvaluator se = new ScriptEvaluator();
            se.setReturnType(String.class);
            se.cook("import 框架.BaseClass;\n"
                    + "import 框架.DerivedClass;\n"
                    + "BaseClass o=new DerivedClass(\"1\",\"join\");\n"
                    + "return o.toString();\n");
            Object res = se.evaluate(new Object[0]);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}