package aston.intensive.part_1;

class Bucket<K, V>
{
    private Node<K, V> head;
    private int size;

    Bucket()
    {
        this.size = 0;
    }

    protected V put(int hash, K key, V value)
    {
        Node<K, V> inside = getMatchNode(hash, key);
        V oldValue = null;
        if (inside == null)
        {
            Node<K, V> outside = new Node<>(hash, key, value, null);
            makePut(outside);
        } else
        {
            oldValue = inside.getValue();
            inside.setValue(value);
        }
        return oldValue;
    }

    protected void putNew(Node<K,V> node)
    {
        makePut(new Node<>(node.getHash(), node.getKey(), node.getValue(), null));
    }

    protected V get(int hash, Object key)
    {
        Node<K, V> node = getMatchNode(hash, key);
        return node != null ? node.getValue() : null;
    }

    protected V remove(int hash, Object key)
    {
        Node<K, V> prevNode = getPrevNode(hash, key);
        if (prevNode != null)
        {
            Node<K, V> currentNode = prevNode.getNext();
            makeRemove(currentNode, prevNode);
            return currentNode.getValue();
        }
        return null;
    }

    protected boolean remove(int hash, Object key, Object value)
    {
        Node<K, V> prevNode = getPrevNode(hash, key);
        if (prevNode != null)
        {
            Node<K, V> currentNode = prevNode.getNext();
            if (currentNode.getValue().equals(value))
            {

                makeRemove(currentNode, prevNode);
                return true;
            }
        }
        return false;
    }


    protected int size()
    {
        return this.size;
    }

    protected Node<K, V> getHead()
    {
        return this.head;
    }

    private Node<K, V> getMatchNode(int hashOutside, Object keyOutside)
    {
        Node<K, V> current = head;
        while (current != null)
        {
            if (hashOutside == current.getHash())
            {
                K keyCurrent = current.getKey();
                if (sameKey(keyCurrent, keyOutside))
                {
                    return current;
                }
            }
            current = current.getNext();
        }
        return null;
    }

    private void makePut(Node<K, V> outside)
    {
        link(outside, head);
        size++;
    }

    private Node<K, V> getPrevNode(int hashOutside, Object keyOutside)
    {
        Node<K, V> current = head;
        Node<K, V> prev = null;
        while (current != null)
        {
            if (hashOutside == current.getHash())
            {
                K keyCurrent = current.getKey();
                if (sameKey(keyCurrent, keyOutside))
                {
                    if (current == head)
                    {
                        prev = new Node<>(0, null, null, head);
                    }
                    return prev;
                }
            }
            prev = current;
            current = current.getNext();
        }
        return prev;
    }

    private boolean sameKey(Object keyCurrent, Object keyOutside)
    {
        if (keyCurrent == keyOutside) return true;
        return keyCurrent != null && keyCurrent.equals(keyOutside);
    }

    private void link(Node<K, V> prev, Node<K, V> current)
    {
        if (prev != null)
        {
            prev.setNext(current);
            if (current == head)
            {
                setHead(prev);
            }
        }
    }

    private void makeRemove(Node<K, V> currentNode, Node<K, V> prevNode)
    {
        if (currentNode != head)
        {
            link(prevNode, currentNode.getNext());
        }
        unlink(currentNode);
        size--;
    }

    private void unlink(Node<K, V> current)
    {
        if (current == head)
        {
            setHead(current.getNext());
        }
        current.setNext(null);
    }

    private void setHead(Node<K, V> node)
    {
        this.head = node;
    }

    protected static class Node<K, V>
    {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;


        private Node(int hash, K key, V value, Node<K, V> next)
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

        private void setValue(V value)
        {
            this.value = value;
        }

        private void setNext(Node<K, V> next)
        {
            this.next = next;
        }
    }
}