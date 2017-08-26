package atm.model;

import atm.exceptions.InvalidBalanceException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class AccountTest {

	@Test
	public void getAccountNumber() throws Exception {
		Account account = new Account(1, new BigDecimal("1"));
		assertThat(account.getAccountNumber(), is(1));
	}

	@Test
	public void getBalance() throws Exception {
		BigDecimal amount = new BigDecimal("1");
		Account account = new Account(1, amount);
		assertThat(account.getBalance(), is(amount));
	}

	@Test(expected = InvalidBalanceException.class)
	public void givenNegativeAmountThenThrowException() throws Exception {
		BigDecimal amount = new BigDecimal("-1");
		new Account(1, amount);
	}

	@Test
	public void givenEnoughMoney_Then_ReturnTrue() throws Exception {
		BigDecimal amount = BigDecimal.ONE;
		Account account = new Account(1, BigDecimal.TEN);
		assertThat(account.canWithdrawAmountOf(amount), is(true));
	}
	@Test
	public void givenNotEnoughMoney_Then_ReturnFalse() throws Exception {
		BigDecimal amount = BigDecimal.TEN;
		Account account = new Account(1, BigDecimal.ONE);
		assertThat(account.canWithdrawAmountOf(amount), is(false));
	}

	@Test
	public void decreasesBalance() throws Exception {
		Account account = new Account(1, BigDecimal.ONE);
		account.decreaseBalance(BigDecimal.ONE);
		assertThat(account.getBalance(), is(BigDecimal.ZERO));
	}

}
