package atm.service;

import atm.LoggerUtils;
import atm.exceptions.AccountDoesNotExistException;
import atm.exceptions.InvalidAccountsException;
import atm.exceptions.NotEnoughMoneyException;
import atm.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Set;

public class AccountServiceImpl implements AccountService {

	public static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private Set<Account> accounts;

	public AccountServiceImpl(Set<Account> accounts) throws InvalidAccountsException {
		this.accounts = accounts;
		validateAccounts();
	}

	@Override
	public String checkBalance(int accountId) {
		Account account = retrieveAccount(accountId);
		String balance = formatAmount(account.getBalance());
		logger.info(LoggerUtils.createBalanceLogMsg("Check Balance", accountId, balance));
		return balance;
	}

	private void validateAccounts() throws InvalidAccountsException {
		if (accounts == null || accounts.size() == 0) {
			throw new InvalidAccountsException("Must load accounts into account service");
		}
	}

	private String formatAmount(BigDecimal amount) {
		return amount.setScale(2).toString();
	}

	@Override
	public void withdraw(BigDecimal amount, int accountId) throws NotEnoughMoneyException {
		Account account = retrieveAccount(accountId);
		logger.info(LoggerUtils.createWithdrawLogMsg("Starting withdraw", accountId, amount, account.getBalance()));
		if (!account.canWithdrawAmountOf(amount)) {
			NotEnoughMoneyException e = new NotEnoughMoneyException("User with balance of " + amount +
					" does not have enough money to withdraw an amount of" + account.getBalance());
			logger.error(e.getMessage(), e);
			throw e;
		}
		account.decreaseBalance(amount);
		logger.info(LoggerUtils.createWithdrawLogMsg("Successful withdraw", accountId, amount, account.getBalance()));
	}

	private Account retrieveAccount(int accountId) {
		return accounts.stream().filter(it -> it.getAccountNumber() == accountId)
				.findFirst()
				.orElseThrow(() ->
						new AccountDoesNotExistException(String.format("Account with id %d does not exist", accountId))
				);
	}
}
