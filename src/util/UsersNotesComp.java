package util;

import java.util.Comparator;

import model.UsersNotes;

public class UsersNotesComp implements Comparator<UsersNotes>{

	@Override
	public int compare(UsersNotes u1, UsersNotes u2) {

		if(u1.getIdn() < u2.getIdn()){
			return 1;
		} else {
			return -1;
		}
	}

}
