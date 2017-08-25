package atm.model;

import atm.exceptions.InvalidBalanceException;

import java.math.BigDecimal;

public class Account {

	private int accountNumber;
	private BigDecimal balance;

	public Account(int accountNumber, BigDecimal balance) throws Exception {
		this.accountNumber = accountNumber;
		this.balance = balance;
		validateBalance();
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * Logic here assumes there is no overdraft
	 * @param amount
	 * @return
	 */
	public Boolean hasAmountAvailable(BigDecimal amount) {
		return amount.compareTo(getBalance()) < 0;
	}

	public void decreaseBalance(BigDecimal amount) {
		balance = balance.subtract(amount);
	}

	private void validateBalance() throws Exception {
		if (balance.compareTo(BigDecimal.ZERO) < 0) throw new InvalidBalanceException("Cannot have balance with negative amount");
	}
}
