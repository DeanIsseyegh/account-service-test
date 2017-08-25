package atm.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class NoteTest {

	@Test
	public void getAmount() throws Exception {
		Note fiver = new Note(NoteAmount.FIVE);
		Note tenner = new Note(NoteAmount.TEN);
		Note twenty = new Note(NoteAmount.TWENTY);
		Note fifty = new Note(NoteAmount.FIFTY);
		assertThat(fiver.getAmount(), is(NoteAmount.FIVE));
		assertThat(tenner.getAmount(), is(NoteAmount.TEN));
		assertThat(twenty.getAmount(), is(NoteAmount.TWENTY));
		assertThat(fifty.getAmount(), is(NoteAmount.FIFTY));
	}

}