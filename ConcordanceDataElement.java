import java.util.LinkedList;

public class ConcordanceDataElement implements Comparable<ConcordanceDataElement>{

	private String word;
	private int[] occurrences;
	private int nextIndex;
	
	public ConcordanceDataElement(String word) {
		this.word = word.toLowerCase();
		occurrences = new int[1];
		nextIndex = 0;
	}
	
	public ConcordanceDataElement(String word, int line) {
		this.word = word.toLowerCase();
		occurrences = new int[1];
		occurrences[0] = line;
		nextIndex = 1;
	}
	
	/**
	 * Add a new page number to the ConcordanceDateElement
	 * @param page the page number to be added
	 */
	public void addPage(int page) {
		
		//Check if the page number is new to the list. If so, add it to the list
		if (isUnique(page)) {
			
			//Check if the occurrences array is full. If so, expand it
			if (this.isFull()) expand();
			
			//The occurrences array should be sorted in ascending order.
			//If the last item in occurrences is lower than the page to be added, we can add the
			//new page at the end
			if (occurrences[nextIndex - 1] < page) {
				expand();
				occurrences[nextIndex] = page;
			}
			//Otherwise, insert the page in sorted order
			else insert(page);
			
			//Increment the nextIndex counter to point to the next open index
			nextIndex++;
		}
		
		//If the new page fails the isUnique test, method returns
		else return;
	}
	
	/**
	 * Compare this ConcordanceDataElement to another
	 * @return the alphabetical comparison of this CDE with another
	 */
	@Override
	public int compareTo(ConcordanceDataElement o) {

		return this.word.compareTo(o.getWord());
		
	}
	
	/**
	 * Generate a List of the ConcordanceDataElement's occurring page numbers
	 * @return a LinkedList representing the pages on which the CDE's Word occurs
	 */
	public LinkedList<Integer> getList() {
		
		//Create an empty list for returning
		LinkedList<Integer> returnList = new LinkedList<>();
		
		//Append each occurrence page number to the return list
		for (int page : occurrences) {
			returnList.add(page);
		}
		
		//Return the populated list
		return returnList;
	}
	
	/**
	 * Get this ConcordanceDataElement's word parameter
	 * @return the CDE's word
	 */
	public String getWord() {
		return this.word;
	}
	
	/**
	 * Get the hash code for this object. NOTE that the hash will need reformatted to fit a given
	 * hash table that it will be entered into
	 * @return the hash code for this object as an integer
	 */
	public int hashCode() {
		return (word.hashCode() < 0 ? word.hashCode() * -1 : word.hashCode()); //Force a positive number return
		
		/*
		 * commenting all this out as per instructions I can use the String hashcode
		int hash = 0;
	
		
		for (int i = 0; i < word.length(); i++) {
			
			//For each character in the string, multiply its ASCII value by its position in  
			//the string and sum
			hash += word.charAt(i) * (i+1);
			
			//If the hash accidentally overflows and goes negative,
			//add the max integer value to get it back into range
			if (hash < 0) hash += Integer.MAX_VALUE;
		}
		
		return hash;
		*/
	}
	
	/**
	 * Convert the ConcordanceDataElement to a String
	 * @return a String representation of the CDE
	 */
	@Override
	public String toString() {
		//Generate the return string
		String returnString = word + ":";
		
		//Append each page that the word appears on
		for (int i = 0; i < occurrences.length; i ++) {
			returnString += " " + occurrences[i] + ",";
		}
		
		//Remove the trailing comma following the final iteration of the for loop
		returnString = returnString.substring(0, returnString.length() - 1);
		
		//Return the formatted String
		return returnString;
	}
	
	/**
	 * Check if a page number is unique in the occurrences list
	 * @param num the page number to be checked
	 * @return true if the page number does not already occur in the occurrences list, and false otherwise
	 */
	private boolean isUnique(int num) {
		
		for (int i = 0; i < nextIndex; i++) {
			if (num == occurrences[i]) return false;
		}
		
		return true;
	}
	
	/**
	 * Check if the occurrences array is currently full
	 * @return true if there is no more room in the occurrences array, and false otherwise
	 */
	private boolean isFull() {
		
		if (nextIndex == occurrences.length - 1) return true;
		
		else return false;
	}
	
	/**
	 * Expand the size of the occurrences array if it becomes full.
	 * The new occurrences array has five more available indices than the previous one.
	 */
	private void expand() {	
		
		//Create a new empty array
		int[] newArray = new int[occurrences.length + 1];
		for (int i = 0; i < occurrences.length; i++) {
			
			//Copy the integers from the occurrences array to the new array
			newArray[i] = occurrences[i];
		}
		
		//Assign the new array as the new occurrences array
		occurrences = newArray;
	}
	
	/**
	 * Insert an integer into the occurrences array in sorted order
	 * @param num the integer to be added
	 */
	private void insert(int num) {
		//iterate through the occurrences array to find the first integer greater than the to-add integer
		for (int i = 0; i < nextIndex; i++) {
			if (occurrences[i] > num) {
				
				//Promote all the following integers to their next index to make room for the to-add integer
				for (int j = nextIndex; j > i; j--) {
					occurrences[j] = occurrences[j-1];
				}
				
				//Insert the to-add integer in the opened index
				occurrences[i] = num;
			}
		}
	}
	
}
