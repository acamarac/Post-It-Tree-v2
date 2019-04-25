package model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Note {
	private int idn;
	private String title;
	private String content;
	private int color;
	
	
	
	public boolean validate(List<String> validationMessages) {
	if (title == null || title.trim().isEmpty() || title.length() < 4) {
		validationMessages.add("The title must be higher than 3 characters.");
	} else if (title.length() > 50) {
		validationMessages.add("The title cannot be higher than 50 characters.");
	}

	
	if (content == null || content.trim().isEmpty() || content.length() < 11) {
		validationMessages.add("The content must be higher than 10 characters.");
	} else if (content.length() > 1000) {
		validationMessages.add("The content cannot be higher than 1000 characters.");
	}

	if (validationMessages.isEmpty())
		return true;
	else
		return false;
	}
	
	public int getIdn() {
		return idn;
	}
	public void setIdn(int idn) {
		this.idn = idn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String newTitle) {
		this.title = newTitle;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String newContent) {
		this.content = newContent;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int newColor) {
		this.color = newColor;
	}
	
	@Override
    public boolean equals(Object o) {
 
        // If the object is compared with itself then return true  
        if (o == this) {
            return true;
        }
 
        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Note)) {
            return false;
        }
         
        // typecast o to Complex so that we can compare data members 
        Note c = (Note) o;
         
        // Compare the data members and return accordingly 
        return Integer.compare(idn, c.idn) == 0;
    }


}
