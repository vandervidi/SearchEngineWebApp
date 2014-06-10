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
	List <PostingFileElement> postingFile = new ArrayList<>();

	@Override
	public void run() {
		while (true){
			File folder = new File("C:\\Users\\joe\\workspaceEE\\SearchEngineWebApp\\db");
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
			    if (file.isFile()) {
			    	
			    	// In case its the first file to be indexed
			    	if (postingFile.isEmpty()){
			    		postingFile.add(new PostingFileElement(file.getPath(), postingFile.size() ));
			    		System.out.println("added - " + postingFile.get(postingFile.size()-1).getM_path() + " and its Document number is: "+postingFile.get(postingFile.size()-1).getM_docNum());
			    		parseFile(file,postingFile.size()-1);
			    		
			    	// In case this is not the first file to be indexed
			    	}else{
			    		boolean exists = false;
			    		// Check if we indexed this file path before
			    		for (int i=0; i<postingFile.size(); i++){
			    			// if file path exist
					    	if (postingFile.get(i).getM_path().equals(file.getPath()) ){
					    		exists=true;
					    		break;
					    	}
			    		}
			    		// if the file does not exist , Add it to the list.
			    		if (!exists){
			    			postingFile.add(new PostingFileElement(file.getPath(), postingFile.size() ));
				    		System.out.println("added - " + postingFile.get(postingFile.size()-1).getM_path() + " and its Document number is: "+postingFile.get(postingFile.size()-1).getM_docNum());
				    		parseFile(file, postingFile.size()-1);
			    		}

			    		// check if all path's are still valid
				    	for (int i=0; i<postingFile.size(); i++){
				    		File f = new File(postingFile.get(i).getM_path());
				    		// if not valid -> Remove file from list
				    		if (!f.exists()){
					    		
					    		//remove all words from DB that Attributed to this path file (by file number)
					    		int docNum_toRemove = postingFile.get(i).getM_docNum();
					    		try {
									ms.removeFileWords(docNum_toRemove);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					    		
					    		//remove the file path from the posting file
				    			System.out.println("Step 2/2 - removed - "+postingFile.get(i).getM_path() + " from the posting file ,in index: "+postingFile.get(i).getM_docNum());
					    		postingFile.remove(i);
					    		
				    		}			    		
				    	}
			    	}
			    }
			}
			if (listOfFiles.length==0)
			{
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
				postingFile = new ArrayList();
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

/*This function does several things:
 * 1. Parses a text file
 * 2. Adds every word from the file to the 'Index file' table
 * 3. Sorts the Index file structure
 */
	private void parseFile(File file, int docNum) {
		String everything=null;
		String words[];
		try(BufferedReader br = new BufferedReader(new FileReader(file.getPath()))) {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        everything = sb.toString();
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Removing special characters from the text
		everything = everything.replaceAll("[!@#$%^&*\\[\\]\"()-=_+~:;<>?,.{}`|/]","");
		everything = everything.replaceAll("\r", "");
		everything = everything.replaceAll("\n", " ");
		words = everything.split(" ");
		for(String tmpWord : words){
			tmpWord.trim();
			if (!tmpWord.equals("")){	//Makes sure we dont add empty words to the index file

				//Add this word to the database
				try {
					ms.insert(tmpWord, docNum , 1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Error inserting ");
					e.printStackTrace();
				}
			}
		}
		
		//Sorting the index file by an alfabetic order
		try {
			ms.sortByWord();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if (ms.statement!= null)
				try {
					ms.statement.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			e.printStackTrace();
		}
		
		//Removing duplicates from the index file
		try {
			ms.removeDuplicate();
			System.out.println("build index completed.");
		} catch (SQLException e) {
			if (ms.statement!= null)
				try {
					ms.statement.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			e.printStackTrace();
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
