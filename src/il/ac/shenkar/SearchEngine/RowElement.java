package il.ac.shenkar.SearchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class RowElement {
	private String word;
	private int docNumber;
	private int freq;

	public RowElement(String word, int docNumber, int freq) {
		this.word = word;
		this.docNumber = docNumber;
		this.freq = freq;
	}

	@Override
	public String toString() {
		return "RowElement [word=" + word + ", docNumber=" + docNumber
				+ ", freq=" + freq + "]";
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(int docNumber) {
		this.docNumber = docNumber;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public StringBuilder getPreview(int docNumber) {
		System.out.println("getPreview");
		StringBuilder preview= new StringBuilder();
		List<PostingFileElement> pf = FolderScanner.getInstance().postingFile;
		for (PostingFileElement element : pf) {
			if (element.getM_docNum() == docNumber) {

				BufferedReader br = null;
				 
				try {
		 
					String sCurrentLine;
		 
					br = new BufferedReader(new FileReader(element.getM_path()));
					int counter=0;
					while ((sCurrentLine = br.readLine()) != null && counter<2) {	
						counter++;
						preview.append(sCurrentLine);
					}
		 
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (br != null)br.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		}

	return preview;
	}
}
