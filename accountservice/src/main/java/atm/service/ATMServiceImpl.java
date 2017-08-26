package atm.service;

import atm.LoggerUtils;
import atm.exceptions.InvalidWithdrawalAmountException;
import atm.exceptions.NotEnoughMoneyException;
import atm.exceptions.NotEnoughNotesException;
import atm.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

import static atm.model.NoteAmount.*;

public class ATMServiceImpl implements ATMService {

	public static Logger logger = LoggerFactory.getLogger(ATMServiceImpl.class);

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
		Collections.sort(this.notes);
		logger.info("Replenishing machine with notes: " + notes + ". New balance is " + calcMachineTotalAmount());
	}

	@Override
	public String checkBalance(int accountNumber) {
		String balance = accountService.checkBalance(accountNumber);
		logger.info(LoggerUtils.createBalanceLogMsg("Checking ATM Balance", accountNumber, balance));
		return balance;
	}

	@Override
	public List<Note> withdraw(BigDecimal amount, int accountNumber)
			throws InvalidWithdrawalAmountException, NotEnoughNotesException, NotEnoughMoneyException {
		logger.info(LoggerUtils.createWithdrawLogMsg("Starting ATM withdraw", accountNumber, amount));
		validateWithdrawalAmount(amount);
		List<Note> notesToReturn = calcNotesToReturn(amount);
		accountService.withdraw(amount, accountNumber);
		if (!notesToReturn.contains(Note.create(FIVE))) {
			giveFivePoundNotesIfPossible(notesToReturn);
		}
		notes.removeAll(notesToReturn);
		logger.info(LoggerUtils.createWithdrawLogMsg("Successful ATM withdraw", accountNumber, amount));
		return notesToReturn;
	}

	private List<Note> calcNotesToReturn(BigDecimal amount) throws NotEnoughNotesException {
		List<Note> notesToReturn = new ArrayList<>();
		BigDecimal amountStillNeeded = amount;
		for (Note note : this.notes) {
			if (note.getValue().compareTo(amountStillNeeded) <= 0) {
				notesToReturn.add(note);
				amountStillNeeded = amountStillNeeded.subtract(note.getValue());
			}
		}
		if (!areAmountsEqual(amountStillNeeded, BigDecimal.ZERO)) throw new NotEnoughNotesException("Not enough notes");
		return notesToReturn;
	}

	private void giveFivePoundNotesIfPossible(List<Note> notesToReturn) {
		if (notesToReturn.contains(Note.create(TEN)) && doesHaveAtLeastXFivers(2)) {
			replaceNotesWithXFivers(notesToReturn, Note.create(TEN), 2);
		} else if (notesToReturn.contains(Note.create(TWENTY)) && doesHaveAtLeastXFivers(4)) {
				replaceNotesWithXFivers(notesToReturn, Note.create(TWENTY), 4);
		} else if (notesToReturn.contains(Note.create(FIFTY)) && doesHaveAtLeastXFivers(10)) {
			replaceNotesWithXFivers(notesToReturn, Note.create(FIFTY), 10);
		}
	}

	private Boolean doesHaveAtLeastXFivers(int n) {
		return Collections.frequency(this.notes, Note.create(FIVE)) >= n;
	}

	private void replaceNotesWithXFivers(List<Note> notesToReturn, Note toReplace, int numOfFivers) {
		notesToReturn.remove(toReplace);
		notesToReturn.addAll(Note.create(FIVE, numOfFivers));
	}


	private void validateWithdrawalAmount(BigDecimal amount) throws InvalidWithdrawalAmountException, NotEnoughNotesException {
		if (amount.compareTo(MIN_WITHDRAW_THRESHOLD) < 0
				|| amount.compareTo(MAX_WITHDRAW_THRESHOLD) > 0
				|| amount.remainder(new BigDecimal("5")).compareTo(BigDecimal.ZERO) != 0) {
			throw new InvalidWithdrawalAmountException("Invalid amount of " + amount + " requested"); //not making this custom exception to save time
		}
		if (amount.compareTo(calcMachineTotalAmount()) > 0) {
			throw new NotEnoughNotesException("Not enough notes for amount requested of " + amount);
		}
	}

	private Boolean areAmountsEqual(BigDecimal x, BigDecimal y) {
		return x.compareTo(y) == 0;
	}

	private BigDecimal calcMachineTotalAmount() {
		return this.notes.stream().map(Note::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

}
