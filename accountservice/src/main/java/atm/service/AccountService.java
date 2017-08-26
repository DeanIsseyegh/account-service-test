package atm.service;

import atm.exceptions.AccountDoesNotExistException;
import atm.exceptions.NotEnoughMoneyException;

import java.math.BigDecimal;

public interface AccountService {

	String checkBalance(int accountId) throws AccountDoesNotExistException;

	void withdraw(BigDecimal amount, int accountId) throws NotEnoughMoneyException;

}
