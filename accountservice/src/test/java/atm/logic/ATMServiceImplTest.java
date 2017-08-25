package atm.logic;

import atm.model.Account;
import atm.model.Note;
import atm.model.NoteAmount;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ATMServiceImplTest {

	private AccountService accountService;
	private ATMServiceImpl atmService;

	@Before
	public void setUp() throws Exception {
		accountService = mock(AccountService.class);
		atmService = new ATMServiceImpl(accountService);
	}

	@Test
	public void checksBalanceOfAccount() throws Exception {
		when(accountService.checkBalance(1)).thenReturn("1.00");
		assertThat(atmService.checkBalance(1), is("1.00"));

		when(accountService.checkBalance(2)).thenReturn("2.00");
		assertThat(atmService.checkBalance(2), is("2.00"));
	}

	@Test(expected = Exception.class)
	public void givenAmountBelowMinimumThreshold_Then_ThrowException() throws Exception {
		atmService.withdraw(new BigDecimal("15.00"), 1);
	}

	@Test(expected = Exception.class)
	public void givenAmountGreaterThanMaxThreshold_Then_ThrowException() throws Exception {
		atmService.withdraw(new BigDecimal("255.00"), 1);
	}

	@Test(expected = Exception.class)
	public void givenValidAmountButWithNoNotes_Then_ThrowException() throws Exception {
		atmService.withdraw(new BigDecimal("20.00"), 1);
	}

	@Test
	public void givenValidAmountWithEnoughNotes_Then_ReturnNotes() throws Exception {
		List<Note> notes = Arrays.asList(new Note(NoteAmount.FIFTY));
		atmService.replenish(notes);
		List<Note> withdrawnNotes = atmService.withdraw(new BigDecimal("50.00"), 1);
		assertThat(withdrawnNotes, is(notes));
	}

}