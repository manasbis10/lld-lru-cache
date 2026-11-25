# LRU Cache (Java)

Low-level design practice: a fixed-capacity cache that evicts the least recently used key.

- `CacheManager<K,V>` owns the `key -> value` map and the capacity. It decides when to evict.
- `EvictionStrategy<K>` is the policy interface: key accessed, key added, who should go.
- `Lru<K>` keeps its own `key -> Node` map and a doubly linked list with dummy head and tail, so `get` and `put` are O(1).

Run:

    javac *.java && java Main
