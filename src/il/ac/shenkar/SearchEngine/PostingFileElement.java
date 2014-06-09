package il.ac.shenkar.SearchEngine;
//This class represents a line in the postings file table. 
//every line is path of File and document number
public class PostingFileElement {
	private String m_path;
	private int m_docNum;
	
	public PostingFileElement(String p, int d)
	{
		this.m_path = p;
		this.m_docNum = d;
	}
	// Getters and setters
	public String getM_path() {
		return m_path;
	}

	public void setM_path(String m_path) {
		this.m_path = m_path;
	}

	public int getM_docNum() {
		return m_docNum;
	}

	public void setM_docNum(int m_docNum) {
		this.m_docNum = m_docNum;
	}
	
}
