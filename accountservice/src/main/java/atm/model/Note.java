package atm.model;

import java.math.BigDecimal;

public class Note {

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


}
