/*
 * Project VSShare, SocketFormatter
 * Author: B. Berclaz x A. May
 * Date creation: 02.01.2020
 * Date last modification: 02.01.2020
 */

package Logging;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Class uses to have a specific format for a Logger
 * 
 * @author Brice Berclaz
 * @author Aurelien May
 */
public class SocketFormatter extends Formatter {

	/**
	 * Constructor
	 */
	public SocketFormatter() {
		super();
	}

	@Override
	public String format(LogRecord record) {

		StringBuffer sb = new StringBuffer();

		Date date = new Date(record.getMillis());
		sb.append(date.toString());
		sb.append(";");

		sb.append(record.getSourceClassName());
		sb.append(";");

		sb.append(record.getLevel().getName());
		sb.append(";");

		sb.append(record.getSourceMethodName());
		sb.append(";");

		sb.append(record.getMessage());
		sb.append(";");

		sb.append("\r\n");

		return sb.toString();
	}

}
