package model;

import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NoteVersion {
	
	private int idn;
	private String timestamp;
	private String title;
	private String content;
	private int color;
	
	
	public NoteVersion() {
		
	}
	
	public NoteVersion (int idn, String timestamp, String title, String content, int color) {
		this.idn = idn;
		this.timestamp = timestamp;
		this.title = title;
		this.content = content;
		this.color = color;
	}
	
	public int getIdn() {
		return idn;
	}
	public void setIdn(int idn) {
		this.idn = idn;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
	@Override
	public boolean equals(Object o) {
	    // self check
	    if (this == o)
	        return true;
	    // null check
	    if (o == null)
	        return false;
	    // type check and cast
	    if (getClass() != o.getClass())
	        return false;
	    Note note = (Note) o;
	    // field comparison
	    return idn == note.getIdn();
	}
	

}
