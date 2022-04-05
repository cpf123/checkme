package 框架;

import org.codehaus.commons.compiler.IScriptEvaluator;
import org.codehaus.janino.ScriptEvaluator;

public class JaninoTester01 {
    public static void main(String[] args) {
        try {
            String content = "System.out.println(\"Hello world\");";
            IScriptEvaluator evaluator = new ScriptEvaluator();
            evaluator.cook(content);
            evaluator.evaluate(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}