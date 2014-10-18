// TODO: Remove each 'todo' comment once I implement each part!

// TODO: class comment header


//plamb was here
import java.util.HashSet;
import java.util.Set;

public class HashMap<K, V> implements Map<K, V> {
	private static final double MAX_LOAD = 0.75;   // load factor on which to rehash
	
	private Node[] elements;
	private int size;
	
	
	public HashMap() {
		elements = (Node[])new HashMap.Node[10];
		size = 0;
	}
	
	public void clear() {
		for (int i = 0; i < elements.length; i++) {
			elements[i] = null;
		}
		size = 0;
		
	}
	
	public boolean containsKey(K key) {
		int h = hash(key);
		Node current = elements[h];
		while (current != null) {
			if (current.key.equals(key)) {
				return true;
			}
			//iterate to next linked node
			current = current.next;
		}
		return false;
	}

	public V get(K key) {
		int h = hash(key);
		Node current = elements[h];
		while (current != null){
			if (current.key.equals(key)){
				return current.value;
			}
			current = current.next;
		}
		return null;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public Set<K> keySet() {
		Set<K> keyset = new HashSet<K>();
		for (int i = 0; i < elements.length; i++) {
			Node current = elements[i];
			//this is O(N^2) tho the spec says O(N) wot
			while (current != null) {
				keyset.add(current.key);
				current = current.next;
			}
		}
		return keyset;
	}
	
	public void put(K key, V value) {
		int h = hash(key);
		Node current = elements[h];
		//index is null so add new Node
		if (current == null){
			Node newNode = new Node(key, value);
			elements[h] = newNode;
			size++;
		}else {
			//check to see if existing nodes have the same key to replace
			boolean replaced = false;
			while (current != null){
				if (current.key.equals(key)) {
					current.key = key;
					current.value = value;
					replaced = true;
				}
				current = current.next;
			}
		//add to front of existing linked list
		//p sure this is wrong
			if (replaced == false) {
				Node newNode = new Node(key, value); 
				newNode.next = elements[h];
				elements[h] = newNode;
				size++;
			}
		}
		
		// resize if necessary
		if (loadFactor() > MAX_LOAD) {
					rehash();
		}
	}
	
	public void remove(K key) {
		int h = hash(key);
		if(elements[h] != null){
			//case where it has one node
			if(elements[h].key.equals(key)){
				elements[h] = elements[h].next;
				size--;
			}else{
				//case where its somewhere in the list
				Node current = elements[h];
				//iterate through elements until you find the one where key == key and you're not at the end
				while (current.next != null && !current.next.key.equals(key)){
					current = current.next;
				}
				//if you havent found the end, you've found where key = key, so remove
				if(current.next != null){
					current.next = current.next.next;
					size--;
				}
			}
		}
	}

	public int size() {
		return size;
	}
	
	
	public String toString() {
		if (isEmpty()){
			return "{}";
		}else{
		String toplol = "{";
		for (int i = 0; i < elements.length; i++){
			Node current = elements[i];
			String lol = "";
			while (current != null){
				lol = lol + current.key+"="+current.value+" ";
				current = current.next;
			}
			if (elements[i] == null){
				
			}else {
			toplol = toplol + "["+lol.substring(0, lol.length()-1)+"], ";
			}
		}
		
		toplol = toplol.substring(0,toplol.length()-2)+"}";
		return toplol;
		}
	}
	
	private void rehash() {
		Node[] newElements = (Node[]) new HashMap.Node[2 * this.elements.length];
		Node[] old = this.elements;
		size = 0;
		this.elements = newElements;
		for (Node node : old) {
			while (node != null) {
				put(node.key, node.value);
				node = node.next;
			}
			
		}
		
	}
	
	private double loadFactor() {
		return (double) size / elements.length;
	}
	
	private int hash(K key) {
		return Math.abs(key.hashCode()) % elements.length;
	}
	
	private class Node {
		public K key;
		public V value;
		public Node next;
		
		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
	
}
