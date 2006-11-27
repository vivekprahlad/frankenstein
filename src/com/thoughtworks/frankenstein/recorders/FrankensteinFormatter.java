package com.thoughtworks.frankenstein.recorders;

import javax.swing.text.DateFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Understands SOMETHING
 *
 * @author vivek
 */
public class FrankensteinFormatter extends Formatter {
    public String format(LogRecord record) {
        StringBuffer sb = new StringBuffer();
        Date date = new Date(record.getMillis());
        DateFormatter formatter = new DateFormatter();
        sb.append(record.getLevel() + ": ");
        sb.append(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(date) + " ");
        if (record.getThrown()!=null) {
            sb.append("Exception: " + record.getThrown().getMessage() + "\n");
            logException(sb, record.getThrown());
        }
        sb.append(formatMessage(record) + "\n");
        return sb.toString();
    }

    private void logException(StringBuffer sb, Throwable thrown) {
        StackTraceElement[] elements = thrown.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            sb.append("\t\t\t\t" + elements[i].toString() + "\n");
        }
    }
}
