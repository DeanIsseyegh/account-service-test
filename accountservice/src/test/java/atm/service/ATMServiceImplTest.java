package atm.service;

import atm.exceptions.InvalidWithdrawalAmountException;
import atm.exceptions.NotEnoughNotesException;
import atm.model.Note;
import atm.model.NoteAmount;
import atm.service.ATMServiceImpl;
import atm.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Not;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static atm.model.NoteAmount.*;
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

	@Test(expected = InvalidWithdrawalAmountException.class)
	public void Given_AmountBelowMinimumThreshold_Then_ThrowException() throws Exception {
		atmService.withdraw(new BigDecimal("15.00"), 1);
	}

	@Test(expected = InvalidWithdrawalAmountException.class)
	public void Given_AmountGreaterThanMaxThreshold_Then_ThrowException() throws Exception {
		atmService.withdraw(new BigDecimal("255.00"), 1);
	}

	@Test(expected = NotEnoughNotesException.class)
	public void Given_ValidAmountButWithNoNotes_Then_ThrowException() throws Exception {
		atmService.withdraw(new BigDecimal("20.00"), 1);
	}

	@Test
	public void Given_WithdrawalOf50Pounds_And_ATMContainsA50PoundNote_Then_ReturnNote() throws Exception {
		List<Note> notes = Arrays.asList(Note.create(FIFTY));
		atmService.replenish(notes);
		List<Note> withdrawnNotes = atmService.withdraw(new BigDecimal("50.00"), 1);
		assertThat(withdrawnNotes, is(notes));
	}

	@Test
	public void Given_WithdrawalOf50Pounds_Then_AmountIsWithdrawnFromAccount() throws Exception {
		List<Note> notes = Arrays.asList(Note.create(FIFTY));
		atmService.replenish(notes);
		BigDecimal amount = new BigDecimal("50.00");
		atmService.withdraw(amount, 1);
		verify(accountService, times(1)).withdraw(amount, 1);
	}

	@Test
	public void Given_WithdrawalOf20Pounds_And_ATMContainsA20PoundNote_Then_ReturnNote() throws Exception {
		List<Note> notes = Arrays.asList(Note.create(TWENTY));
		atmService.replenish(notes);
		BigDecimal amount = new BigDecimal("20.00");
		List<Note> withdrawnNotes = atmService.withdraw(amount, 1);
		verify(accountService, times(1)).withdraw(amount, 1);
		assertThat(withdrawnNotes, is(notes));
	}

	@Test
	public void Given_WithdrawalOf20Pounds_And_ATMContainsFour5PoundNotes_Then_ReturnNotes() throws Exception {
		List<Note> notes = Arrays.asList(Note.create(FIVE), Note.create(FIVE), Note.create(FIVE), Note.create(FIVE));
		atmService.replenish(notes);
		BigDecimal amount = new BigDecimal("20.00");
		List<Note> withdrawnNotes = atmService.withdraw(amount, 1);
		verify(accountService, times(1)).withdraw(amount, 1);
		assertThat(withdrawnNotes, is(notes));
	}

	@Test(expected = InvalidWithdrawalAmountException.class)
	public void Given_AmountNotDivisibleBy5_Then_ThrowExceptionAndDontWithdrawMoneyFromAccount() throws Exception {
		List<Note> notes = Arrays.asList(Note.create(FIFTY));
		atmService.replenish(notes);
		atmService.withdraw(new BigDecimal("9.00"), 1);
		verify(accountService, times(0)).withdraw(any(), any());
	}

	@Test
	public void Given_WithdrawalOf20Pounds_And_ATMContains1TwentyPoundNoteAnd1FivePoundNote_Then_ReturnNotes() throws Exception {
		List<Note> notes = Arrays.asList(Note.create(TWENTY), Note.create(FIVE));
		atmService.replenish(notes);
		List<Note> withdrawnNotes = atmService.withdraw(new BigDecimal("20.00"), 1);
		assertThat(withdrawnNotes, is(Arrays.asList(Note.create(TWENTY))));
	}

	@Test
	public void Given_WithdrawalOf50Pounds_And_ATMContainsMultipleDifferentNotes_Then_ReturnNotes() throws Exception {
		List<Note> notes = Arrays.asList(Note.create(TWENTY), Note.create(FIVE), Note.create(FIVE),
				Note.create(TEN), Note.create(TEN), Note.create(TEN), Note.create(TWENTY));
		atmService.replenish(notes);
		List<Note> withdrawnNotes = atmService.withdraw(new BigDecimal("50.00"), 1);
		assertThat(withdrawnNotes, is(Arrays.asList(Note.create(TWENTY), Note.create(TWENTY), Note.create(FIVE), Note.create(FIVE))));
	}

	@Test
	public void Given_WithdrawalOf20Pounds_Then_ATMMachineLoses20Pounds() throws Exception {
		List<Note> notes = Arrays.asList(Note.create(TWENTY), Note.create(FIFTY));
		atmService.replenish(notes);
		assertThat(atmService.withdraw(new BigDecimal("20.00"), 1), is(Arrays.asList(Note.create(TWENTY))));
		assertThat(atmService.withdraw(new BigDecimal("50.00"), 1), is(Arrays.asList(Note.create(FIFTY))));
		try {
			atmService.withdraw(new BigDecimal("20.00"), 1);
			assertTrue(false);
		} catch (NotEnoughNotesException e) {
			assertTrue(true);
		}
	}

	@Test
	public void Given_Valid20PoundWithdrawal_Then_ReturnNotesWithAtTwoFivers() throws Exception {
		List<Note> notes = Arrays.asList(Note.create(TEN), Note.create(FIVE), Note.create(TEN), Note.create(TEN),
				Note.create(FIVE), Note.create(FIVE));
		atmService.replenish(notes);
		List<Note> withdrawnNotes = atmService.withdraw(new BigDecimal("20.00"), 1);
		assertThat(withdrawnNotes, is(Arrays.asList(Note.create(TEN), Note.create(FIVE), Note.create(FIVE))));
	}

	@Test
	public void Given_Valid20PoundWithdrawal_Then_ReturnNotesWithFourFivers() throws Exception {
		List<Note> notes = Arrays.asList(Note.create(TWENTY), Note.create(FIVE), Note.create(FIVE),
				Note.create(FIVE), Note.create(FIVE));
		atmService.replenish(notes);
		List<Note> withdrawnNotes = atmService.withdraw(new BigDecimal("20.00"), 1);
		assertThat(withdrawnNotes, is(Note.create(FIVE, 4)));
	}

	@Test
	public void Given_Valid50PoundWithdrawal_Then_ReturnNotesWith10Fivers() throws Exception {
		List<Note> notes = new ArrayList<>(Arrays.asList(Note.create(FIFTY), Note.create(FIFTY), Note.create(TWENTY)));
		notes.addAll(Note.create(FIVE, 10));
		atmService.replenish(notes);
		List<Note> withdrawnNotes = atmService.withdraw(new BigDecimal("50.00"), 1);
		assertThat(withdrawnNotes, is(Note.create(FIVE, 10)));
	}

	@Test(expected = NotEnoughNotesException.class)
	public void Given_25PoundWithdrawal_AndMachineOnlyHasFiftyPoundNotes_ThenThrowException() throws Exception {
		List<Note> notes = Arrays.asList(Note.create(FIFTY), Note.create(FIFTY));
		atmService.replenish(notes);
		atmService.withdraw(new BigDecimal("25.00"), 1);
	}

}