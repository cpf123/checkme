package leecode;

public class a122 {
    public static void main(String[] args) {

  int[] tmp= {7,1,5,3,6,4};
        int i = new Solution().maxProfit(tmp);
        System.out.println(i);
    }
   static class Solution {
        public int maxProfit(int[] prices) {
            int res=-1;
            int sum=0;
            for(int i=0; i<prices.length;i++){
                if(res<prices[i] && res!=-1){
                    sum= (prices[i] -res)+sum;
                }
                res=prices[i] ;
            }
            return sum;
        }
    }

    /**
     * 1.定义状态：cash[i]代表没有股票时手里的最大现金
     * hold[i]代表持有股票时手里的最大现金
     *
     * 2.转移方程：
     * 对于没有股票的情况：是不操作现金多，还是把前一天把买的股票的卖掉现金多(只有当后面的价格大于持有的时候才会多，这里相当于买了前一天的股票，然后卖出)？
     * cash[i]=Math.max(cash[i-1],hold[i-1]+prices[i]);
     * 对于手里有股票的情况：是继续持有股票现金多，还是前一天没有股票然后买股票剩下现金多(比如[7,2] 第一天持有股票，hold[0]=-7，第一天没有股票，买第二天股票hold[1]=-2)？
     * hold[i]=Math.max(hold[i-1],cash[i-1]-prices[i]);
     * 3.初始值：
     * cash[0]=0;
     * hold[0]=prices[0]
     * 4.输出值：最后肯定是没有持股票现金多，所以应该是cash[prices.length-1]
     *
     */
    class Solution2 {
        public int maxProfit(int[] prices) {
            int[] cash=new int[prices.length];//cash[i]代表第i天持有的最大现金
            int[] hold=new int[prices.length];//hold[i]代表第i天持有股票时拥有的最大现金

            cash[0]=0;//第零天的时候没有操作
            hold[0]=-prices[0];//买入了一只股票

            for(int i=1;i<prices.length;i++){
                cash[i]=Math.max(cash[i-1],hold[i-1]+prices[i]);//对于没有股票的情况：是不操作现金多，还是把前一天把买的股票的卖掉现金多(只有当后面的价格大于持有的时候才会多，这里相当于买了前一天的股票，然后卖出)？ 是不操作现金多，还是把前一天把买的股票的卖掉现金多
                hold[i]=Math.max(hold[i-1],cash[i-1]-prices[i]);//对于手里有股票的情况：是继续持有股票现金多，还是前一天没有股票然后买股票剩下现金多(比如[7,2] 第一天持有股票，hold[0]=-7，第一天没有股票，买第二天股票hold[1]=-2)？ 是继续持有股票现金多，还是前一天没有股票然后买股票剩下现金多？
            }
            return cash[prices.length-1];
        }
    }


}
