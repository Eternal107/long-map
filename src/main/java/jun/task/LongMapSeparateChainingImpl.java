package jun.task;

/**
 * This implementation uses Separate Chaining collision resolution technique.
 * In which each bucket is independent and has linked entries in the same index.
 * This approach uses more memory rather than Open Addressing because of necessity
 * allocate additional memory for Entry class header(12 bytes).
 * But this technique allows us to use primitive long which saves us 16 bytes
 * in comparison to Long.
 *
 * Note: Using type Long as key makes this implementation less memory  efficient
 * than Open Addressing.
 * With primitive long it's a little bit more memory efficient with default load factor.
 */
public class LongMapSeparateChainingImpl<V> {

    /**
     * Innerclass that acts as a data structure to create a new entry in the table.
     */
    private static class Entry<V>{
        private final long key;
        private V value;
        private Entry<V> next;

        public Entry(long key, V value, Entry<V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    /**
     * The default capacity for the hashtable.
     */
    static final int DEFAULT_CAPACITY = 8;

    /**
     * The load factor for the hashtable.
     * lower factor works faster but less memory efficient.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * The actual amount of allocated entries.
     */
    public int mapSize;
    /**
     * The total number of entries in the hash table.
     */
    public int count;
    private Entry<V>[] table;
    private final float loadFactor;

    public LongMapSeparateChainingImpl() {
        this(DEFAULT_CAPACITY);
    }

    public LongMapSeparateChainingImpl(int initialCapacity) {
        this(initialCapacity,DEFAULT_LOAD_FACTOR);
    }

    public LongMapSeparateChainingImpl(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 && loadFactor > 1) {
            throw new IllegalArgumentException("Illegal Load factor: " + loadFactor);
        }
        if (initialCapacity == 0) {
            initialCapacity = 1;
        }
        this.mapSize = initialCapacity;
        this.loadFactor = loadFactor;
        table = new Entry[mapSize];
    }

    /**
     * Returns the number of key-value pairs in this hashtable.
     */
    public int size() {
        return count;
    }

    /**
     * Tests if the specified object is a key in this hashtable.
     */
    public boolean containsKey(long key) {
        int index = hash(key);
        for (Entry<V> e = table[index]; e != null; e = e.next) {
            if (e.key == key) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests if some key maps into the specified value in this hashtable.
     * This method is more expensive than containsKey.
     * @see        #containsKey(long)
     */
    public boolean containsValue(V value) {
        if (value == null) {
            throw new NullPointerException();
        }

        for (int i = 0 ; i < table.length;i++) {
            for (Entry<V> e = table[i]; e != null; e = e.next) {
                if (e.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Applies a supplemental hash function to a given hashCode, which defends against poor quality hash functions.
     * This is critical because by default Map uses power-of-two length hash tables, that otherwise encounter
     * collisions for hashCodes that do not differ in lower bits.
     */
    private int hash(long key) {
        int hash = (int)(key ^ (key >>> 32)); /** implementation equals to Long.HashCode()*/
        hash =hash ^ ((hash >>> 20) ^ (hash >>> 12));
        hash ^= (hash >>> 7) ^ (hash >>> 4);
        return (hash % mapSize + mapSize) % mapSize;
    }

    /**
     * Increases the capacity of and internally reorganizes this
     * hashtable, in order to accommodate and access its entries more
     * efficiently.
     */
    private void reAllocateHashMap(int newSize){
        this.mapSize = newSize;
        int oldCapacity = table.length;

        Entry<V>[] oldMap = table;
        Entry<V>[] newMap = new Entry[newSize];

        table = newMap;

        for (int i = 0; i < oldCapacity;i++) {
            for (Entry<V> old = oldMap[i]; old != null;) {
                Entry<V> e = old;
                old = old.next;

                int index = hash(e.key);
                e.next = newMap[index];
                newMap[index] = e;
            }
        }
    }
    /**
     * Returns the value to which the specified key is mapped in this map.
     */
    public V get(long key) {
        int index = hash(key);
        for (Entry<V> e = table[index]; e != null; e = e.next) {
            if (e.key == key) {
                return e.value;
            }
        }
        return null;
    }
    /**
     * Maps the specified key to the specified
     * value in this hashtable. The key cannot be null.
     */
    public void put(long key, V value) {

        if(mapSize*loadFactor<= count){
            reAllocateHashMap(mapSize*2);
        }
        int index = hash(key);
        for (Entry<V> e = table[index]; e!=null; e = e.next){
            if(e.key == key){
                e.value = value;
                return;
            }
        }

        Entry<V> e = new Entry<>(key, value, table[index]);
        table[index] = e;
        count++;

    }
    /**
     * Removes the key (and its corresponding value) from this hashtable.
     * This method does nothing if the key is not present in the hashtable.
     */
    public void remove(long key) {
        int index = hash(key);
        for (Entry<V> e = table[index], prev = null; e != null; prev = e, e = e.next) {
            if (e.key == key) {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    table[index] = e.next;
                }
                count--;
                e.value = null;
                if(count!=0 && count <= mapSize/8){
                    reAllocateHashMap(mapSize/2);
                }
            }
        }
    }
}