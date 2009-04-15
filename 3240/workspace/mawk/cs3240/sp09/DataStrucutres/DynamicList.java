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
	
	@SuppressWarnings("unchecked")
	public E get(int i){
		return (E)array[i];
	}
	
	public void add(E obj){
		if(array.length == size)
			resize(size * 2);
		array[size++] = obj;
	}
	
	/**
	 * TODO: Testing
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

	public int size() {
		return size;
	}

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

	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return new DynamicListIterator<E>();
	}
	
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
			// TODO: Is this entirely necessary?
			
		}
		
	}
}
