package il.ac.shenkar.SearchEngine;

public class FileSchema {
 
	private String path;
	private int docNum;
	private int deleted;
	
	public FileSchema(String p, int d, int de) {
		path=p;
		docNum = d;
		deleted = de;
	}
	@Override
	public String toString() {
		return "FileSchema [path=" + path + ", docPath=" + docNum
				+ ", deleted=" + deleted + "]";
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getDocNum() {
		return docNum;
	}
	public void setDocNum(int docNum) {
		this.docNum = docNum;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
}
