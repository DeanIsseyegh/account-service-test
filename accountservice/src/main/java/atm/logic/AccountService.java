package atm.logic;

import atm.exceptions.AccountDoesNotExistException;
import atm.exceptions.NotEnoughMoney;

import java.math.BigDecimal;

public interface AccountService {

	String checkBalance(int accountId) throws AccountDoesNotExistException;

	void withdraw(BigDecimal amount, int accountId) throws NotEnoughMoney;

}
