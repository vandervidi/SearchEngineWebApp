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
			try {
				for (File file : listOfFiles) {
				    if (file.isFile()) {
				    	ms.insert_file_to_db_if_doesnt_exists(file.getPath());	
				    }
				}
			
			
				ms.check_if_all_file_exists_by_posting_table_paths();
				
				if (listOfFiles.length==0) {
					// Clear DB tables
					ms.clear_db_tables();
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
