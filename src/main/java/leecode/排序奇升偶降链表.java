package leecode;
import java.util.*;
public class 排序奇升偶降链表 {

    /*
     * public class ListNode {
     *   int val;
     *   ListNode next = null;
     *   public ListNode(int val) {
     *     this.val = val;
     *   }
     * }
     */

    public class Solution {
        /**
         * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
         * @param head ListNode类
         * @return ListNode类
         */

        public ListNode sortLinkedList (ListNode head) {
            // write code here
            if(head == null || head.next == null) {
                return head;
            }
            // 先把奇数位链表和偶数位链表拆开
            ListNode oddCur = head; // 1
            ListNode evenCur = oddCur.next;// 2
            ListNode oddHead = oddCur;
            ListNode evenHead = evenCur;
            while(evenCur != null){
                oddCur.next = evenCur.next;// 1
                if(oddCur.next != null) {
                    evenCur.next = oddCur.next.next; //2
                }
                // die dai
                oddCur = oddCur.next;
                evenCur = evenCur.next;
            }
            // 然后把偶数位链表逆序
            evenHead = reverseList(evenHead);
            // 最后把两个升序的链表归并
            return mergeList(oddHead, evenHead);
        }

        // 反转链表
        private ListNode reverseList(ListNode head) {
            if(head == null) {
                return head;
            }
            ListNode prev = null;
            ListNode cur = head;
            while(cur != null){
                ListNode next = cur.next;
                cur.next = prev;
                prev = cur; // 遍历
                cur = next; // 遍历
            }
            return prev;
        }

        // 合并两个有序链表
        private ListNode mergeList(ListNode head1, ListNode head2) {
            ListNode dummy = new ListNode(-1);
            ListNode cur = dummy;
            while(head1 != null && head2 != null){
                if(head1.val <= head2.val){
                    cur.next = head1;
                    head1 = head1.next;
                }else{
                    cur.next = head2;
                    head2 = head2.next;
                }
                cur = cur.next;
            }
            while(head1 != null){
                cur.next = head1;
                head1 = head1.next;
                cur = cur.next;
            }
            while(head2 != null){
                cur.next = head2;
                head2 = head2.next;
                cur = cur.next;
            }
            return dummy.next;
        }
    }
}
