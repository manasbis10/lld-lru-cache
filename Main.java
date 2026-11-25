import java.util.Objects;

class Main {

    static int failures = 0;

    // Objects.equals, not ==, because Integer == only works for small cached values
    static void check(String name, Object actual, Object expected) {
        boolean ok = Objects.equals(actual, expected);
        if (!ok) failures++;
        System.out.println((ok ? "PASS  " : "FAIL  ") + name
                + (ok ? "" : "   expected=" + expected + " actual=" + actual));
    }

    static CacheManager<Integer, Integer> newCache(int capacity) {
        return new CacheManager<>(capacity, new Lru<Integer>());
    }

    public static void main(String[] args) {

        // 1. basic eviction: get(1) saves 1, so 2 is evicted
        CacheManager<Integer, Integer> c = newCache(2);
        c.put(1, 10);
        c.put(2, 20);
        check("1a get(1)", c.get(1), 10);
        c.put(3, 30);
        check("1b 2 evicted", c.get(2), null);
        check("1c get(3)", c.get(3), 30);
        check("1d get(1)", c.get(1), 10);

        // 2. miss on an empty cache
        c = newCache(2);
        check("2  miss", c.get(99), null);

        // 3. put on an existing key updates it and must not create a second node
        c = newCache(2);
        c.put(1, 10);
        c.put(1, 11);
        check("3a updated value", c.get(1), 11);
        c.put(2, 20);
        c.put(3, 30);                       // full: evicts 1 (oldest)
        check("3b 1 evicted", c.get(1), null);
        check("3c get(2)", c.get(2), 20);
        check("3d get(3)", c.get(3), 30);

        // 4. get on the key that is already the most recent (the self-loop bug case)
        c = newCache(2);
        c.put(1, 10);
        c.put(2, 20);
        c.get(2);
        c.get(2);
        c.put(3, 30);                       // evicts 1
        check("4a 1 evicted", c.get(1), null);
        check("4b get(2)", c.get(2), 20);
        check("4c get(3)", c.get(3), 30);

        // 5. capacity 1
        c = newCache(1);
        c.put(1, 10);
        c.put(2, 20);
        check("5a 1 evicted", c.get(1), null);
        check("5b get(2)", c.get(2), 20);

        // 6. access order: list is [3,2,1], get(1) makes it [1,3,2], so put(4) evicts 2
        c = newCache(3);
        c.put(1, 10);
        c.put(2, 20);
        c.put(3, 30);
        c.get(1);
        c.put(4, 40);
        check("6a 2 evicted", c.get(2), null);
        check("6b get(3)", c.get(3), 30);
        check("6c get(1)", c.get(1), 10);
        check("6d get(4)", c.get(4), 40);

        System.out.println(failures == 0 ? "\nALL PASSED" : "\n" + failures + " FAILED");
    }
}
