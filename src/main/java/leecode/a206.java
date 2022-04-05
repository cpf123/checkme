package leecode;

public class a206 {
    class Solution {
        public ListNode reverseList(ListNode head) {
            ListNode prev = null;
            ListNode curr = head;
            while (curr != null) {
                ListNode next = curr.next;
                curr.next = prev;
                prev = curr;
                curr = next;
            }
            return prev;
        }
    }

    // 错误：
    public static void main(String[] args) {
        ListNode listNode = new ListNode(0);
        listNode.next = new ListNode(1);
        listNode.next.next = new ListNode(2);
        listNode.next.next.next = new ListNode(3);
        ListNode listNode1 = new a206().reverseList(listNode);
        System.out.println(listNode1.val);
    }

    public ListNode reverseList(ListNode head) {
        if (head == null && head.next == null) {
            return head;
        }

        while (head.next != null) {
            ListNode next = head.next;
            head.next = null;
            next.next=head; // next.next=0 cuowu
            head = next;

        }
        return head;
    }

}
