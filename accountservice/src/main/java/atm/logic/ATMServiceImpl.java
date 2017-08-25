package atm.logic;

import atm.model.Note;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ATMServiceImpl implements ATMService {

	//Use composition for account service vs inheritance here as its an has-a relation as opposed to an is-a
	private AccountService accountService;
	private List<Note> notes = new ArrayList<>();

	private final static BigDecimal MIN_WITHDRAW_THRESHOLD = new BigDecimal("20"); //Make this configurable?
	private final static BigDecimal MAX_WITHDRAW_THRESHOLD = new BigDecimal("250");

	public ATMServiceImpl(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public void replenish(List<Note> notes) {
	this.notes.addAll(notes);
	}

	@Override
	public String checkBalance(int accountNumber) {
		return accountService.checkBalance(accountNumber);
	}

	@Override
	public List<Note> withdraw(BigDecimal amount, int accountNumber) throws Exception {
		if (amount.compareTo(MIN_WITHDRAW_THRESHOLD) < 0 || amount.compareTo(MAX_WITHDRAW_THRESHOLD) > 0) {
			throw new Exception("Invalid amount of " + amount + " requested for account number " + accountNumber); //not making this custom exception to save time
		}
		BigDecimal atmMachineAmount = notes.stream().map(Note::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (amount.compareTo(atmMachineAmount) > 0) {
			throw new Exception("Not enough notes for amount requested of " + amount);
		}
		return notes;
	}
}
