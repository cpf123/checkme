package 框架;

import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.ScriptEvaluator;

public class JaninoTester02 {

    public static void main(String[] args) {
        try {
            String express = "(1+2)*3";
            ScriptEvaluator evaluator = new ExpressionEvaluator();
            evaluator.cook(express);
            Object res = evaluator.evaluate(null);
            System.out.println(express + "=" + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}