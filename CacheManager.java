import java.util.*;

class CacheManager<K, V> {

    // maximum number of entries the cache can hold (never changes)
    final int capacity;

    // Map which contains the real mapping.
    // It behaves as a normal cache; the eviction policy only decides
    // who leaves and how recency changes after a key is accessed.
    Map<K, V> cache;

    // strategy
    EvictionStrategy<K> es;

    CacheManager(int capacity, EvictionStrategy<K> es)
    {
        this.capacity = capacity;
        cache = new HashMap<>();
        this.es = es;
    }


    // get
    V get(K key)
    {
        if(!cache.containsKey(key))
        {
            return null;
        }
        V value = cache.get(key);
        es.keyAccesed(key);
        return value;
    }


    // put
    void put(K key, V value)
    {
        // key already present: only update the value and mark it as used
        if(cache.containsKey(key))
        {
            cache.put(key, value);
            es.keyAccesed(key);
            return;
        }

        // new key and the cache is full: evict first
        if(cache.size() == capacity)
        {
            K evictedKey = es.evict();
            cache.remove(evictedKey);
        }

        es.put(key);
        cache.put(key, value);
    }

    // EDGE: capacity <= 0, null key, and two threads calling put at once (needs a lock)
}
