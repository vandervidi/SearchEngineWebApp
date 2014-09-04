package il.ac.shenkar.SearchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* This class is responsible for:
 * 1.Initializing a connection to the MySQL server
 * 2.Manages functionality on the 'index file' table 
 */

public class MysqlConnector {
	private static final MysqlConnector INSTANCE = new MysqlConnector();

	Connection connection = null;
	Statement statement = null;

	private MysqlConnector() {
		try {
			// System.out.println("MysqlConnector CTOR()");
			// initializing connection to the database
			Class.forName("com.mysql.jdbc.Driver");

			connection = DriverManager.getConnection("jdbc:mysql://localhost/searchengine", "root", "");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// getter for MysqlConnecor instance
	public static MysqlConnector getInstance() {
		return INSTANCE;
	}

	public void initTables() throws SQLException {
		createTable_postingFile();
		createTable_indexFile();
		// check tables if exist
		// statement = connection.createStatement();
		// String query = "SELECT 1 FROM postingFile LIMIT 1";
		// ResultSet rs = statement.executeQuery(query);
		//
		// rs.last();
		// int equalNumRows = rs.getRow();
		// statement.close();
		// // If the path does not exist in the PostingFile table, add it.
		// if (equalNumRows == 0) {
		// createTable_postingFile();
		// createTable_indexFile();
		//
		// //System.out.println("\nCreate seccessfully the tables.");
		// }
	}

	// This function creates,if doesnt exits, a new 'index File' table
	public void createTable_indexFile() throws SQLException {
		String createIndexTable = "CREATE TABLE indexFile ("
				+ "        word VARCHAR(30) NOT NULL, "
				+ "        docNumber INT(4) NOT NULL, "
				+ "        freq INT(4) DEFAULT 0, "
				+ "		   hits INT(4) DEFAULT 0 )";

		try {
			statement = connection.createStatement();
			statement.executeUpdate(createIndexTable);
			statement.close();
		} catch (SQLException e) {
			// e.printStackTrace();
			// check table if exist
			statement = connection.createStatement();
			String query = "SELECT 1 FROM postingFile LIMIT 1";
			ResultSet rs = statement.executeQuery(query);

			rs.last();
			int equalNumRows = rs.getRow();
			statement.close();

			if (equalNumRows == 1) {
				System.out.println("\nindexFile table already exist!");
			}
		}
	}

	// This function creates,if doesnt exits, a new 'index File' table
	public void createTable_postingFile() throws SQLException {
		String createPostingFileTable = "CREATE TABLE postingFile ("
				+ "        docPath VARCHAR(300) NOT NULL, "
				+ "        docNumber INT(4) KEY AUTO_INCREMENT,"
				+ "		   deleted INT(1) DEFAULT 0, "
				+ "		   lastIndex BIGINT DEFAULT 0 )";    

		try {
			statement = connection.createStatement();
			statement.executeUpdate(createPostingFileTable);
			statement.close();
		} catch (SQLException e) {
			statement = connection.createStatement();
			String query = "SELECT 1 FROM postingFile LIMIT 1";
			ResultSet rs = statement.executeQuery(query);

			rs.last();
			int equalNumRows = rs.getRow();
			statement.close();

			if (equalNumRows == 1) {
				System.out.println("\npostingFile table already exist!");
			}
		}
	}

	public void clear_tables() throws SQLException {
		clear_table_indexFile();
		clear_table_postingFile();
	}

	public void clear_table_indexFile() throws SQLException {
		// Clear the table that already exists
		statement.executeUpdate("TRUNCATE TABLE indexFile;");
		System.out
				.println("This table already exist! Clearing the existing one...");
		statement.close();
	}

	public void clear_table_postingFile() throws SQLException {
		// Clear the table that already exists
		statement.executeUpdate("TRUNCATE TABLE postingFile;");
		System.out
				.println("This table already exist! Clearing the existing one...");
		statement.close();
	}

	// This function inserts a new row into the 'index File' table
	public void insert_postingFile(String path, long lastModified) throws SQLException {
		PreparedStatement prepstate = connection
				.prepareStatement("INSERT INTO `postingFile` (`docPath`, lastIndex) "
						+ "VALUES (?, ?)");
		prepstate.setString(1, path);
		prepstate.setLong(2, lastModified);
		prepstate.execute();
		prepstate.close();

		// System.out.println("Add " + path + " to posting file completly.");
	}

	// This function inserts a new row into the 'index File' table
	public void insert_indexFile(String word, int docNum, int freq)
			throws SQLException {
		PreparedStatement prepstate = connection
				.prepareStatement("INSERT INTO `indexFile` (`word`, `docNumber`, `freq`) "
						+ "VALUES (?, ?, ?)");
		prepstate.setString(1, word);
		prepstate.setInt(2, docNum);
		prepstate.setInt(3, freq);

		prepstate.execute();
		prepstate.close();
	}

	// This function is executed when we want to sort the 'index file' database
	// table by the 'word' column
	public void sortByWord() throws SQLException {
		statement = connection.createStatement();
		statement.execute("ALTER TABLE `indexFile` ORDER BY word ASC;");
		statement.close();
	}

	/*
	 * This function removes duplicate (word + document number) sets Before
	 * deleting it sums the 'freq' column
	 */
	public void removeDuplicate() throws SQLException {
		statement = connection.createStatement();
		// Creating a temporary table that will store a table without duplicate
		// rows.
		statement.executeUpdate("CREATE temporary TABLE tsum AS"
				+ "		SELECT word, docNumber, SUM(freq) as freq , hits "
				+ "		FROM indexfile group by word, docNumber;");
		// Clearing completely the 'index File' table
		statement.executeUpdate("TRUNCATE TABLE indexFile;");

		// Inserting into the empty 'index file' table all rows from the
		// temporary table
		statement
				.executeUpdate("INSERT INTO indexFile (`word`,`docNumber`,`freq`, `hits`)"
						+ "		SELECT word,docNumber,freq,hits " + "		FROM tsum;");

		// Deleting the temporary table we used
		statement.executeUpdate("DROP TEMPORARY TABLE IF EXISTS tsum;");

		statement.close();
	}

	// remove all the words from DB that corresponding to a file
	public void removeFileWords(int docNum) throws SQLException {
		statement = connection.createStatement();
		statement.executeUpdate("DELETE FROM indexFile "
				+ "		WHERE docNumber =" + docNum + ";");
		System.out
				.println("Step 1/2 - removed file from the posting file ,in index: "
						+ docNum);
		statement.close();
	}


	public int checkNumRows_postingFile() throws SQLException {
		int counter = 0;
		statement = connection.createStatement();

		// String query = "SELECT COUNT(*) FROM  `postingfile`;"
		String query = "SELECT * FROM  `postingfile`;";
		try {
			ResultSet rs = statement.executeQuery(query);
			rs.last();
			int numRows = rs.getRow();
			return numRows;
		} catch (SQLException e) {
			e.printStackTrace();
			/* currently table currently doesnt have any lines */
			System.out
					.println("Posting file table currently doesnt have any files");
		}
		return 0;
	}

	// remove the row from DB that have this docNumber
	public void deleteDocRow_by_number_postingFile(int docNum)
			throws SQLException {
		statement = connection.createStatement();
		int check = statement.executeUpdate("UPDATE postingFile "
				+ "		SET deleted=1 " + "		WHERE docNumber =" + docNum + ";");
		System.out
				.println("step 2/2 - logical delete the row from DB that have this docNumber ");
		statement.close();
		System.out.println("Removing completed");
	}

	public void insert_file_to_db_if_doesnt_exists_or_deleted_before(String path, long lastModified)
			throws SQLException, IOException {
		
		// Fix path (ex. c:\\abc\\file...)
		String pathFix = path.replace("\\", "\\\\");
		// check if a path already exists in the table
		statement = connection.createStatement();
		String query = "SELECT docPath FROM postingFile "
				+ "		WHERE docPath ='" + pathFix + "'";
		ResultSet rs = statement.executeQuery(query);

		rs.last();
		int equalNumRows = rs.getRow();
		statement.close();
		// If the path does not exist in the PostingFile table, add it.
		if (equalNumRows == 0) {
			insert_postingFile(path, lastModified);

			// get file docNum by path
			int docNum = get_docNum_by_path_from_postingFile_table(path);

			// insert to indexFile table
			parseFile_and_add_to_index_file_table(path, docNum);

			System.out.println("\nAdd completely - " + path);
		
		// The path exist in postingFile table
		//Check if this file(path) has removed from db folder (deleted==1)
		} else {
			
			statement = connection.createStatement();
			query = "SELECT docPath,docNumber	FROM postingFile "
					+ "		WHERE docPath ='" + pathFix + "' AND deleted=1";
			rs = statement.executeQuery(query);
			rs.last();
			equalNumRows = rs.getRow();

			// if there are rows that equals to a path that was deleted in the
			// past.
			if (equalNumRows != 0) {
				int docNumber = rs.getInt("docNumber");

				// Change deleted column to 0
				int check = statement.executeUpdate("UPDATE postingFile "
						+ "		SET deleted=0 " + "		WHERE docPath ='" + pathFix
						+ "';");

				statement.close();
				System.out.println("\nThis file that was deleted before - has recovered completely. \n"+path);
				
				// insert to indexFile table
				parseFile_and_add_to_index_file_table(path, docNumber);

			//the path is exist and deleted==0
			// check for changes on this file
			}else{
				
				statement = connection.createStatement();
				query = "SELECT docNumber,lastIndex	 FROM postingFile "
						+ "		WHERE docPath ='" + pathFix + "' AND deleted=0";
				rs = statement.executeQuery(query);
				if (rs.last()){
					// If file change remove words and index them again
					long lastIndex_postingFile = rs.getLong("lastIndex");
					int docNum_ToRemoveWords = rs.getInt("docNumber");
					
					if(rs!=null && lastIndex_postingFile != lastModified ){	
						System.out.println("\nDetect changes in document: "+path);
						setNew_lastModified_in_postingFile(docNum_ToRemoveWords, lastModified);
						removeFileWords(docNum_ToRemoveWords);
						parseFile_and_add_to_index_file_table(path, docNum_ToRemoveWords);
						System.out.println("Step 2/2 - Index the words again for document: "+path);
					}
				}
				statement.close();
			}
		}
	}

	private void setNew_lastModified_in_postingFile(int docNumber,long lastModified) throws SQLException {
		statement = connection.createStatement();

		// Change deleted column to 0
		int check = statement.executeUpdate("UPDATE postingFile "
					+ "		SET lastIndex="+ lastModified
					+ "		WHERE docNumber =" + docNumber);

		statement.close();
	}

	public int get_docNum_by_path_from_postingFile_table(String path)
			throws SQLException {
		path = path.replace("\\", "\\\\");
		statement = connection.createStatement();
		String query = "SELECT * FROM `postingFile` WHERE `docPath` ='" + path
				+ "';";
		ResultSet rs = statement.executeQuery(query);

		int docNumber = -1;
		rs.next();
		docNumber = rs.getInt("docNumber");

		return docNumber;
	}

	/*
	 * This function does several things: 1. Parses a text file 2. Adds every
	 * word from the file to the 'Index file' table 3. Sorts the Index file
	 * structure
	 */
	public void parseFile_and_add_to_index_file_table(String path, int docNum)
			throws IOException, SQLException {
		String everything = null; // The complete text from the file
		String words[];
		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();

		while (line != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
		everything = sb.toString();

		// Removing special characters from the text
		everything = everything.replaceAll("(?<!\\d)\\.|\\.+$|[^a-zA-Z0-9. ]",
				" ");
		// everything =
		// everything.replaceAll("(?<=\\d)\\.(?!\\d)|(?<!\\d)\\.|[^a-zA-Z0-9. ]",
		// " ");
		everything = everything.replaceAll("\r", "");
		everything = everything.replaceAll("\n", " ");
		words = everything.split(" ");
		for (String tmpWord : words) {
			tmpWord.trim();
			if (!tmpWord.equals("")) { // Makes sure we dont add empty words to
										// the index file

				// Add this word to the database
				insert_indexFile(tmpWord, docNum, 1);
			}
		}
	}

	/*
	 * This function checks if the paths from the Posting File table are valid.
	 * If not, clear it from the posting file and its words from the index file
	 */
	public void check_if_all_file_exists_by_posting_table_paths()
			throws SQLException {

		/* check if all path's are still valid */
		// Get all rows from postingFile table
		statement = connection.createStatement();
		String query = "SELECT * FROM `postingFile` WHERE deleted=0";
		ResultSet rs = statement.executeQuery(query);

		List<Integer> docNumbersToDelete = new ArrayList<Integer>();
		while (rs.next()) {

			File f = new File(rs.getString("docPath"));
			// if not valid -> Remove file from list
			if (!f.exists()) {

				// Save all document numbers of paths that are not valid
				docNumbersToDelete.add(rs.getInt("docNumber"));
			}
		}
		statement.close();
		for (int i = 0; i < docNumbersToDelete.size(); i++) {
			System.out.println("\nDetect invalid path in postingFile");
			// remove all words from DB that Attributed to this path file (by
			// filenumber)
			removeFileWords(docNumbersToDelete.get(i));

			// remove the file path from the posting file
			deleteDocRow_by_number_postingFile(docNumbersToDelete.get(i));
		}
	}

	public void clear_db_tables() throws SQLException {
		clear_indexFile_table();
		clear_postingFile_table();
	}

	public void clear_indexFile_table() throws SQLException {
		Statement statement = connection.createStatement();
		statement.executeUpdate("TRUNCATE TABLE indexFile;");
		statement.close();
	}

	public void clear_postingFile_table() throws SQLException {
		Statement statement = connection.createStatement();
		statement.executeUpdate("TRUNCATE TABLE postingFile;");
		statement.close();
	}

	public List<String> analyzeQuery(String searchQuery) throws SQLException,IOException {
		// Split by OR operator
		List<String> splitByOR = new ArrayList<String>(Arrays.asList(searchQuery.split(" OR ")));

		// Run on every element of splitByOR list and remove AND word
		// and get array of words.
		for (int i=0; i<splitByOR.size(); i++) {
			String query = splitByOR.get(i).replace(" AND ", " ");
			query = query.trim();
			splitByOR.set(i, query);
		}

		return splitByOR;
	}

	public List<Integer> getDocNumResults(List<String> splitedQueryList)
			throws SQLException {
		List<Integer> resultDocNumbers = new ArrayList<Integer>();
		List<Integer> docNumbers_ToRemove = new ArrayList<Integer>();
		List<Integer> docNumbers_ToRemoveFRom = new ArrayList<Integer>();
		for (String words : splitedQueryList) {

			/*
			 * str can be:
			 * 
			 * [victory , dog NOT big duck] 2,3 cat 4 hot , blue dog 5 pig NOT
			 * loop gorj
			 */
			// If words contains a substring "NOT"
			if (words.contains("NOT")) {
				// list - dog, big duck
				// remove 'NOT' and split
				// string(0) , string(1)
				List<String> list_of_not_parts = new ArrayList<String>(Arrays.asList(words.split(" NOT ")));

				// take string(0) split by space " "
				List<String> tmp = new ArrayList<String>(Arrays.asList(list_of_not_parts.get(0).split(" ")));
				// Get docNum by words
				docNumbers_ToRemoveFRom = getDocNumList(tmp);

				// take string(1) split by space " "
				tmp = new ArrayList<String>(Arrays.asList(list_of_not_parts.get(1).split(" ")));
				// Get docNum by words
				docNumbers_ToRemove = getDocNumList(tmp);

				for (int num : docNumbers_ToRemove) {
					int index_to_remove = docNumbers_ToRemoveFRom.indexOf(num);
					if (index_to_remove != -1) {
						docNumbers_ToRemoveFRom.remove(index_to_remove);
					}
				}
				
				// Add numbers to main list of ducument numbers
				for (int num : docNumbers_ToRemoveFRom) {
					if (resultDocNumbers.indexOf(num) == -1) {
						resultDocNumbers.add(num);
					}
				}

			// search query without NOT
			} else {
				
				List<String> tmp = new ArrayList<String>(Arrays.asList(words.split(" ")));
				
				// Get docNum by words
				List<Integer> docNumbers_to_add_if_need = getDocNumList(tmp);
				
				// Add numbers to main list of ducument numbers
				for (int num : docNumbers_to_add_if_need) {
					if (resultDocNumbers.indexOf(num) == -1) {
						resultDocNumbers.add(num);
					}
				}
			}
		}
		return resultDocNumbers;
	}

	public List<Integer> getDocNumList(List<String> stringList)
			throws SQLException {
		List<Integer> DocumentNumbers = new ArrayList<Integer>();
		StringBuilder words = new StringBuilder();
		// example of the string we are trying to create:
		// 'blue','pen','green','key'
		for (String w : stringList) {
			words.append("'" + w + "',");
		}
		// Remove the last ","
		words = words.deleteCharAt(words.length() - 1);

		int numberOfWords = stringList.size();

		statement = connection.createStatement();
		String query = "SELECT docNumber  FROM indexFile "
				+ "		WHERE word IN (" + words + ")" + "		GROUP BY docNumber"
				+ "		HAVING COUNT(DISTINCT word) = " + numberOfWords;
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
			DocumentNumbers.add(rs.getInt("docNumber"));
		}
		return DocumentNumbers;
	}

	public Iterator create_fileDescriptors_list_by_docNumbers(List<Integer> docNumbers_of_results) throws SQLException, IOException {
		List <FileDescriptor> fd = new ArrayList<FileDescriptor>();
		for( int docNum : docNumbers_of_results ){
			String selectSQL = "SELECT * FROM postingFile WHERE docNumber=?";
			PreparedStatement prepstate = connection.prepareStatement (selectSQL);
			prepstate.setInt(1, docNum);
			ResultSet rs = prepstate.executeQuery();
			
			 while (rs.next()) {
				 // Read file
				 File f = new File(rs.getString("docPath"));
				 BufferedReader br = new BufferedReader(new FileReader(f.getPath()));
				 
				 //StringBuilder sb = new StringBuilder();
				 String line = br.readLine();
				
				 int counter =0;
				 FileDescriptor fileDes = new FileDescriptor();
				 fileDes.setPath(rs.getString("docPath"));
				 while (line != null) {
					 // if line number is #0 | #1 | #2 - remove '#' from line variable
					 /* (0) - Title
					 /* (1) - Creation Date
					 /* (2) - Author
					 /* (3) - Preview 
					 */
					
					 switch (counter) {
						 case 0: line = line.substring(1);
						 String title= line.replaceAll("Title: ", "");
						 fileDes.setTitle(title);
						 break;
						 case 1: line = line.substring(1);
						 String creationDate= line.replaceAll("Creation date: ", "");
						 fileDes.setCreationDate(creationDate);
						 break;
						 case 2: line = line.substring(1);
						 String author= line.replaceAll("Author: ", "");
						 fileDes.setAuthor(author);
						 break;
						 case 3: line = line.substring(1);
						 String preview= line.replaceAll("Preview: ", "");
						 fileDes.setPreview(preview);
						 break;
					 }
					 counter++;
					 line = br.readLine();
					 if (counter==4) break;
				 }
				 br.close();
				 fd.add(fileDes);
			 }
		}
		return fd.iterator();
	}

	public String readFileContent(String filePath) throws IOException {
		// Read file
		 File f = new File(filePath);
		 BufferedReader br = new BufferedReader(new FileReader(f.getPath()));
		 
		 StringBuilder allText = new StringBuilder();
		 String line = br.readLine();
		
		 while (line != null) {
			 allText.append(line+"<br>");
			 line = br.readLine();
		 }
		 br.close();
		 return allText.toString();
	}
}
