package il.ac.shenkar.SearchEngine;

public class FileDescriptor {
	private String title;
	private String creationDate;
	private String author;
	private String preview;
	private StringBuilder content;
	
	public FileDescriptor() {
		content = new StringBuilder();
	}

	@Override
	public String toString() {
		return "FileDescriptor [title=" + title + ", author=" + author
				+ ", creationDate=" + creationDate + "]";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}



	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public StringBuilder getContent() {
		return content;
	}

	public void setContent(StringBuilder content) {
		this.content = content;
	}
	public void appendContent (String content){
		this.content.append(content);
	}
	
	
	
}
