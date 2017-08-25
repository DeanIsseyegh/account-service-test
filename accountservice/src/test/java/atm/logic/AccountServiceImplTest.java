package atm.logic;

import atm.exceptions.InvalidAccountsException;
import atm.exceptions.NotEnoughMoneyException;
import atm.model.Account;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class AccountServiceImplTest {

	@Test(expected = InvalidAccountsException.class)
	public void givenNoAccounts_Then_ThrowException() throws Exception {
		new AccountServiceImpl(new HashSet<>());
	}

	@Test
	public void givenAnAccount_When_CheckingBalance_Then_ReturnAccountsBalance() throws Exception {
		Set<Account> accounts = Stream.of(new Account(1, BigDecimal.ONE))
				.collect(Collectors.toSet());
		AccountServiceImpl accountService = new AccountServiceImpl(accounts);
		assertThat(accountService.checkBalance(1), is("1.00"));
	}

	@Test
	public void givenMultipleAccounts_When_CheckingBalance_Then_ReturnAccountsBalance() throws Exception {
		Set<Account> accounts = Stream.of(new Account(1, BigDecimal.ONE),
				new Account(2, BigDecimal.TEN))
				.collect(Collectors.toSet());
		AccountServiceImpl accountService = new AccountServiceImpl(accounts);
		assertThat(accountService.checkBalance(1), is("1.00"));
		assertThat(accountService.checkBalance(2), is("10.00"));
	}


	@Test
	public void When_WithdrawingMoney_Then_BalanceShouldDecrease() throws Exception {
		Set<Account> accounts = Stream.of(new Account(1, BigDecimal.TEN))
				.collect(Collectors.toSet());
		AccountServiceImpl accountService = new AccountServiceImpl(accounts);

		accountService.withdraw(BigDecimal.ONE,1);
		assertThat(accountService.checkBalance(1), is("9.00"));

		accountService.withdraw(BigDecimal.ONE,1);
		assertThat(accountService.checkBalance(1), is("8.00"));
	}

	@Test(expected = NotEnoughMoneyException.class)
	public void givenAccountWithNotEnoughMoney_When_WithdrawingMoney_Then_ExceptionIsThrown() throws Exception {
		Set<Account> accounts = Stream.of(new Account(1, BigDecimal.ONE))
				.collect(Collectors.toSet());
		AccountServiceImpl accountService = new AccountServiceImpl(accounts);
		accountService.withdraw(BigDecimal.TEN,1);
	}

}