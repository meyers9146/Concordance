//TODO: make new expand method for over-running the hash table
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

public class ConcordanceDataStructure implements ConcordanceDataStructureInterface{
	
	private String title;
	private LinkedList<ConcordanceDataElement>[] hashTable;
	
	/**
	 * Create a ConcordanceDataStructure with an estimated number of words to be hashed
	 * @param num the estimated number of words to be input and hashed
	 */
	@SuppressWarnings("unchecked") //Cast is safe as all uses will be of the CDE object type
	public ConcordanceDataStructure(int num) {
		this.title = "Untitled";
		
		//Set the hash table at the next 4k+3 prime number after a loading factor of 1.5
		hashTable = (LinkedList<ConcordanceDataElement>[]) new LinkedList<?>[next4kPlus3((int)(num / 1.5))];
		
		//Populate array with empty LinkedLists
		for (int i = 0; i < hashTable.length; i++) {
			hashTable[i] = new LinkedList<ConcordanceDataElement>();
		}
	}
	
	/**
	 * Create a ConcordanceDataStructure with a set name and hash table size
	 * @param test the name to apply to the CDS
	 * @param size the size at which to set the hash table
	 */
	@SuppressWarnings("unchecked") //Cast is safe as all uses will be of the CDE object type
	public ConcordanceDataStructure(String test, int size) {
		this.title = test;
		this.hashTable = (LinkedList<ConcordanceDataElement>[]) new LinkedList<?>[size];
		
		//Populate array with empty LinkedLists
		for (int i = 0; i < hashTable.length; i++) {
			hashTable[i] = new LinkedList<ConcordanceDataElement>();
		}
	}
	
	/**
	 * Add a new item to the concordance
	 * @param term the term to be added to the concordance
	 * @param lineNum the line where the term occurs
	 */
	@Override
	public void add(String term, int lineNum) {
		
		//Clean up the term by removing unwanted punctuation
		term = cleanUp(term);
		
		//Ignore common or too-short words
		if (isInvalid(term)) return;
		
		//Create a new CDE with the provided information
		ConcordanceDataElement newCDE = new ConcordanceDataElement(term, lineNum);
		
		//Create a hash code from the created CDE. If the hash is negative, increase it to fit in the table
		int newHash = newCDE.hashCode() % hashTable.length;
		
		//Go to the hash index and check the elements present in the LinkedList at that index
		//If the LinkedList at the hash index is empty, directly add the new CDE
		if(hashTable[newHash].isEmpty()) hashTable[newHash].add(newCDE);
		
		//If the LinkedList at the hash index is populated, check if the new CDE is new to the LinkedList.
		//If the new CDE is unique, it is added.
		//Otherwise, the new line number is added to the pre-existing CDE that matches the term
		else if(isUnique(newCDE)) {
			
			//If the LinkedList is NOT empty, add the new CDE at the index before an entry greater than the CDE
			//to maintain sorting
			boolean added = false; //Flag to indicate if the CDE gets added in the next step
			
			for (int i = 0; i < hashTable[newHash].size(); i++) {
				
				//If an entry in the LinkedList is greater than the to-add CDE, add the CDE before the greater entry
				if (hashTable[newHash].get(i).getWord().compareTo(newCDE.getWord()) > 0) {
					hashTable[newHash].add(i, newCDE);
					added = true;
					break;
				}
			}//end for loop
			
			//If the CDE is greater than all items in the LinkedList, add the CDE to the end
			if (added == false) {
				hashTable[newHash].add(newCDE);
			}
		}
		
		//If the entry is NOT unique, find the entry in the hash table and add the new page number to the existing CDE
		else {
			//Locate the existing CDE in the hash table
			for (int i = 0; i < hashTable[newHash].size(); i++) {
				if (hashTable[newHash].get(i).getWord().equals(newCDE.getWord())) {
					//When the entry is found, have that entry add the page number
					hashTable[newHash].get(i).addPage(lineNum);
				}
			}
		}
	}
	
	/**
	 * Remove unwanted punctuation from a to-add term
	 * @param term the term to be cleaned up
	 * @return a String of the term with unwanted punctuation removed
	 */
	private String cleanUp(String term) {
		//Go through the term and remove punctuation as found, replacing with whitespace
		for(int i = 0; i < term.length(); i++) {
			if (!(Character.isLetterOrDigit(term.charAt(i)))) {
				if (!(term.charAt(i) == '\'')) {
					term = term.replace(term.charAt(i), ' ');
				}
			}
		}
		
		//Remove the whitespace characters added as the result of the above operation
		term = term.replaceAll(" ", "");
		
		//Return the modified term
		return term;
	}

	/**
	 * Check if a term is able to be included in the concordance
	 * @param term the term to be validated
	 * @return true if the term is INVALID, and should be excluded from the concordance
	 */
	private boolean isInvalid(String term) {
		//Create a String containing common words for removal
		String invalids = "and the";
		
		//Remove whitespace
		term = term.trim();
		
		//Check if the trimmed term contains any words from the invalid string
		if (invalids.contains(term)) return true;
		
		//Check if the trimmed term is shorter than 3 characters in length
		else if (term.length() < 3) return true;
		
		//If the above tests are passed, return false
		else return false;
	}

