package atm.logic;

import atm.model.Note;

import java.math.BigDecimal;
import java.util.List;

public interface ATMService {

	void replenish(List<Note> notes);

	String checkBalance(int accountNumber);

	List<Note> withdraw(BigDecimal amount, int accountNumber) throws Exception;

}
