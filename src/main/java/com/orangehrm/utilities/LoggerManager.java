package com.orangehrm.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager {
	// this method returns Logger instance of provided class
	public static Logger getLogger(Class<?> clazz) {
		return LogManager.getLogger(clazz);

	}
}
