package atm.exceptions;

public class InvalidWithdrawalAmountException extends Exception {
	public InvalidWithdrawalAmountException(String message) {
		super(message);
	}
}
