package atm.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Note implements Comparable {

	private final NoteAmount noteAmount;

	public Note(NoteAmount noteAmount) {
		this.noteAmount = noteAmount;
	}

	public NoteAmount getDenominator() {
		return noteAmount;
	}

	public BigDecimal getValue() {
		return noteAmount.getValue();
	}

	public static Note create(NoteAmount noteAmount) {
		return new Note(noteAmount);
	}

	public static List<Note> create(NoteAmount noteAmount, int n) {
		List<Note> notes = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			notes.add(Note.create(noteAmount));
		}
		return notes;
	}

	@Override
	public String toString() {
		return noteAmount.name();
	}

	@Override
	public int compareTo(Object o) {
		BigDecimal oAmount = ((Note) o).getValue();
		return oAmount.compareTo(getValue());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Note)) return false;

		Note note = (Note) o;

		return noteAmount == note.noteAmount;
	}

	@Override
	public int hashCode() {
		return noteAmount != null ? noteAmount.hashCode() : 0;
	}
}
