package atm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	public static Logger logger = LoggerFactory.getLogger(Main.class);


	/**
	 * Assumptions:
	 * No need to use a proper framework with persistence (e.g. Spring boot)
	 *
	 * Used sl4j to easily be able switch between logging frameworks log4j
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("YO");
	}
}
