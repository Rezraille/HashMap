package aston.intensive.part_1;

class Node<K, V>
{
    private int hash;
    private K key;
    private V value;
    private Node<K, V> next;


    protected Node(int hash, K key, V value, Node<K, V> next)
    {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    protected int getHash()
    {
        return hash;
    }

    protected K getKey()
    {
        return key;
    }

    protected V getValue()
    {
        return value;
    }

    protected Node<K, V> getNext()
    {
        return next;
    }

    protected void setValue(V value)
    {
        this.value = value;
    }

    protected void setNext(Node<K, V> next)
    {
        this.next = next;
    }

}
