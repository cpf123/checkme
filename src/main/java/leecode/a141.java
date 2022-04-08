package leecode;

import java.util.HashSet;
import java.util.Set;

public class a141 {
    public class Solution {
        public boolean hasCycle(ListNode head) {
            Set<ListNode> seen = new HashSet<ListNode>();
            while (head != null) {
                // 如果 Set 集合中不包含要添加的对象，则添加对象并返回   true，否则返回 false。
                if (!seen.add(head)) {
                    return true;
                }
                head = head.next;  // 遍历
            }
            return false;
        }
    }


}
