package atm.logic;

import atm.model.Note;

import java.math.BigDecimal;
import java.util.List;

public interface ATMService {

	void replenish(BigDecimal amount);

	String checkBalance(int accountNumber);

	List<Note> withdraw(BigDecimal amount, int accountNumber);

}
