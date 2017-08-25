package atm.model;

import java.math.BigDecimal;

public class Note {

	private final NoteAmount amount;

	public Note(NoteAmount noteAmount) {
		this.amount = noteAmount;
	}

	public NoteAmount getAmount() {
		return amount;
	}
}

enum NoteAmount {
	FIVE(new BigDecimal("5")),
	TEN(new BigDecimal("10")),
	TWENTY(new BigDecimal("20")),
	FIFTY(new BigDecimal("50"));

	private BigDecimal amount;

	NoteAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}
}


