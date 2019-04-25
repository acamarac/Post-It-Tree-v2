package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Friend {

	private long idu1;
	private long idu2;
	
	
	public long getIdu1() {
		return idu1;
	}
	public void setIdu1(int idu1) {
		this.idu1 = idu1;
	}
	public long getIdu2() {
		return idu2;
	}
	public void setIdu2(int idu2) {
		this.idu2 = idu2;
	}
	
}
