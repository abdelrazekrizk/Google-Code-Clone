package cs3240.sp09.DataStrucutres;

import java.util.Iterator;

import cs3240.sp09.RegularLanguage.State;

/**
 * Implementation of a dynamically sized list.
 * @author Anthony Marion
 */
public class DynamicList<E> implements java.lang.Iterable<E>{
	int size;
	Object[] array;
	
	public DynamicList() {
		size = 0;
		array = new Object[10];
	}
	
	/**
	 * Retrieves the ith member of this list
	 */
	@SuppressWarnings("unchecked")
	public E get(int i){
		return (E)array[i];
	}
	
	/**
	 * Adds the given object to the end of this list
	 */
	public void add(E obj){
		if(array.length == size)
			resize(size * 2);
		array[size++] = obj;
	}
	
	/**
	 * Removes the given index from this list
	 */
	public E remove(int i){
		E item = get(i);
		for(int j = i + 1; j < array.length; j++){
			array[i++] = array[j++];
		}
		array[array.length] = null;
		size--;
		return item;
	}

	private void resize(int i) {
		Object[] tmp = new Object[i];
		for(int j = 0; j < array.length; j++)
			if(j < tmp.length)
				tmp[j] = array[j];
		array = tmp;
	}

	/**
	 * Returns the number of elements in this list.
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns whether or not this list contains the given state.
	 * (specific to state objects.) 
	 */
	public boolean contains(State state) {
		for(int i = 0; i < size; i++){
			if(array[i].equals(state)){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object o){
		if(o.getClass() == DynamicList.class){
			DynamicList<State> list = (DynamicList<State>)o;
			if(list.size != this.size)
				return false;
			for(int i = 0; i < this.size; i++){
				if(!this.contains(list.get(i))){
					return false;
				}
			}
			return true;
		} else {
			System.err.println("DynamicList.equals error");
			return false;
		}
	}

	/**
	 * Returns the iterator for this list, useful in foreach loops.
	 */
	public Iterator<E> iterator() {
		return new DynamicListIterator<E>();
	}
	
	/**
	 * The iterator implementation for this class
	 */
	private class DynamicListIterator<T> implements java.util.Iterator<T>{
		
		private int position;
		
		private DynamicListIterator(){
			position = 0;
		}
		
		public boolean hasNext() {
			return position < size;
		}

		@SuppressWarnings("unchecked")
		public T next() {
			return (T) get(position++);
		}

		public void remove() {
			// Is this entirely necessary?
			
		}
		
	}
}
