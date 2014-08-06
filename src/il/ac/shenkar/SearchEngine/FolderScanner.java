package il.ac.shenkar.SearchEngine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class FolderScanner implements Runnable{
	private static final FolderScanner INSTANCE = new FolderScanner();
	private  MysqlConnector ms;
	
	private FolderScanner() {
		super();
		ms = MysqlConnector.getInstance();
	}

	//initializing Posting File structure
	//List <PostingFileElement> postingFile = new ArrayList<>();

	@Override
	public void run() {
		File folder = new File( System.getProperty("user.dir")+"\\db" );
		
		while (true){
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
			    if (file.isFile()) {
			    	try {
						
		    		ms.insert_to_postingFile_if_doesnt_exists(file.getPath());
		    		
		    		
		    		// check if all path's are still valid
//					for (int i=0; i<ms.checkNumRows_postingFile(); i++){
//						File f = new File(ms.getFilePath_by_docNumber_postingFile(i));
//						// if not valid -> Remove file from list
//						if (!f.exists()){
//							
//							//remove all words from DB that Attributed to this path file (by file number)
//							int docNum_toRemove = i;
//							try {
//								ms.removeFileWords(docNum_toRemove);
//							} catch (SQLException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//							
//							//remove the file path from the posting file
//							System.out.println("Step 2/2 - removed - "+ms.getFilePath_by_docNumber_postingFile(i)+" from the posting file ,in index: "+i);
//							ms.removeDocRow_by_number_postingFile(i);
//						}			    		
//						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			}
			
			
			ms.check_if_a_file_exists();
			
			if (listOfFiles.length==0) {
			// Clear DB table
				try {
					Statement statement = ms.connection.createStatement();
					statement.executeUpdate("TRUNCATE TABLE indexFile;");
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			// Clear Posting file
				try {
					Statement statement = ms.connection.createStatement();
					statement.executeUpdate("TRUNCATE TABLE postingFile;");
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	//Getters and setters
	public static FolderScanner getInstance() {
        return INSTANCE;
    }
	public MysqlConnector getMs() {
		return ms;
	}

	public void setMs(MysqlConnector ms) {
		this.ms = ms;
	}
	
	
}
