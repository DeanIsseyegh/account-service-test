package atm.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class NoteAmountTest {

	@Test
	public void getsValue() {
		assertThat(NoteAmount.FIVE.getValue(), is(new BigDecimal("5")));
		assertThat(NoteAmount.TEN.getValue(), is(new BigDecimal("10")));
		assertThat(NoteAmount.TWENTY.getValue(), is(new BigDecimal("20")));
		assertThat(NoteAmount.FIFTY.getValue(), is(new BigDecimal("50")));
	}

}