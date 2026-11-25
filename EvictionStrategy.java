interface EvictionStrategy<K> {
    
    //policies will have diff strategies to handle when a key is accessed.
    void keyAccesed(K key);

    //create the new node for the new key
    void put(K key);

    //gives who will leave when cache is full
    K evict();
}
