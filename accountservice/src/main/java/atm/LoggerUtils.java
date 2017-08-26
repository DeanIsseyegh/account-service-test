package atm;

import java.math.BigDecimal;

public class LoggerUtils {

	public static String createBalanceLogMsg(String method, int accountId, String balance) {
		return "Action: " + method + " | Account id : " + accountId + " | Balance: " + balance;
	}

	public static String createWithdrawLogMsg(String method, int accountId, BigDecimal withdrawalAmount) {
		return "Action: " + method + " | Account id : " + accountId + " | Withdraw amount : " + withdrawalAmount;
	}

	public static String createWithdrawLogMsg(String method, int accountId, BigDecimal withdrawalAmount, BigDecimal balance) {
		return "Action: " + method + " | Account id : " + accountId + " | Withdraw amount : " + withdrawalAmount +
				" | Balance : " + balance;
	}

}
