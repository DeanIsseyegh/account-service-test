package atm;

import atm.exceptions.AccountDoesNotExistException;
import atm.exceptions.InvalidWithdrawalAmountException;
import atm.exceptions.NotEnoughMoneyException;
import atm.exceptions.NotEnoughNotesException;
import atm.model.Account;
import atm.model.Note;
import atm.model.NoteAmount;
import atm.service.ATMService;
import atm.service.ATMServiceImpl;
import atm.service.AccountService;
import atm.service.AccountServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class IntegrationTest {

	private Set<Account> accounts;
	private AccountService accountService;
	private ATMService atmService;

	@Before
	public void setup() throws Exception {
		accounts = new HashSet<>();
		accounts.add(new Account(1001, new BigDecimal("2738.59")));
		accounts.add(new Account(1002, new BigDecimal("23.00")));
		accounts.add(new Account(1003, new BigDecimal("0.00")));
		accountService = new AccountServiceImpl(accounts);
		atmService = new ATMServiceImpl(accountService);
	}

	/**
	 * Would normally break this down into more integration tests, name them better etc. but for sake of this challenge
	 * and time constraints I'm going to do it as a one large one quickly.
	 */
	@Test
	public void IntegrationTest() throws Exception {
		checksBalance();
		givenMachineHasNoNotesThrowNotesException();
		givenAttemptToWithdrawAmountsTooSmallThrowException();
		givenAtmMachineHasAFiftyPoundNoteButWithdrawalOf20IsAttemptedThenThrowException();
		givenAtmMachineHasEnoughNotesButAccountDoesNotExistThenThrowException();
		givenValidWithdrawalMadeThenDepleteAtmMachinesNotes();
		givenWithdrawalOf50PoundsAndMachineHas50PoundNoteReturnNotes();
		givenMachineHasEnoughNotesAndButAccountDoesntHaveEnougHMoneyThenThrowException();
		withdrawingDepletesAccountsMoney();
	}

	private void checksBalance() {
		assertThat(atmService.checkBalance(1001), is("2738.59"));
		assertThat(atmService.checkBalance(1002), is("23.00"));
		assertThat(atmService.checkBalance(1003), is("0.00"));
	}

	private void givenMachineHasNoNotesThrowNotesException() throws Exception {
		try {
			atmService.withdraw(new BigDecimal("20"), 1001);
			assertTrue(false);
		} catch (NotEnoughNotesException e) {
			assertTrue(true);
		}
	}

	private void givenAttemptToWithdrawAmountsTooSmallThrowException() throws Exception {
		try {
			atmService.withdraw(new BigDecimal("10"), 1001);
			assertTrue(false);
		} catch (InvalidWithdrawalAmountException e) {
			assertTrue(true);
		}

		try {
			atmService.withdraw(new BigDecimal("19"), 1001);
			assertTrue(false);
		} catch (InvalidWithdrawalAmountException e) {
			assertTrue(true);
		}
	}

	private void givenAtmMachineHasAFiftyPoundNoteButWithdrawalOf20IsAttemptedThenThrowException() throws Exception {
		atmService.replenish(Note.create(NoteAmount.FIFTY, 1));

		try {
			atmService.withdraw(new BigDecimal("20"), 1001);
			assertTrue(false);
		} catch (NotEnoughNotesException e) {
			assertTrue(true);
		}
	}

	private void givenAtmMachineHasEnoughNotesButAccountDoesNotExistThenThrowException() throws Exception {
		atmService.replenish(Note.create(NoteAmount.TWENTY, 1));

		try {
			atmService.withdraw(new BigDecimal("20"), 0);
			assertTrue(false);
		} catch (AccountDoesNotExistException e) {
			assertTrue(true);
		}
	}

	private void givenValidWithdrawalMadeThenDepleteAtmMachinesNotes() throws Exception {
		assertThat(atmService.checkBalance(1001), is("2738.59"));
		assertThat(atmService.withdraw(new BigDecimal("20"), 1001), is(Note.create(NoteAmount.TWENTY, 1)));
		assertThat(atmService.checkBalance(1001), is("2718.59"));

		try {
			atmService.withdraw(new BigDecimal("20"), 1001);
			assertTrue(false);
		} catch (NotEnoughNotesException e) {
			assertTrue(true);
		}
	}

	private void givenWithdrawalOf50PoundsAndMachineHas50PoundNoteReturnNotes() throws Exception {
		assertThat(atmService.checkBalance(1001), is("2718.59"));
		assertThat(atmService.withdraw(new BigDecimal("50"), 1001), is(Note.create(NoteAmount.FIFTY, 1)));
		assertThat(atmService.checkBalance(1001), is("2668.59"));
	}

	private void givenMachineHasEnoughNotesAndButAccountDoesntHaveEnougHMoneyThenThrowException() throws Exception {
		atmService.replenish(Note.create(NoteAmount.TWENTY, 1));
		try {
			atmService.withdraw(new BigDecimal("20"), 1003);
			assertTrue(false);
		} catch (NotEnoughMoneyException e) {
			assertTrue(true);
		}
	}

	public void withdrawingDepletesAccountsMoney() throws Exception {
		atmService.replenish(Note.create(NoteAmount.TWENTY, 1));
		assertThat(atmService.checkBalance(1002), is("23.00"));
		assertThat(atmService.withdraw(new BigDecimal("20"), 1002), is(Note.create(NoteAmount.TWENTY, 1)));
		assertThat(atmService.checkBalance(1002), is("3.00"));
		try {
			atmService.withdraw(new BigDecimal("20"), 1002);
			assertTrue(false);
		} catch (NotEnoughNotesException e) {
			assertTrue(true);
		}
		atmService.replenish(Note.create(NoteAmount.TWENTY, 1));
		try {
			atmService.withdraw(new BigDecimal("20"), 1002);
			assertTrue(false);
		} catch (NotEnoughMoneyException e) {
			assertTrue(true);
		}
	}

}