	/**
	 * Verify if a given Concordance Data Element is unique in the data structure
	 * @param newCDE the CDE to verify
	 * @return true if there is no occurrence of the CDE parameter in the data structure, and false otherwise
	 */
	private boolean isUnique(ConcordanceDataElement newCDE) {
		//Get the CDE's hash code to search the table
		int hashCode = newCDE.hashCode() % hashTable.length;
		
		//Look through the associated LinkedList at the indicated hash
		for (int i = 0; i < hashTable[hashCode].size(); i++) {
			if (hashTable[hashCode].get(i).getWord().equals(newCDE.getWord())) return false;
		}
		
		//Return true if the above loop concludes without finding a matching term
		return true;
	}

	/**
	 * From a starting integer, find the next integer value that satisfies the 4k+3 prime test
	 * @param fourKPlus3PrimeTest the starting integer
	 * @return the next prime number that is 3 more than a multiple of four
	 * @throws ArithmeticException if no 4k+3 prime is found within the integer range
	 */
	private int next4kPlus3(int fourKPlus3PrimeTest) throws ArithmeticException{
		
		boolean fourKPlus3Found = false;
		
		while (fourKPlus3Found == false && fourKPlus3PrimeTest != Integer.MAX_VALUE) {
			fourKPlus3PrimeTest++;
			if (((fourKPlus3PrimeTest - 3) % 4 == 0) && isPrime(fourKPlus3PrimeTest)) {
				fourKPlus3Found = true;
				return fourKPlus3PrimeTest;
			}
		}
		
		//If no new Prime is found, throw ArithmeticException
		//This should only happen if the integer value overflows
		throw new ArithmeticException("No 4k+3 Prime was found");
	}
	
	/**
	 * Check if a number is prime by dividing it by all numbers between 2 and its square root
	 * @param num the number to be checked
	 * @return true if the number is prime, and false otherwise
	 */
	private boolean isPrime(int num) {
		
		boolean isPrimeNumber = true;
		
		//Check if the number is evenly divisible by the numbers preceding it
		//Test goes from 2 to the square root of the number
		for(int i = 2; (i * i) < num; i++) {
			if (num % i == 0) {
				isPrimeNumber = false;
				break;
			}
		}
		
		return isPrimeNumber;
	}
	
	/**
	 * Get a list of the page numbers for the terms at a given index in the hash table
	 * @param index the index form which to generate the list of page numbers
	 * @return an ArrayList containing all page numbers for all terms at the given index
	 */
	@Override
	public ArrayList<LinkedList<Integer>> getPageNumbers(int index) {
		
		//Create empty ArrayList for returning
		ArrayList<LinkedList<Integer>> returnList = new ArrayList<LinkedList<Integer>>();
		
		//Access the indicated index of the hash table and add term's page number list to the ArrayList
		for (int i = 0; i < hashTable[index].size(); i++) {
			returnList.add(hashTable[index].get(i).getList());
		}
		
		//Return the completed ArrayList
		return returnList;
	}

	/**
	 * Get the size of the Structure's hash table
	 */
	@Override
	public int getTableSize() {
		return hashTable.length;
	}

	/**
	 * Get a list of the words entered at the given index of the hash table
	 * @param index the index of the hash table
	 * @return an ArrayList of each word in the given hash table bucket
	 */
	@Override
	public ArrayList<String> getWords(int index) {
		//Create empty ArrayList for returning
		ArrayList<String> returnList = new ArrayList<>();
		
		for(int i = 0; i < hashTable[index].size(); i++) {
			returnList.add(hashTable[index].get(i).getWord());
		}
		
		//Return populated ArrayList
		return returnList;
	}

	/**
	 * Display all words in the ConcordanceDataStructure
	 * @return an ArrayList containing all words and their page occurrences
	 */
	@Override
	public ArrayList<String> showAll() {

		//Create SortedDoubleLinkedList for returning
		SortedDoubleLinkedList<String> sortedList = new SortedDoubleLinkedList<>(new Comparator<String>() {
																					@Override //This is messy, but it compares
																					//Strings without involving the apostrophe
																					public int compare(String s1, String s2) {
																						return s1.replaceAll("'", "").compareTo(
																								s2.replaceAll("'", ""));
																					}
																				}); 
		
		//Add each item in the hashTable to the sortedList. Each term will be automatically sorted in alphabetical ascending order
		for(int i = 0; i < hashTable.length; i++) {
			for(int j = 0; j < hashTable[i].size(); j++) {
				sortedList.add(hashTable[i].get(j).toString() + "\n");
			}
		}
		
		//Convert the sorted list to an ArrayList<String> for returning
		return sortedList.toArrayList();
	}

}

