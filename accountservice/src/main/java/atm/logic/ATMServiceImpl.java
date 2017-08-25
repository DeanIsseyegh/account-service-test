package atm.logic;

import atm.model.Note;

import java.math.BigDecimal;
import java.util.List;

public class ATMServiceImpl implements ATMService {

	private AccountService accountService;
	private List<Note> notes;

	public ATMServiceImpl(AccountService accountService, List<Note> notes) {
		this.accountService = accountService;
		this.notes = notes;
	}

	@Override
	public void replenish(BigDecimal amount) {

	}

	@Override
	public String checkBalance(int accountNumber) {
		return null;
	}

	@Override
	public List<Note> withdraw(BigDecimal amount, int accountNumber) {
		return null;
	}
}
