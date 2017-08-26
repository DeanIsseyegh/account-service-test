# account-service-test
Coding challenge to implement an account and atm service

# Notes
No `Main` method was provided, but running the `Integration Test` should be enough to prove the application works and to use it.

# Design Decisions
Implemented using TDD
sl4j used so open to extend to logging frameworks. Ones like log4j provide date timestamps which is why I didn't include them in the applications logging.

# Assumptions
Accounts do not have overdrafts. If they did, could extend by modifying the `Account.canWithdrawAmountOf` method
Min/Max withdrawal amounts don't need to be easily extendable (if they had to be, would have put those in a settings/config file)


Given more time...
1. Better integration tests (instead of one giant one, cover more withdrawal cases with multiple notes etc.)
2. More logging (only logging most of the basics and couple of errors, would have extended it to all exceptions)
3. `compareTo`, `equals` and `hashCode` were not directly unit tested - would have re-done those ones in TDD


