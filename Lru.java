import java.util.*;

class Lru<K> implements EvictionStrategy<K> {
    
    Node<K> start;
    Node<K> end;
    Map<K, Node<K>>keyAddress;
    Lru()
    {
        keyAddress = new HashMap<>();
        start = new Node<>();
        end = new Node<>();

        start.next = end;
        end.prev = start;

    }

    public void keyAccesed(K key)
    {
        Node<K> node = keyAddress.get(key);
        
        Node<K>prevNode = node.prev;
        Node<K>nextNode = node.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;

        Node<K>startNext = start.next;
        start.next = node;
        node.prev = start;

        node.next = startNext;
        startNext.prev = node;

    }

    public void put(K key)
    {
        Node<K> node = new Node<>();
        keyAddress.put(key, node);
        node.key = key;

        Node<K>startNext = start.next;
        start.next = node;
        node.prev = start;
        node.next = startNext;
        startNext.prev = node;

    }

    public K evict()
    {
        Node<K>evictNode = end.prev;
        K key = evictNode.key;

        keyAddress.remove(key);

        evictNode.prev.next = end;
        end.prev = evictNode.prev;

        return key;

    }





}
