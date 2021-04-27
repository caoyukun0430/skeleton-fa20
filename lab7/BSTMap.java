import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashSet;

/** @source: https://algs4.cs.princeton.edu/32bst/BST.java.html */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;             // root of BST

    private class Node {
        private K key;           // sorted by key
        private V val;         // associated data
        private Node left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree

        Node(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    public BSTMap() {
        root = null;
    }


    @Override
    public void clear() {
        root = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    @Override
    public V get(K key) {
        return get(root, key);
    }

    /* private HELPER for get */
    private V get(Node x, K key) {
        // base cases
        if (x == null) {
            return null;
        }
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        // compare with x.key, three cases
        int cmp = key.compareTo(x.key);
        if (cmp == 0) {
            return x.val;
        } else if (cmp < 0) {
            return get(x.left, key);
        } else {
            return get(x.right, key);
        }
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    @Override
    public int size() {
        return size(root);
    }

    // private HELPER: return number of key-value pairs in BST rooted at x
    private int size(Node x) {
        if (x == null) {
            return 0;
        } else {
            return x.size;
        }
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * optional: Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param  key the key
     * @param  value the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }
        root = put(root, key, value);
    }

    /* Private HELPER for put */
    private Node put(Node x, K key, V value) {
        // if root is null yet, it's the 1st element
        // Or we reached the leaf and still match nothing
        if (x == null) {
            return new Node(key, value, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp == 0) {
            x.val = value;
        } else if (cmp < 0) {
            x.left = put(x.left, key, value);
        } else {
            x.right = put(x.right, key, value);
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    public void printInOrder() {
        for (K i : this) {
            System.out.println(i);
        }
    }

    // https://stackoverflow.com/questions/8712469/any-implementation-of-ordered-set-in-java
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new LinkedHashSet<>();
        keySet(keySet, root);
        return keySet;
    }

    // tree preorder traversal, root - left- right
    // in this exact order, we need the LinkedHashSet, so that element
    // is added in this way, Set is unordered
    private void keySet(Set<K> set, Node x) {
        // if x is null, no need to traverse
        if (x == null) {
            return;
        }
        set.add(x.key);
        keySet(set, x.left);
        keySet(set, x.right);
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     * return null if the argument key does not exist in the BSTMap.
     *
     * @param  key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("calls remove() with a null key");
        }
        V removedVal = get(key);
        root = remove(root, key);
        return removedVal;
    }

    private Node remove(Node x, K key) {
        if (x == null) {
            return null;
        }
        // Try to find the key to remove
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = remove(x.left, key);
        } else if (cmp > 0) {
            x.right = remove(x.right, key);
        } else {
            // cmp == 0 means the key is reached
            // Three deletion cases
            // Case 1: if x has no child, remove it simplt
            if (x.left == null && x.right == null) {
                return null;
            } else if (x.left == null) {
                // Case 2a: x has only right child
                return x.right;
            } else if (x.right == null) {
                // Case 2b: x has only left child
                return x.left;
            } else {
                // Case 3: x has 2 children, take the left-most node in the right subtree
                Node temp = x;
                // overwrite x with left-most node in right subtree
                x = getMin(temp.right);
                // right branch of x is updated, we have to removeMin first,
                // because temp.right will be changed by x.left = temp.left
                x.right = removeMin(temp.right);
                // left branch of x unchanged
                x.left = temp.left;

            }
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    /* Get the left-most node in a tree */
    private Node getMin(Node x) {
        if (x.left == null) {
            return x;
        } else {
            return getMin(x.left);
        }
    }

    /* Removes the smallest key and associated value */
    private Node removeMin(Node x) {
        if (x.left == null) {
            return x.right;
        } else {
            x.left = removeMin(x.left);
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * specific values*/
    @Override
    public V remove(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("calls remove() with a null key");
        }
        if (get(key).equals(value)) {
            return remove(key);
        } else {
            return null;
        }
    }

   /* Direct use the iterator method from HashSet*/
    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
