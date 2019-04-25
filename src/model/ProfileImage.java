package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProfileImage {
	private int idi;
	private String urlimage;
	
	
	public int getIdi() {
		return idi;
	}
	public void setIdi(int idi) {
		this.idi = idi;
	}
	public String getUrlimage() {
		return urlimage;
	}
	public void setUrlimage(String newUrlimage) {
		this.urlimage = newUrlimage;
	}
}
