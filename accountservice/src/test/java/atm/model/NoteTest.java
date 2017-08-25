package atm.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class NoteTest {


	@Test
	public void getDenominator() throws Exception {
		Note fiver = new Note(NoteAmount.FIVE);
		Note tenner = new Note(NoteAmount.TEN);
		Note twenty = new Note(NoteAmount.TWENTY);
		Note fifty = new Note(NoteAmount.FIFTY);
		assertThat(fiver.getDenominator(), is(NoteAmount.FIVE));
		assertThat(tenner.getDenominator(), is(NoteAmount.TEN));
		assertThat(twenty.getDenominator(), is(NoteAmount.TWENTY));
		assertThat(fifty.getDenominator(), is(NoteAmount.FIFTY));
	}

	@Test
	public void getValue() throws Exception {
		Note fiver = new Note(NoteAmount.FIVE);
		Note tenner = new Note(NoteAmount.TEN);
		Note twenty = new Note(NoteAmount.TWENTY);
		Note fifty = new Note(NoteAmount.FIFTY);
		assertThat(fiver.getValue(), is(new BigDecimal("5")));
		assertThat(tenner.getValue(), is(new BigDecimal("10")));
		assertThat(twenty.getValue(), is(new BigDecimal("20")));
		assertThat(fifty.getValue(), is(new BigDecimal("50")));
	}

}