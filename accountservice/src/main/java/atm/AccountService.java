package atm;

import java.math.BigDecimal;

public interface AccountService {

	String checkBalance();

	void withdraw(BigDecimal amount);

}
