package atm.logic;

import atm.exceptions.AccountDoesNotExistException;
import atm.exceptions.InvalidAccountsException;
import atm.exceptions.NotEnoughMoney;
import atm.model.Account;

import java.math.BigDecimal;
import java.util.Set;

public class AccountServiceImpl implements AccountService {

	private Set<Account> accounts;

	public AccountServiceImpl(Set<Account> accounts) throws InvalidAccountsException {
		this.accounts = accounts;
		validateAccounts();
	}

	@Override
	public String checkBalance(int accountId) {
		Account account = retrieveAccount(accountId);
		return formatAmount(account.getBalance());
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
	public void withdraw(BigDecimal amount, int accountId) throws NotEnoughMoney {
		Account account = retrieveAccount(accountId);
		if (!account.hasAmountAvailable(amount)) {
			throw new NotEnoughMoney("User with balance of " + amount +
					" does not have enough money to withdraw an amount of" + account.getBalance());
		}
		account.decreaseBalance(amount);
	}

	private Account retrieveAccount(int accountId) {
		return accounts.stream().filter(it -> it.getAccountNumber() == accountId)
				.findFirst()
				.orElseThrow(() ->
						new AccountDoesNotExistException(String.format("Account with id %d does not exist", accountId))
				);
	}
}
