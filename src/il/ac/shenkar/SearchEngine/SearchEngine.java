package il.ac.shenkar.SearchEngine;

public class SearchEngine {
	
	static FolderScanner fs;
	public static void main(String[] args) {
		//System.err.println(System.getProperties());
	    System.out.println("folderScanner is starting...");
	    fs = FolderScanner.getInstance();
		fs.run();
	}

}
