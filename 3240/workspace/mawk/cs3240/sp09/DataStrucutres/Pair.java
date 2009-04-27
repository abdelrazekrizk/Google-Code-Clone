package cs3240.sp09.DataStrucutres;

/**
 * Custom generic key-value pair data structure.
 */
public class Pair<K, V> {
	public K key;
	public V value;
	
	public Pair(K key, V value){
		this.key = key;
		this.value = value;
	}
}
