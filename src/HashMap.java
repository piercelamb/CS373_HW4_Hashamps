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
		int h = hash(key, elements);
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
		int h = hash(key, elements);
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
		int h = hash(key, elements);
		Node current = elements[h];
		//index is null so add new Node
		if (current == null){
			current = new Node(key, value);
			size++;
		}
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
		
		// resize if necessary
		if (loadFactor() > MAX_LOAD) {
					rehash();
		}
	}
	
	public void remove(K key) {
		// TODO: implement this method
		
	}

	public int size() {
		return size;
	}
	
	
	public String toString() {
		for (int i = 0; i < elements.length; i++){
			Node current = elements[i];
			System.out.print("{");
			while (current != null){
				System.out.print(current.key+"="+current.value+" ");
				current = current.next;
			}
		}
		return "TODO";
	}
	
	private void rehash() {
		Node[] newElements = (Node[]) new HashMap.Node[2 * this.elements.length];
		Node[] old = this.elements;
		size = 0;
		for (Node node : old) {
			while (node != null) {
				int h = hash(node.key, newElements);
				put(node.key, node.value);
				node = node.next;
			}
			size++;
		}
		this.elements = newElements;
	}
	
	private double loadFactor() {
		return (double) size / elements.length;
	}
	
	private int hash(K key, Node[] elements) {
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
