package leecode;

import java.util.Deque;
import java.util.LinkedList;

public class a227 {

    public static void main(String[] args) {
        String a="30+2*2+";
        int calculate =  new Solution().calculate(a);
        System.out.println(calculate);
    }
   static class Solution {
        public int calculate(String s) {
            Deque<Integer> stack = new LinkedList<Integer>(); // Deque
            char preSign = '+';
            int num = 0;
            int n = s.length();
            for (int i = 0; i < n; ++i) {
                 char cha= s.charAt(i);
                if (Character.isDigit(cha)) { // 数字情况
                    num = num * 10 + cha - '0';
                }
                if (!Character.isDigit(cha) && cha != ' ' || i == n - 1) {
                    switch (preSign) { //yufa  preSign 出错
                        case '+':
                            stack.push(num); // num s.charAt(i) 上一轮num
                            break;  // !!!!!!chucuo
                        case '-':
                            stack.push(-num);
                            break;
                        case '*':
                            stack.push(stack.pop() * num);
                            break;
                        default:
                            stack.push(stack.pop() / num);
                    }
                    preSign = cha; // preSign 是 cha 上一轮 符号
                    num = 0;
                }
            }
            int ans = 0;
            while (!stack.isEmpty()) { // while
                ans += stack.pop();
            }
            return ans;
        }
    }

}
