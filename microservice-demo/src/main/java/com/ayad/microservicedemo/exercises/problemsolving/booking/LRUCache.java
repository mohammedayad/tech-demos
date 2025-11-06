package com.ayad.microservicedemo.exercises.problemsolving.booking;

import java.util.Map;

// initialize with capacity -- positive size
// implement get(int key) -- if not exist return -1
// implement put(int key, int value) -- if it is exist update value 
public class LRUCache {

    private int capacity;

    private Map<Integer, Node> cache;
    private Node head;
    private Node tail;
    private int size;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new java.util.HashMap<>();
        this.head = new Node(0, 0); // dummy head
        this.tail = new Node(0, 0); // dummy tail
        head.next = tail;
        tail.prev = head;
    }

    //if the key exist update the value
    //if not exist create new one
    // move the node after the head as the recently used one
    // track the size of the cache if it is more the capacity remove the node before the tail as the least recently used one
    public void put(int key, int value) {
        if (cache.containsKey(key)) {// exist
            System.out.println("updating existing key: " + key);
            Node node = cache.get(key);
            node.value = value;
            cache.put(key, node);
            removeNode(node);
            moveToHead(node);

        } else { // not exist
            System.out.println("adding new key: " + key);
            System.out.println("current size: " + size + ", capacity: " + capacity);
            if (size >= capacity) {
                Node lessUsedNode = tail.prev;
                System.out.println("removing less used key: " + lessUsedNode.key);
                removeNodeFromTail();
                cache.remove(lessUsedNode.key);
                size--;
            }
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            moveToHead(newNode);
            size++;
            printCache();

        }
    }

    public void printCache() {
        Node current = head;
        while (current != tail) {
            System.out.print("(" + current.key + "," + current.value + ") ");
            current = current.next;
        }
        System.out.print("(" + current.key + "," + current.value + ") ");
        System.out.println();
    }

    // node.prev = head
    // node.next = head.next
    // head.next = node
    // head.next.prev = node

    private void moveToHead(Node node) {
        System.out.println("moving to head key: " + node.key + " prev: " + node.prev + " next: " + node.next);
        System.out.println("head next key: " + head.next.key + " head prev: " + head.prev);
        System.out.println("tail prev key: " + tail.prev.key + " tail next: " + tail.next);
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }


    // tail.prev=tail.prev.prev
    // tail.prev.prev.next=tail
    // tail.prev.next=null
    // tail.prev.prev=null
    private void removeNodeFromTail() {
        Node toRemove = tail.prev;
        Node prevNode = toRemove.prev;
        prevNode.next = tail;
        toRemove.next = null;
        tail.prev = prevNode;
        toRemove.prev = null;


//        tail.prev = tail.prev.prev;
//        tail.prev.prev.next = tail;
//        tail.prev.next = null;
//        tail.prev.prev = null;
        printCache();

    }

    // node.prev.next=node.next
    // node.next.prev=node.prev
    // node.prev=null
    // node.next=null
    private void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;
        prevNode.next = nextNode;
        node.next = null;
        nextNode.prev = prevNode;
        node.prev = null;
//        node.prev.next = node.next;
//        node.next.prev = node.prev;
//        node.prev = null;
//        node.next = null;
    }


    public class Node {
        private int key;
        private int value;
        private Node prev;
        private Node next;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache(2);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        lruCache.put(3, 3);
        lruCache.put(2, 22);
        lruCache.printCache();
//        lruCache.put(3, 3);
        lruCache.put(4, 4);
//        System.out.println(lruCache.cache.keySet()); // [3, 4]
//        lruCache.put(3, 33);
//        System.out.println(lruCache.cache.keySet()); // [3, 4]
//        lruCache.put(5, 5);
//        System.out.println(lruCache.cache.keySet()); // [5, 3]
    }


}
