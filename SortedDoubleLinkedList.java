import java.util.Comparator;

/**
 * Create a generic sorted double linked list of objects. Uses a Comparator to determine the sorting arrangement
 * @author Mike Meyers
 * @version 1.0
 *
 * @param <T> A generic type determining the objects that will be able to be added to the list. Must extend Comparable.
 */
public class SortedDoubleLinkedList<T extends Comparable<T>> extends BasicDoubleLinkedList<T> {
	
	Comparator<T> comparator;
	
	/**
	 * Create an empty SortedDoubleLinkedList
	 * @param comparator the Comparator that will determine list sorting
	 */
	public SortedDoubleLinkedList(Comparator<T> comparator) {
		super();
		this.comparator = comparator;
	}
	
	/**
	 * Create a SortedDoubleLinkedList with a single data point
	 * @param data the object to be added
	 * @param comparator the Comparator that will determine list sorting
	 */
	public SortedDoubleLinkedList(T data, Comparator<T> comparator) {
		super(data);
		this.comparator = comparator;
	}
	
	/**
	 * Add an object to the list
	 * @param data the object to be added
	 * @return a SortedDoubleLinkedList object with the added data
	 */
	public SortedDoubleLinkedList<T> add(T data) {

		//Otherwise, create an iterator for traversing and add the data
		Iterator listIterator = iterator();
		
		//If the list is empty, skip right to adding the data
		if(nodeCount == 0) {
			super.addToFront(data);
			return this;
		}
		
		else {
		//If the list has data, search through the data and find where to insert the data
		while(listIterator.hasNext()) {
			if (comparator.compare(data, listIterator.getNext()) < 0) break;
			listIterator.next();
		}
		
		//Wrap the data in a Node to insert
		Node newNode = new Node(data);
		
		//Have iterator insert the Node at its location
		listIterator.insert(newNode);
		
		//Return the list with the data added
		return this;
		}
	}
	
	/**
	 * Unsupported method. The add function handles insertion of data where it is needed
	 * @param data the object to be added
	 * @throws UnsupportedOperationException because this method is not supported
	 */
	@Override
	public SortedDoubleLinkedList<T> addToEnd(T data) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported");
	}
	
	/**
	 * Unsupported method. The add function handles insertion of data where it is needed
	 * @param data the object to be added
	 * @throws UnsupportedOperationException because this method is not supported
	 */
	@Override
	public SortedDoubleLinkedList<T> addToFront(T data) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported");
	}
	
	/**
	 * Create a new iterator for traversing the list
	 * @return an iterator object that can traverse the list
	 */
	@Override
	public Iterator iterator() {
		return new Iterator();
	}
	
	/**
	 * Remove a specific object from the list. If there is more than one instance of the object in the list, only
	 * the first is removed
	 * @param data the object to be removed
	 * @param comparator the Comparator matching the argument data to the list data
	 * @return the sorted list minus the removed object (will return an empty list if the list is reduced to zero objects)
	 */
	public SortedDoubleLinkedList<T> remove (T data, Comparator<T> comparator) {
		//Call superclass's remove method (will remove the first instance of the target datum)
		return (SortedDoubleLinkedList<T>) super.remove(data, comparator);
	}
	
	/**
	 * An internal Iterator class for traversing the Sorted List
	 * @author Mike Meyers
	 * @version 1.0
	 *
	 */
	class Iterator extends BasicDoubleLinkedList<T>.Iterator {

		/**
		 * Insert a Node into the list at the iterator's current location
		 * @param nodeToAdd the Node to be entered into the list
		 */
		public void insert(Node nodeToAdd) {
			
			//If the iterator is at the start of the list already, just insert the data at the front of the list
			if(cursor == 0) {
				SortedDoubleLinkedList.super.addToFront(nodeToAdd.data);
				return;
			}
			
			//If the iterator is at the end of the list, add the data to the end of the list
			else if(cursor == nodeCount) {
				SortedDoubleLinkedList.super.addToEnd(nodeToAdd.data);
				return;
			}
			
			//Otherwise, we are inserting between two Nodes, and need to update references accordingly
			else {
			nodeToAdd.previousNode = thisNode;
			thisNode.nextNode = nodeToAdd;
			nodeToAdd.nextNode = nextNode;
			nextNode.previousNode = nodeToAdd;
			nodeCount++; //Increment size counter. The two super-method calls above contain their own nodeCount++
			}
			
			//Lastly - if inserting at the second-to-last position, lastNode's previousNode reference will need updated
			if(cursor == nodeCount - 2) {
				lastNode.previousNode = nodeToAdd;
			}
		}
		
		/**
		 * Check the next Node's data without advancing the cursor
		 * @return the next Node's data
		 */
		public T getNext() {
			return nextNode.data;
		}
		
	}
	
}
