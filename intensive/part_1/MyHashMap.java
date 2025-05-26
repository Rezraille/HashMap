package aston.intensive.part_1;

public class MyHashMap<K, V>
{
    private static final int LIMIT_SIZE_BUCKET = 8;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    private Bucket<K, V>[] buckets;
    private int initialCapacity;

    private float loadFactor;
    private int threshold;
    public MyHashMap()
    {
        this(16, 0.75f);
    }

    public MyHashMap(int initialCapacity)
    {
        this(initialCapacity, 0.75f);
    }

    public MyHashMap(int initialCapacity, float loadFactor)
    {
        initialCapacity = getCorrected(initialCapacity);
        check(loadFactor);

        this.initialCapacity = bucketsSizeFor(initialCapacity);
        this.loadFactor = loadFactor;

        this.threshold = createThreshold();
        this.buckets = createBuckets(this.initialCapacity);
    }
    public V put(K key, V value)
    {
        int hash = key == null ? 0 : key.hashCode();
        int index = getIndexBucket(hash);
        V val = buckets[index].put(hash, key, value);;
        if (size() > threshold || buckets[index].size() > LIMIT_SIZE_BUCKET)
        {
            rehashing();
        }
        return val;
    }

    public V get(Object key)
    {
        int hash = key == null ? 0 : key.hashCode();
        int index = getIndexBucket(hash);
        return buckets[index].get(hash, key);
    }

    public V remove(Object key)
    {
        int hash = key == null ? 0 : key.hashCode();
        int index = getIndexBucket(hash);
        return buckets[index].remove(hash, key);
    }

    public boolean remove(Object key, Object value)
    {
        int hash = key == null ? 0 : key.hashCode();
        int index = getIndexBucket(hash);
        return buckets[index].remove(hash, key, value);
    }

    public int size()
    {
        int sum = 0;
        for (int i = 0; i < buckets.length; i++)
        {
            sum += buckets[i].size();
        }
        return sum;
    }

    private static  int getCorrected(int initialCapacity)
    {
        if (initialCapacity < 0)
        {
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY)
            return MAXIMUM_CAPACITY;
        return initialCapacity;
    }
    private static int bucketsSizeFor(int initialCapacity)
    {
        int correctCapacity = initialCapacity - 1;
        correctCapacity = correctCapacity | (correctCapacity >>> 1);
        correctCapacity = correctCapacity | (correctCapacity >>> 2);
        correctCapacity = correctCapacity | (correctCapacity >>> 4);
        correctCapacity = correctCapacity | (correctCapacity >>> 8);
        correctCapacity = correctCapacity | (correctCapacity >>> 16);
        if (correctCapacity < 0)
        {
            return 1;
        } else if (correctCapacity >= MAXIMUM_CAPACITY)
        {
            return MAXIMUM_CAPACITY;
        }
        return correctCapacity + 1;
    }
    private void check(float loadFactor)
    {
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
    }
    private int createThreshold()
    {
        return Math.round(this.initialCapacity * this.loadFactor);
    }
    private Bucket<K,V> [] createBuckets(int initialCapacity)
    {
        Bucket<K,V> [] buckets = new Bucket[initialCapacity];
        for (int i = 0; i < initialCapacity; i++)
        {
            buckets[i] = new Bucket<K, V>();
        }
        return buckets;
    }

    private int getIndexBucket(int hash)
    {
        return hash & (buckets.length - 1);
    }

    private void rehashing()
    {
        int newInitialCapacity = getCorrected(initialCapacity * 2);
        Bucket<K,V> [] oldBuckets = this.buckets;
        this.buckets = createBuckets(newInitialCapacity);
        this.initialCapacity = newInitialCapacity;
        this.threshold = createThreshold();

        for (int i = 0; i < oldBuckets.length; i++)
        {
            Bucket.Node<K,V> current = oldBuckets[i].getHead();
            while (current != null)
            {
                int hash = current.getHash();
                int index = getIndexBucket(hash);
                this.buckets[index].putNew(current);
                current = current.getNext();
            }
        }
    }
}
