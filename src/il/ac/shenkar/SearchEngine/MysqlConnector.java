package il.ac.shenkar.SearchEngine;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/* This class is responsible for:
 * 1.Initializing a connection to the MySQL server
 * 2.Manages functionality on the 'index file' table 
 */

public class MysqlConnector {
	private static final MysqlConnector INSTANCE = new MysqlConnector();

	Connection connection  = null;
	Statement statement = null;
	
	
	private MysqlConnector() {
		try {
			System.out.println("MysqlConnector CTOR()");
			//initializing connection to the database
			Class.forName("com.mysql.jdbc.Driver");
			
			//Vidrans connection
//			connection = DriverManager.getConnection(
//					"jdbc:mysql://localhost/searchengine", "root", "");
			
			// Ofirs connection
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost/searchengine", "jaja", "gaga");
			
			
			//creating the 'index file' table
			initTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//getter for MysqlConnecor instance
	public static MysqlConnector getInstance() {
        return INSTANCE;
    }
	
	//This function creates,if doesnt exits, a new 'index File' table
	public void initTable(){
        String createIndexTable = "CREATE TABLE indexFile ("
                                +"        word VARCHAR(30) NOT NULL, "
                                +"        docNumber INT(4) NOT NULL, "
                                +"        freq INT(4) NOT NULL "
                                +"        )";

        try {
                statement = connection.createStatement();
                statement.executeUpdate(createIndexTable);
                statement.close();
        } catch (SQLException e) {
                // TODO Auto-generated catch block
                try {
                	//Clear the table that already exists
            		statement.executeUpdate("TRUNCATE TABLE indexFile;");
					System.out.println("This table already exist! Clearing the existing one...");
					statement.close();
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        }
	}
	//This function inserts a new row into the 'index File' table
	public void insert(String word, int docNum, int freq) throws SQLException{
		PreparedStatement prepstate =connection.prepareStatement
                ("INSERT INTO `indexFile` (`word`, `docNumber`, `freq`) "
                                + "VALUES (?, ?, ?)");
                prepstate.setString(1, word);
                prepstate.setInt(2, docNum);
                prepstate.setInt(3, freq);
       
                prepstate.execute();
                prepstate.close();
	}
	
	//This function is executed when we want to sord the 'index file' database tabl by the 'word' column
	public void sortByWord() throws SQLException{
	
		statement = connection.createStatement();
		statement.execute(
				"ALTER TABLE `indexFile` ORDER BY word ASC;"
				);
        statement.close();   
	}
	
	/* This function removes duplicate (word + document number) sets
	 * Before deleting it sums the 'freq' column
	 */
	public void removeDuplicate() throws SQLException {
		statement = connection.createStatement();
		//Creating a temporary table that will store a table without duplicate rows.
		statement.executeUpdate(
				"CREATE temporary TABLE tsum AS"  
				+"		SELECT word, docNumber, SUM(freq) as freq "
				+"		FROM indexfile group by word, docNumber;"
				);
		//Clearing completely the 'index File' table
		statement.executeUpdate("TRUNCATE TABLE indexFile;");
		
		//Inserting into the empty 'index file' table all rows from the temporary table
		statement.executeUpdate(
				"INSERT INTO indexFile (`word`,`docNumber`,`freq`)"
				+"		SELECT word,docNumber,freq"
				+"		FROM tsum;"
				);
		
		//Deleting the temporary table we used
		statement.executeUpdate(
				"DROP TEMPORARY TABLE IF EXISTS tsum;"
				);
		

        statement.close();  
        System.out.println("close-statement");
	}
	
	// remove all the words from DB that corresponding to a file 
	public void removeFileWords(int docNum) throws SQLException {
		statement = connection.createStatement();
		statement.executeUpdate(
				"DELETE FROM indexFile "
		        +"		WHERE docNumber ="+docNum+";"
				);
        System.out.println("step 1/2 - removed all words from the database table ");
		statement.close();  
        System.out.println("close-statement");
		System.out.println("Removing completed");
	}
	
	public List<RowElement> searchWord(String q) throws SQLException{
		List <RowElement> rows = new ArrayList<>();
		statement = connection.createStatement();
		
		String query =
		        "SELECT word, docNumber, freq " +
		        "FROM indexFile WHERE word ='"+q+"' ORDER BY freq ASC;";

		    try {
		       
		        ResultSet rs = statement.executeQuery(query);
		        while (rs.next()) {
		            String resultWord = rs.getString("word");
		            int resultDocNum = rs.getInt("docNumber");
		            int resultFreq = rs.getInt("freq");
		            
		            rows.add(new RowElement(resultWord, resultDocNum, resultFreq));
		            System.out.println(resultWord + "\t" + resultDocNum +
		                               "\t" + resultFreq);
		        }
		    } catch (SQLException e ) {
		    	e.printStackTrace();
		    }
		
		
		
		return rows;
	}
	
}
