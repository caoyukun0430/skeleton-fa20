public class DMSList {
    private IntNode sentinel;
    public DMSList() {
        sentinel = new IntNode(-1000, new LastIntNode());
    }
    public class IntNode {
        public int item;
        public IntNode next;
        public IntNode(int i, IntNode h) {
            item = i;
            next = h;
        }
        public int max() {
            return Math.max(item, next.max());
        }
    }
    public class LastIntNode extends IntNode {
        //Constructor
        public LastIntNode() {
            super(0, null);
        }
        @Override
        public int max() {
            return 0;
        }
    }
    /* Returns 0 if list is empty. Otherwise, returns the max element. */
    public int max() {
        return sentinel.next.max();
    }
    public void insertFront(int x) {
        sentinel.next = new IntNode(x, sentinel.next);
    }

    public static void main(String[] args) {
        DMSList lst = new DMSList();
        lst.insertFront(1);
        lst.insertFront(3);
        lst.insertFront(2);
        int max1 = lst.max();
    }
}
