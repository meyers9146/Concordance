import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ConcordanceDataManager implements ConcordanceDataManagerInterface {

	/**
	 * Display the words of a concordance in alphabetical order, with one word per line
	 * @param input a String (one line, or multiple) from which to create a concordance
	 * @return an ArrayList of Strings. Each String element is one word, followed by its line occurrences,
	 * followed by a newline character
	 */
	@Override
	public ArrayList<String> createConcordanceArray(String input) {
		
		//Create scanner and variables
		Scanner scanner = new Scanner(input); //Scanner to read file
		String[] tokens; //Array to hold the line split into tokens
		int lineCounter = 1; //Counter to track number of read lines
		
		//Check number of lines to estimate starting CDS size
		String[] wordCount = input.split("\\s+");
		
		//Create CDS to process data
		ConcordanceDataStructure cds = new ConcordanceDataStructure(wordCount.length);
		
		while(scanner.hasNextLine()) { //Read the line in entirety
			tokens = scanner.nextLine().split(" "); //Create a String array of each word in the line
			for (String token : tokens) {
				cds.add(token, lineCounter); //Add each word individually to the CDS
			}
			lineCounter++; //Increment the line counter to track to the next line
		}
		
		//Close scanner at end of file
		scanner.close();
		
		//Convert the cds to an ArrayList<String> for returning
		return cds.showAll();
	}

	/**
	 * Read a text file, create a concordance from it, and then create a file holding the concordance output
	 * @param input the file to be read (.txt format)
	 * @param output the name and filepath for the output file
	 * @return true if the operation was successful, false if not
	 * @throws FileNotFoundException if the specified input file was not found
	 */
	@Override
	public boolean createConcordanceFile(File input, File output) throws FileNotFoundException {
		
		//Create a String to hold file contents
		String fileContents = "";
		
		try {
			//Open scanner to read file
			Scanner scanner = new Scanner(input);
			
			//Read each line into the string
			while (scanner.hasNextLine()) {
				fileContents += scanner.nextLine() + "\n"; 
			}
			
			//Close scanner at end of file
			scanner.close();
		}
		catch(FileNotFoundException e) {
			throw new FileNotFoundException("The specified file was not found");
		}
		
		//Convert input String into a concordance ArrayList
		ArrayList<String> concordance = createConcordanceArray(fileContents);
		
		//Create output stream for file output
		PrintWriter pw = new PrintWriter(output);
		
		//Read each line in the concordance into the output file
		for(String line : concordance) {
			pw.print(line);
		}
		
		//Close PrintWriter at the end of writing
		pw.close();
		
		//Return true on successful completion
		return true;
	}

}
