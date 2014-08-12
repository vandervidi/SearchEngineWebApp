package il.ac.shenkar.SearchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

			// Vidrans connection
			// connection = DriverManager.getConnection(
			// "jdbc:mysql://localhost/searchengine", "root", "");

			// Ofirs connection
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost/searchengine", "jaja", "gaga");

			// creating the 'index file' table
			//initTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// getter for MysqlConnecor instance
	public static MysqlConnector getInstance() {
		return INSTANCE;
	}

	public void initTable() {
		createTable_postingFile();
		createTable_indexFile();
	}

	// This function creates,if doesnt exits, a new 'index File' table
	public void createTable_indexFile() {
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
			// TODO Auto-generated catch block
			try {
				// Clear the table that already exists
				statement.executeUpdate("TRUNCATE TABLE indexFile;");
				System.out
						.println("This table already exist! Clearing the existing one...");
				statement.close();

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	// This function creates,if doesnt exits, a new 'index File' table
	public void createTable_postingFile() {
		String createPostingFileTable = "CREATE TABLE postingFile ("
				+ "        docPath VARCHAR(300) NOT NULL, "
				+ "        docNumber INT(4) KEY AUTO_INCREMENT    )";

		try {
			statement = connection.createStatement();
			statement.executeUpdate(createPostingFileTable);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				// Clear the table that already exists
				statement.executeUpdate("TRUNCATE TABLE postingFile;");
				System.out
						.println("This table already exist! Clearing the existing one...");
				statement.close();

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	// This function inserts a new row into the 'index File' table
	public void insert_postingFile(String path) throws SQLException {
		PreparedStatement prepstate = connection
				.prepareStatement("INSERT INTO `postingFile` (`docPath`) "
						+ "VALUES (?)");
		prepstate.setString(1, path);
		prepstate.execute();
		prepstate.close();

		//System.out.println("Add " + path + " to posting file completly.");
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
						+ "		SELECT word,docNumber,freq,hits "
						+ "		FROM tsum;");

		// Deleting the temporary table we used
		statement.executeUpdate("DROP TEMPORARY TABLE IF EXISTS tsum;");

		statement.close();
	}

	// remove all the words from DB that corresponding to a file
	public void removeFileWords(int docNum) throws SQLException {
		statement = connection.createStatement();
		statement.executeUpdate("DELETE FROM indexFile "
				+ "		WHERE docNumber =" + docNum + ";");
		System.out.println("\nStep 1/2 - removed file from the posting file ,in index: "+docNum);
		statement.close();
	}

//	public List<RowElement> searchWord(String q) throws SQLException {
//		List<RowElement> rows = new ArrayList<>();
//		statement = connection.createStatement();
//
//		String query = "SELECT word, docNumber, freq "
//				+ "FROM indexFile WHERE word ='" + q + "' ORDER BY freq ASC;";
//
//		try {
//
//			ResultSet rs = statement.executeQuery(query);
//			while (rs.next()) {
//				String resultWord = rs.getString("word");
//				int resultDocNum = rs.getInt("docNumber");
//				int resultFreq = rs.getInt("freq");
//
//				rows.add(new RowElement(resultWord, resultDocNum, resultFreq));
//				System.out.println(resultWord + "\t" + resultDocNum + "\t"
//						+ resultFreq);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return rows;
//	}

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
			System.out.println("Posting file table currently doesnt have any files");
		}
		return 0;
	}

	// check if work
	public String getFilePath_by_docNumber_postingFile(int docNum)
			throws SQLException {
		statement = connection.createStatement();
		String query = "SELECT docPath"
				+ "		FROM postingFile "
				+ "		WHERE docNumber ='" + docNum + "'";
		try {
			ResultSet rs = statement.executeQuery(query);
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// remove the row from DB that have this docNumber
	public void removeDocRow_by_number_postingFile(int docNum)
			throws SQLException {
		statement = connection.createStatement();
		statement.executeUpdate("DELETE FROM postingFile "
				+ "		WHERE docNumber =" + docNum + ";");
		System.out.println("step 2/2 - remove the row from DB that have this docNumber ");
		statement.close();
		System.out.println("Removing completed");
	}

	public void insert_file_to_db_if_doesnt_exists(String path)
			throws SQLException, IOException {
		// Fix path (ex. c:\\abc\\file...)
		String pathFix = path.replace("\\", "\\\\");
		// check if a path already exists in the table
		statement = connection.createStatement();
		String query = "SELECT docPath " + "		FROM postingFile "
				+ "		WHERE docPath ='" + pathFix + "'";
		ResultSet rs = statement.executeQuery(query);

		rs.last();
		int equalNumRows = rs.getRow();
		statement.close();
		// If the path does not exist in the PostingFile table, add it.
		if (equalNumRows == 0) {
			insert_postingFile(path);

			// get file docNum by path
			int docNum = get_docNum_by_path_from_postingFile_table(path);

			// insert to indexFile table
			parseFile_and_add_to_index_file_table(path, docNum);
			
			System.out.println("\nAdd completely - "+path);
		}
	}

	public int get_docNum_by_path_from_postingFile_table(String path)
			throws SQLException {
		path = path.replace("\\", "\\\\");
		statement = connection.createStatement();
		String query = "SELECT * FROM `postingFile` WHERE `docPath` ='"	+ path + "';";
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
		String everything = null;  // The complete text from the file
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
		everything = everything.replaceAll("(?<!\\d)\\.|\\.+$|[^a-zA-Z0-9. ]", " ");
		//everything = everything.replaceAll("(?<=\\d)\\.(?!\\d)|(?<!\\d)\\.|[^a-zA-Z0-9. ]", " ");
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
	public void check_if_all_file_exists_by_posting_table_paths() throws SQLException {
		
		/* check if all path's are still valid */
		// Get all rows from postingFile table
		statement = connection.createStatement();
		String query = "SELECT * FROM `postingFile`";
		ResultSet rs = statement.executeQuery(query);
		
		List<Integer> docNumbersToDelete = new ArrayList<Integer>();
		while (rs.next()){
			
			File f = new File(  rs.getString("docPath")  );
			// if not valid -> Remove file from list
			if (!f.exists()){
				// Save all document numbers of paths that are not valid
				docNumbersToDelete.add( rs.getInt("docNumber") );
			}
		}
		statement.close();
		for (int i=0 ; i<docNumbersToDelete.size(); i++){
			//remove all words from DB that Attributed to this path file (by filenumber)
			removeFileWords(docNumbersToDelete.get(i));
			
			//remove the file path from the posting file
			removeDocRow_by_number_postingFile(docNumbersToDelete.get(i));
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

	public Iterator getResult(String searchQuery) throws SQLException, IOException {
		List<Integer> docNumList = new ArrayList<Integer>();
		List<FileDescriptor> fileDesList = new ArrayList<FileDescriptor>();
		
		String selectSQL = "SELECT * FROM indexfile WHERE word=?";
		PreparedStatement prepstate = connection.prepareStatement (selectSQL);
        prepstate.setString(1, searchQuery);
        ResultSet rs = prepstate.executeQuery();

		while (rs.next()) {
			docNumList.add(rs.getInt("docNumber"));	
		}
		prepstate.close();
		
		for(int i=0; i<docNumList.size(); i++){
			selectSQL = "SELECT * FROM postingFile WHERE docNumber=?";
			prepstate = connection.prepareStatement (selectSQL);
	        prepstate.setInt(1, docNumList.get(i));
	        rs = prepstate.executeQuery();
			
			while (rs.next()) {
				// Read file
				File f = new File(rs.getString("docPath"));
				BufferedReader br = new BufferedReader(new FileReader(f.getPath()));
				//StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				int counter =0;
				FileDescriptor fileDes = new FileDescriptor();
				while (line != null) {
					// if line number is #0 | #1 | #2 - remove '#' from line variable 
					/* (0) - Title
					 * (1) - Creation Date
					 * (2) - Author
					 * (3) - Preview
					 * (4) - Content
					 */
					switch (counter) {
					case 0: line = line.substring(1);
							fileDes.setTitle(line);
						break;
					case 1: line = line.substring(1);
							fileDes.setCreationDate(line);
						break;
					case 2: line = line.substring(1);
							fileDes.setAuthor(line);
						break;
					case 3: line = line.substring(1);
							fileDes.setPreview(line);
						break;
					default:
						fileDes.appendContent(line);
						//fileDes.getContent().append(System.lineSeparator());
						break;
					}
					counter++;
					line = br.readLine();
				}
				br.close();
				
				fileDesList.add(fileDes);
				
				System.out.println(fileDes.toString());
				System.out.println("Content: "+fileDes.getContent().toString());
			}
		}
		return fileDesList.iterator();
	}
}
