package jun.task;

/**
 * This implementation uses Open Addressing collision resolution technique.
 * In which all entry records are stored in the bucket array itself.
 * Which uses less memory in comparison to Separate Chaining where each bucket is independent.
 * and for each entry has to be allocated more memory for additional class header(12 bytes).
 *
 * Note: this implementation cannot be done using primitive type long
 */

public class LongMapOpenAddressingImpl<V> {
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
    private Long[] keys;
    private Object[] values;
    private final float loadFactor;

    public LongMapOpenAddressingImpl() {
        this(DEFAULT_CAPACITY);
    }

    public LongMapOpenAddressingImpl(int initialCapacity) {
        this(initialCapacity,DEFAULT_LOAD_FACTOR);
    }

    public LongMapOpenAddressingImpl(int initialCapacity, float loadFactor) {

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
        count = 0;
        keys =  new Long[mapSize];
        values = new Object[mapSize];
    }

    /**
     * Returns the number of key-value pairs in this hashtable.
     */
    public int size(){
        return count;
    }

    /**
     * Tests if the specified object is a key in this hashtable.
     */
    public boolean containsKey(Long key){
        return get(key) != null;
    }

    /**
     * Tests if some key maps into the specified value in this hashtable.
     * This method is more expensive than containsKey.
     * @see        #containsKey(Long)
     */
    public boolean containsValue(V value){
        if(value == null) throw new NullPointerException();

        for(int i=0;i<mapSize;i++){
            if(values[i]!=null && values[i] == value){
                return true;
            }
        }
        return false;
    }
    /**
     * Applies a supplemental hash function to a given hashCode, which defends against poor quality hash functions.
     * This is critical because by default Map uses power-of-two length hash tables, that otherwise encounter
     * collisions for hashCodes that do not differ in lower bits.
     */
    private int hash(Long key) {
        int hash = key.hashCode();
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
        LongMapOpenAddressingImpl<V> temp = new LongMapOpenAddressingImpl<>(newSize);
        for (int i = 0; i < mapSize; i++) {
            if (keys[i] != null) {
                temp.put(keys[i], (V)values[i]);
            }
        }
        keys = temp.keys;
        values = temp.values;
        mapSize = temp.mapSize;
    }
    /**
     * Rehashes values after removing entry
     */
    private void reHash(int startIndex){
        Object tempVal;
        long temKey;
        for (int i = (startIndex + 1) % mapSize; keys[i] != null; i= (i + 1) % mapSize) {
            tempVal = values[i];
            temKey = keys[i];
            values[i] = null;
            keys[i] = null;
            count--;
            put(temKey, (V)tempVal);
        }
    }

    /**
     * Returns the value to which the specified key is mapped in this map.
     */
    public V get(Long key){
        int index = hash(key);

        while(keys[index] != null){
            if(keys[index].equals(key)){
                return (V)values[index];
            }
            index = (index + 1) % mapSize;
        }
        return null;
    }

    /**
     * Maps the specified key to the specified
     * value in this hashtable. The key cannot be null.
     */
    public void put(Long key, V value){
        if(key == null) throw new IllegalArgumentException("Illegal key: " + key);

        if(mapSize*loadFactor<= count){
            reAllocateHashMap(mapSize*2);
        }

        int i;
        for (i = hash(key); keys[i] != null ; i = (i + 1) % mapSize) {
            if (keys[i].equals(key)) {
                values[i] = value;
                return ;
            }
        }
        keys[i] = key;
        values[i] = value;
        count++;
    }
    /**
     * Removes the key (and its corresponding value) from this hashtable.
     * After removing entry other entries in the same cluster has to be rehashed
     * to ensure that they don't have collision with the removed entry.
     * This method does nothing if the key is not present in the hashtable.
     */
    public void remove(Long key){
        int index = hash(key);

        while(keys[index]!=null){
            if(keys[index].equals(key)){
                keys[index] = null;
                values[index] = null;
                count--;
                if(count!=0 && count <= mapSize/8){
                    reAllocateHashMap(mapSize/2);
                }else {
                    reHash(index);
                }
                return;
            }
            index = (index + 1) % mapSize;
        }
    }
}

