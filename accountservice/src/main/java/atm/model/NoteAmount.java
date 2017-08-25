package atm.model;

import java.math.BigDecimal;

public enum NoteAmount {
	FIVE(new BigDecimal("5")),
	TEN(new BigDecimal("10")),
	TWENTY(new BigDecimal("20")),
	FIFTY(new BigDecimal("50"));

	private BigDecimal amount;

	NoteAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getValue() {
		return amount;
	}
}