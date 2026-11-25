public class Node<K> {
    K key;
    Node<K>prev;
    Node<K>next;

    Node()
    {
        prev = null;
        next = null;
    }
    Node(K key)
    {
        this.key = key;
        prev = null;
        next = null;
    }

}

