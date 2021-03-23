public class SLList {
	public static class IntNode {
		public int item;
		public IntNode next;

		public IntNode(int i, IntNode n) {
			item = i;
			next = n;
		}
		/** Returns the ith item of this IntList. */
		public int get(int i) {
			if (i == 0) {
				return item;
			}
			return next.get(i - 1);
		}
	}

	private IntNode first;
	private int size;

	/**
	 * Constructors for list and empty list
	 */
	public SLList() {
		first = null;
		size = 0;
	}

	public SLList(int x) {
		first = new IntNode(x, null);
		size = 1;
	}

	/** Adds an item to the front of the list. */
	public void addFirst(int x) {
		size += 1;
		first = new IntNode(x, first);
	}


	/**
	 * Returns the size of the list - Caching
	 */
	public int size() {
		return size;
	}

	/** Above all are given code, I write code from below for disc2 */

	/** SLList.insert which takes in an integer x and an integer position.
	 It inserts x at the given position
	 If position is after the end of the list, insert
	 the new node at the end.
	 */
	public void insert(int item, int position) {
		IntNode p = first;
		if (position == 0) {
			addFirst(item);
			size += 1;
			return;
		}
		while (position > 1 && p.next != null) {
			p = p.next;
			position = position - 1;
		}
		p.next = new IntNode(item, p.next);
		size += 1;
	}

	/**
	 * Recursively removes all nodes that contain
	 * a certain item. This method takes in an integer x
	 * and destructively changes the list
	 * */
	private IntNode removeItemHelper(int x, IntNode current) {
		if (current == null) {
			return null;
		} else if (current.item == x) {
			// skip current item
			current = current.next;
			return removeItemHelper(x, current);
		} else {
			// this step is to get the updated current.next result back!
			current.next = removeItemHelper(x,current.next);
			return current;
		}
	}

	public void removeItem(int x) {
		first = removeItemHelper(x, first);
	}

	/**
	 * reverses the elements
	 * */
	public void reverse() {
		if (first == null || first.next == null) {
			return;
		}
		// core idea is to first store location first.next, then we detach first next to
		// to make the new reverse list, and put first at the last position first.next = null
		IntNode ptr = first.next;
		first.next = null;
		while (ptr != null) {
			// temp save the location after detached item ptr.item
			IntNode temp = ptr.next;
			// since we have stored the unordered list into temp, we detach and update first
			ptr.next = first;
			first = ptr;
			// update is done, reassign ptr to continue while
			ptr = temp;
		}
	}

	public static void main(String[] args) {
		SLList L = new SLList(10);
		L.addFirst(5);
		L.addFirst(1);
		L.insert(8, 1);
//		L.removeItem(8);
		L.reverse();
		for (int i = 0; i < L.size(); i++) {
			System.out.println(L.first.get(i));
		}
	}
}