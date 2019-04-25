package model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UsersNotes {
	private int idu;
	private int idn;
	private int owner; //0=false,1=true
	private int archived;//0=false,1=true
	private int pinned;//0=false,1=true
	private int trash;//0=false,1=true
	
	
	public UsersNotes() {
		
	}
	
	/*
	 * Creates a UsersNotes with the parameters and set owner, archived and pinned 0
	 */
	public UsersNotes(int idu, int idn, int owner)  {
		this.idu = idu;
		this.idn = idn;
		this.owner = owner;
		this.archived = 0;
		this.pinned = 0;
		this.trash = 0;
	}
	
	
	public boolean validate(List<String> validationMessages) {
		if (owner!=0 && owner!=1) {
            validationMessages.add("Fill in the Owner field with a correct value (0=false,1=true).");
        } 

        if (archived!=0 && archived!=1) {
            validationMessages.add("Fill in the Archived field with a correct value (0=false,1=true).");
        } 

        if (pinned!=0 && pinned!=1) {
            validationMessages.add("Fill in the Pinned field with a correct value (0=false,1=true).");
        } 
        
        if (trash!=0 && trash!=1) {
            validationMessages.add("Fill in the Trash field with a correct value (0=false,1=true).");
        } 

        if (validationMessages.isEmpty())
            return true;
        else
            return false;
	}
	
	public int getIdu() {
		return idu;
	}
	public void setIdu(int idu) {
		this.idu = idu;
	}
	
	public int getIdn() {
		return idn;
	}
	public void setIdn(int idn) {
		this.idn = idn;
	}
	public int getOwner() {
		return owner;
	}
	public void setOwner(int propietary) {
		this.owner = propietary;
	}
	public int getArchived() {
		return archived;
	}
	public void setArchived(int stored) {
		this.archived = stored;
	}
	public int getPinned() {
		return pinned;
	}
	public void setPinned(int marked) {
		this.pinned = marked;
	}
	public int getTrash() {
		return trash;
	}
	public void setTrash(int inTrash) {
		this.trash = inTrash;
	}
	
	
	


}
