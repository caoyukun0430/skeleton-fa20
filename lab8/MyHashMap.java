import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import edu.princeton.cs.algs4.SequentialSearchST;

/** source: https://algs4.cs.princeton.edu/34hash/
 * https://algs4.cs.princeton.edu/34hash/SeparateChainingHashST.java.html */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private int initialSize = 16;
    private double loadFactor = 0.75;           // N / M
    private double decreaseFactor = 0.25;       // N / M used for remove()
    private int N;                              // number of key-value pairs
    private int M;                              // hash table size, num. of buckets
    // https://algs4.cs.princeton.edu/34hash/SequentialSearchST.java.html
    // array of linked-list symbol tables, each item SequentialSearchST is
    // a singly-linked list aka. bucket
    private SequentialSearchST<K, V>[] hashTable;

    /**
     * Initializes an empty symbol table with {@code m} chains.
     * num. of bucket using default initialSize = 16
     */
    public MyHashMap() {
        this.M = initialSize;
        hashTable = (SequentialSearchST<K, V>[]) new SequentialSearchST[M];
        for (int i = 0; i < M; i++) {
            hashTable[i] = new SequentialSearchST<K, V>();
        }
    }

    /**
     * Initializes an empty symbol table with {@code m} chains.
     * @param initialSize the initial number of chains
     */
    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        this.M = initialSize;
        hashTable = (SequentialSearchST<K, V>[]) new SequentialSearchST[M];
        for (int i = 0; i < M; i++) {
            hashTable[i] = new SequentialSearchST<K, V>();
        }
    }

    /**
     * Initializes an empty symbol table with {@code m} chains.
     * @param initialSize the initial number of chains
     * @param loadFactor the loadFactor aka. size / hashSize
     */
    public MyHashMap(int initialSize, double loadFactor) {
        this.initialSize = initialSize;
        this.loadFactor = loadFactor;
        this.M = initialSize;
        hashTable = (SequentialSearchST<K, V>[]) new SequentialSearchST[M];
        for (int i = 0; i < M; i++) {
            hashTable[i] = new SequentialSearchST<K, V>();
        }
    }

    // hash function for keys - returns key bucket index between 0 and M-1
    private int hash(K key) {
        return Math.floorMod(key.hashCode(), hashTable.length);
    }

    // Resize / multiply size to capability when N / M >= loadFactor
    private void resize(int capability) {
        MyHashMap<K, V> temp = new MyHashMap<K, V>(capability);
        for (int i = 0; i < M; i++) {
            for (K key: hashTable[i].keys()) {
                // copy each pair into temp
                temp.put(key, hashTable[i].get(key));
            }
        }
        // update the private class instances
        this.M = temp.M;
        this.N = temp.N;
        this.hashTable = temp.hashTable;
    }

    /**
     * Clean all key-value pairs in the table and initialize it.
     * */
    @Override
    public void clear() {
        N = 0;
        for (int i = 0; i < M; i++) {
            hashTable[i] = new SequentialSearchST<K, V>();
        }
    }

    /**
     * Returns true if this symbol table contains the specified key.
     *
     * @param  key the key
     * @return {@code true} if this symbol table contains {@code key};
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    /**
     * Returns the value associated with the specified key in this symbol table.
     *
     * @param  key the key
     * @return the value associated with {@code key} in the symbol table;
     *         {@code null} if no such value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        // using hash func to get index in the table
        int index = hash(key);
        // SequentialSearchST linked list has get() to iterate over to check if key exists
        return hashTable[index].get(key);
    }

    @Override
    public int size() {
        return N;
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * NOT IMPLEMENTED YET: Deletes the specified key (and its associated value)
     * from this symbol table if the specified value is {@code null}.
     *
     * @param  key the key
     * @param  value the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("argument to put() is null");
        }
        if ((double) N / M >= loadFactor) {
            resize(2 * M);
        }
        int index = hash(key);
        // Only if key is not presented, we increase size
        if (!containsKey(key)) {
            N++;
        }
        // We need to update value even if key exists
        hashTable[index].put(key, value);
    }

    /** Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (int i = 0; i < M; i++) {
            for (K key : hashTable[i].keys()) {
                keySet.add(key);
            }
        }
        return keySet;
    }

    /**
     * Removes the key and associated value from the symbol table
     * (if the key is in the symbol table).
     * @param key the key
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to remove() is null");
        }
        V removed = get(key);
        int index = hash(key);
        if (containsKey(key)) {
            N--;
        }
        // SequentialSearchST itself judges whether key exists in bucket
        hashTable[index].delete(key);

        // Havle the hash size if the hash is too empty
        if (M > initialSize && (double) N / M <= decreaseFactor) {
            resize(M / 2);
        }
        return removed;

    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value.
     */
    @Override
    public V remove(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("argument to remove() is null");
        }
        if (get(key).equals(value)) {
            return remove(key);
        } else {
            return null;
        }
    }

    /**
     * Returns an Iterator that iterates over the stored keys in any order
     * */
    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
