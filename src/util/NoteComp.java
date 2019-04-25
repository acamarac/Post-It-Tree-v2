package util;

import java.util.Comparator;

import model.Note;

public class NoteComp implements Comparator<Note> {

	@Override
	public int compare(Note n1, Note n2) {
		if(n1.getIdn() < n2.getIdn()){
            return 1;
        } else {
            return -1;
        }
	}

}
