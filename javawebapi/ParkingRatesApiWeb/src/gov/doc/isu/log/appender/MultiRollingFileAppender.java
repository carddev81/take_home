/**
 * 
 */
package gov.doc.isu.log.appender;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;

import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * MultiRollingFileAppender extends {@link FileAppender} so that the
 * underlying file is rolled over at a user chosen frequency. This 
 * appender will roll over by a schedule specified by the <b>DatePattern</b> and
 * also will roll over by size specified by the <b>MaxFileSize</b>.
 * 
  * <p>The rolling size schedule is specified by the <b>MaxFileSize</b>
 * option. When the log file reaches the specified size, it is rolled over: it is renamed
 * by appending "'.'yyyy-MM-dd_1" (user specified DatePattern) to the file name.</p>
 * 
 * <p>This appender has two additional options, namely <b>MaxFileSize</b> option and 
 * maxBackupIndex to the list of options supported by FileAppender. The <b>MaxFileSize</b>
 * option takes a String value representing a long integer in the range 0 - 263. You can
 * specify the value with the suffixes "KB", "MB" or "GB" so that the integer is interpreted
 * as being expressed respectively in kilobytes, megabytes or gigabytes. For example, the
 * value "10KB" will be interpreted as 10240.</p>
 * 
 * <p>Rollover occurs either daily or when the log file reaches MaxFileSize. Note that since
 * the last log event is written entirely before a roll over is triggered, actual files are
 * usually a tad larger than the value of MaxFileSize.</p>
 * 
 * <p>The default value of this option is 10MB.</p>
 *
 * <p>The rolling schedule is specified by the <b>DatePattern</b>
 * option. This pattern should follow the {@link SimpleDateFormat}
 * conventions. In particular, you <em>must</em> escape literal text
 * within a pair of single quotes. A formatted version of the date
 * pattern is used as the suffix for the rolled file name.
 * 
 * <p>For example, if the <b>File</b> option is set to
 * <code>/foo/bar.log</code> and the <b>DatePattern</b> set to
 * <code>'.'yyyy-MM-dd</code>, on 2001-02-16 at midnight, the logging
 * file <code>/foo/bar.log</code> will be copied to
 * <code>/foo/bar.2001-02-16_1.log</code> and logging for 2001-02-17
 * will continue in <code>/foo/bar.log</code> until it rolls over
 * the next day.
 * 
 * <p>It is possible to specify monthly, weekly, half-daily, daily,
 * hourly, or minutely rollover schedules.
 * 
 * <p><table border="1" cellpadding="2">
 * <tr>
 * <th>DatePattern</th>
 * <th>Rollover schedule</th>
 * <th>Example</th>
 * 
 * <tr>
 * <td><code>'.'yyyy-MM</code>
 * <td>Rollover at the beginning of each month</td>
 * 
 * <td>At midnight of May 31st, 2002 <code>/foo/bar.log</code> will be
 * copied to <code>/foo/bar.2002-05_1.log</code>. Logging for the month
 * of June will be output to <code>/foo/bar.log</code> until it is
 * also rolled over the next month.
 * 
 * <tr>
 * <td><code>'.'yyyy-ww</code>
 * 
 * <td>Rollover at the first day of each week. The first day of the
 * week depends on the locale.</td>
 * 
 * <td>Assuming the first day of the week is Sunday, on Saturday
 * midnight, June 9th 2002, the file <i>/foo/bar.log</i> will be
 * copied to <i>/foo/bar.2002-23_1.log</i>.  Logging for the 24th week
 * of 2002 will be output to <code>/foo/bar.log</code> until it is
 * rolled over the next week.
 * 
 * <tr>
 * <td><code>'.'yyyy-MM-dd</code>
 * 
 * <td>Rollover at midnight each day.</td>
 * 
 * <td>At midnight, on March 8th, 2002, <code>/foo/bar.log</code> will
 * be copied to <code>/foo/bar.2002-03-08_1.log</code>. Logging for the
 * 9th day of March will be output to <code>/foo/bar.log</code> until
 * it is rolled over the next day.
 * 
 * <tr>
 * <td><code>'.'yyyy-MM-dd-a</code>
 * 
 * <td>Rollover at midnight and midday of each day.</td>
 * 
 * <td>At noon, on March 9th, 2002, <code>/foo/bar.log</code> will be
 * copied to <code>/foo/bar.2002-03-09-AM_1.log</code>. Logging for the
 * afternoon of the 9th will be output to <code>/foo/bar.log</code>
 * until it is rolled over at midnight.
 * 
 * <tr>
 * <td><code>'.'yyyy-MM-dd-HH</code>
 * 
 * <td>Rollover at the top of every hour.</td>
 * 
 * <td>At approximately 11:00.000 o'clock on March 9th, 2002,
 * <code>/foo/bar.log</code> will be copied to
 * <code>/foo/bar.2002-03-09-10_1.log</code>. Logging for the 11th hour
 * of the 9th of March will be output to <code>/foo/bar.log</code>
 * until it is rolled over at the beginning of the next hour.
 * 
 * 
 * <tr>
 * <td><code>'.'yyyy-MM-dd-HH-mm</code>
 * 
 * <td>Rollover at the beginning of every minute.</td>
 * 
 * <td>At approximately 11:23,000, on March 9th, 2001,
 * <code>/foo/bar.log</code> will be copied to
 * <code>/foo/bar.2001-03-09-10-22_1.log</code>. Logging for the minute
 * of 11:23 (9th of March) will be output to
 * <code>/foo/bar.log</code> until it is rolled over the next minute.
 * 
 * </table>
 * 
 * <p>Do not use the colon ":" character in anywhere in the
 * <b>DatePattern</b> option. The text before the colon is interpreted
 * as the protocol specification of a URL which is probably not what
 * you want.
 * 
 * @author unascribed (File Appender)
 * @author Richard Salas JCCC, 11/20/2013 (modified FileAppender to be MultiRollingFileAppender)
 */
public class MultiRollingFileAppender extends FileAppender {

    /**
     * The code assumes that the following constants are in an increasing sequence.
     */
    static final int TOP_OF_TROUBLE = -1;
    static final int TOP_OF_MINUTE = 0;
    static final int TOP_OF_HOUR = 1;
    static final int HALF_DAY = 2;
    static final int TOP_OF_DAY = 3;
    static final int TOP_OF_WEEK = 4;
    static final int TOP_OF_MONTH = 5;

    /**
     * The date pattern. By default, the pattern is set to "'.'yyyy-MM-dd" meaning daily rollover.
     */
    private String datePattern = "'.'yyyy-MM-dd";

    /**
     * The default maximum file size is 10MB.
     */
    private long maxFileSize = 10 * 1024 * 1024;

    /**
     * There is one backup file by default.
     */
    private int maxBackupIndex = 1;

    /**
     * The log file will be renamed to the value of the scheduledFilename variable when the next interval is entered. For example, if the
     * rollover period is one hour, the log file will be renamed to the value of "scheduledFilename" at the beginning of the next hour. The
     * precise time when a rollover occurs depends on logging activity.
     */
    private String scheduledFilename;

    /**
     * The next time we estimate a rollover should occur.
     */
    private long nextCheck = System.currentTimeMillis() - 1;

    /**
     * current date
     */
    private Date now = new Date();

    /**
     * simple date format for formating the datePattern
     */
    private SimpleDateFormat sdf;

    private RollingCalendar rc = new RollingCalendar();

//    private int checkPeriod = TOP_OF_TROUBLE; commented this line out because i may not need to use it but will keep it here for maybe implementing it in the future.

    /**
     * The GMT_TIME_ZONE is used only in computeCheckPeriod() method.
     */
    static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");

    /**
     * The default constructor does nothing.
     */
    public MultiRollingFileAppender() {

    }

    /**
     * Instantiate a <code>MultiRollingFileAppender</code> and open the file designated by <code>filename</code>. The opened filename will become
     * the ouput destination for this appender.
     * 
     * @param layout - the layout being used
     * @param filename - the filename
     * @param datePattern - the date pattern
     * @throws IOException - Signals that an I/O exception of some sort has occurred. This class is the general 
     *                       class of exceptions produced by failed or interrupted I/O operations.
     */
    public MultiRollingFileAppender(final Layout layout, final String filename, final String datePattern) throws IOException {
        super(layout, filename, true);
        this.datePattern = datePattern;
        activateOptions();
    }

    /**
     * The <b>DatePattern</b> takes a string in the same format as expected by {@link SimpleDateFormat}. This options determines the rollover
     * schedule.
     * 
     * @param pattern - the date pattern  
     */
    public void setDatePattern(final String pattern) {
        datePattern = pattern;
    }

    /** 
     * Returns the value of the <b>DatePattern</b> option. 
     *
     * @return the date pattern
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * If the value of <b>File</b> is not <code>null</code>, then {@link #setFile} is called with the values of <b>File</b> and <b>Append</b>
     * properties.
     * 
     * @since 0.8.1
     */
    public void activateOptions() {
        // call super classes activate options ...
        super.activateOptions();

        // if the datePattern is not equal to null and fileName is not
        // equal to null then start initializing all the appenders options
        if (datePattern != null && fileName != null) {
            now.setTime(System.currentTimeMillis());
            sdf = new SimpleDateFormat(datePattern);
            final int type = computeCheckPeriod();
            printPeriodicity(type);
            rc.setType(type);
            final File file = new File(fileName);
            scheduledFilename = fileName + sdf.format(new Date(file.lastModified()));
        } else {
            // oops an error has occured so lets use log4j's logger that will log to a system out log.
            LogLog.error("Either File or DatePattern options are not set for appender [" + name + "].");
        }
    }

    /**
     * This method computes the roll over period by looping over the periods, starting with the shortest, and stopping when the r0 is different
     * from from r1, where r0 is the epoch formatted according the datePattern (supplied by the user) and r1 is the epoch+nextMillis(i) formatted
     * according to datePattern. All date formatting is done in GMT and not local format because the test logic is based on comparisons relative
     * to 1970-01-01 00:00:00 GMT (the epoch).
     * 
     * @return integer value for type of check period
     */
    int computeCheckPeriod() {
        // create an instance of the rolling calendar here
        final RollingCalendar rollingCalendar = new RollingCalendar(GMT_TIME_ZONE, Locale.ENGLISH);
        // set sate to 1970-01-01 00:00:00 GMT
        final Date epoch = new Date(0);
        // if datePattern is not equal null then initialize the date
        if (datePattern != null) {
            for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
                simpleDateFormat.setTimeZone(GMT_TIME_ZONE); // do all date formatting in GMT
                final String r0 = simpleDateFormat.format(epoch);
                rollingCalendar.setType(i);
                final Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
                final String r1 = simpleDateFormat.format(next);
                // System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);
                if (r0 != null && r1 != null && !r0.equals(r1)) {
                    return i;
                }
            }
        }
        return TOP_OF_TROUBLE; // Deliberately head for trouble...
    }

    /**
     * This method prints the type of datePattern that is being used to the console to 
     * let user know which type of roll over has been scheduled.
     * 
     * <p><b>NOTE: This is only accomplished by setting the debug attribute within 
     * the &lt;log4j:configuration&gt; element (log4j.xml) or the log4j.debug=true property 
     * (log4j.properties).</b></p>
     * 
     * @param type - integer type value of check period of file to be rolled over.
     */
    void printPeriodicity(final int type) {
        switch (type) {
            case TOP_OF_MINUTE:
                LogLog.debug("Appender [" + name + "] to be rolled every minute.");
                break;
            case TOP_OF_HOUR:
                LogLog.debug("Appender [" + name + "] to be rolled on top of every hour.");
                break;
            case HALF_DAY:
                LogLog.debug("Appender [" + name + "] to be rolled at midday and midnight.");
                break;
            case TOP_OF_DAY:
                LogLog.debug("Appender [" + name + "] to be rolled at midnight.");
                break;
            case TOP_OF_WEEK:
                LogLog.debug("Appender [" + name + "] to be rolled at start of week.");
                break;
            case TOP_OF_MONTH:
                LogLog.debug("Appender [" + name + "] to be rolled at start of every month.");
                break;
            default:
                LogLog.warn("Unknown periodicity for appender [" + name + "].");
        }
    }

    /**
     * This method differentiates MultiRollingFileAppender from its super class.
     * <p>
     * Before actually logging, this method will check whether it is time to do a rollover. If it is, it will schedule the next rollover time and
     * then rollover. If the <b>MaxFileSize</b> has been reached it will also roll over.
     * 
     * @param event - the log event that was captured
     */
    protected void subAppend(final LoggingEvent event) {
        final long n = System.currentTimeMillis();
        if (n >= nextCheck) {
            now.setTime(n);
            nextCheck = rc.getNextCheckMillis(now);
            try {
                // use scheduledRollOver() method here to roll over using the date pattern.
                scheduledRollOver();
            } catch (final IOException ioe) {
                LogLog.error("rollOver() failed.", ioe);
            }
        }

        // calling the super class subAppend method here to go ahead and append
        super.subAppend(event);

        // if fileName is not null then the counting queit writer (file in bytes) is greater than
        // the maxFileSize then rollover the file.
        if ((fileName != null) && ((CountingQuietWriter) qw).getCount() >= maxFileSize) {
            sizeRollOver();
        }

    }

    /**
     * This method is a scheduled roll over using the date pattern which will roll over by the user specified <b>DatePattern</b>.
     * 
     * @throws IOException - Signals that an I/O exception of some sort has occurred. This class
     *                       is the general class of exceptions produced by failed or interrupted I/O operations.
     */
    void scheduledRollOver() throws IOException {
        File file;
        File target;

        /* Compute filename, but only if datePattern is specified */
        if (datePattern == null) {
            errorHandler.error("Missing DatePattern option in rollOver().");
            return;
        }

        final String datedFilename = fileName + sdf.format(now);

        // It is too early to roll over because we are still within the
        // bounds of the current interval. Rollover will occur once the
        // next interval is reached.
        if (scheduledFilename.equals(datedFilename)) {
            return;
        }

        // get the formatted file name
        // basically here all we want is the filename in the following format:
        //                  ApplicationAppLog.datePattern_1.log
        final String formattedFileName = getFormattedFilename();
        final String extension = getFileExtension();

        // If maxBackups <= 0, then there is no file renaming to be done.
        if (maxBackupIndex > 0) {
            // Delete the oldest file, to keep Windows happy.
            file = new File(formattedFileName + maxBackupIndex + extension);
            if (file.exists()) {
                file.delete();
            }

            // Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3, 2}
            // this here runs through every file and renames them and if the maxBackIndex file has been reach it
            // is deleted.
            for (int i = maxBackupIndex - 1; i >= 1; i--) {
                file = new File(formattedFileName + i + extension);
                if (file.exists()) {
                    target = new File(formattedFileName + (i + 1) + extension);
                    LogLog.debug("Renaming file " + file + " to " + target);
                    file.renameTo(target);
                }
            }
        }

        // close current file (keep windows happy), and rename it using formattedFileName
        this.closeFile();

        // make the target file and delete the current one before renaming it as this is required
        target = new File(formattedFileName + 1 + extension);
        if (target.exists()) {
            target.delete();
        }

        // go ahead and get the current log file here so we can rename it to the target file. 
        file = new File(fileName);
        final boolean result = file.renameTo(target);
        if (result) {
            LogLog.debug("Renaming file " + fileName + " -> " + target);
        } else {
            LogLog.error("Failed to rename [" + fileName + "] to [" + target + "].");
        }

        try {
            // This will also close the file. This is OK since multiple
            // close operations are safe.
            this.setFile(fileName, false, this.bufferedIO, this.bufferSize);
        } catch (final IOException e) {
            errorHandler.error("setFile(" + fileName + ", false) call failed.");
        }
        // now set the new scheduledFilename
        scheduledFilename = datedFilename;
    }

    /**
     * Implements the usual roll over behaviour.
     * 
     * <p>This method is called when the MaxFileSize is reached.</p>
     *  
     * <p>Rollover the current file to a new file datePattern.</p>
     */
    void sizeRollOver() {
        File target;
        File file;

        // log some stuff here for showing why file is being rolled over...
        LogLog.debug("the rolling over count=" + ((CountingQuietWriter) qw).getCount());
        LogLog.debug("the maxBackupIndex=" + maxBackupIndex);

        // get the formatted file name
        // basically here all we want is the filename in the following format:
        //                  ApplicationAppLog.datePattern_1.log
        final String formattedFileName = getFormattedFilename();
        final String extension = getFileExtension();

        // If maxBackups <= 0, then there is no file renaming to be done.
        if (maxBackupIndex > 0) {
            // Delete the oldest file, to keep Windows happy.
            file = new File(formattedFileName + maxBackupIndex + extension);
            if (file.exists()) {
                file.delete();
            }

            // Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3, 2}
            // this here runs through every file and renames them and if the maxBackIndex file has been reach it
            // is deleted.
            for (int i = maxBackupIndex - 1; i >= 1; i--) {
                file = new File(formattedFileName + i + extension);
                if (file.exists()) {
                    target = new File(formattedFileName + (i + 1) + extension);
                    LogLog.debug("Renaming file " + file + " to " + target);
                    file.renameTo(target);
                }
            }

            // make the target file and delete the current one before renaming it as this is required
            target = new File(formattedFileName + 1 + extension);

            this.closeFile(); // keep windows happy.

            file = new File(fileName);
            final boolean result = file.renameTo(target);
            if (result) {
                LogLog.debug("Renaming file " + fileName + " -> " + target);
            } else {
                LogLog.error("Failed to rename [" + fileName + "] to [" + target + "].");
            }
        }

        try {
            // This will also close the file. This is OK since multiple
            // close operations are safe.
            this.setFile(fileName, false, bufferedIO, bufferSize);
        } catch (final IOException e) {
            LogLog.error("setFile(" + fileName + ", false) call failed.", e);
        }
    }

    /**
     * This method returns the file extension of <b>fileName</b> if one exists. If an extension
     * does not exist then this method will return an empty string.
     * 
     * @return the file extension as a string  
     */
    private String getFileExtension() {
        // get index of the last '.' from the file name and also get the string length of the file name 
        final int indexOfLastDot = fileName.lastIndexOf(".");
        final int lengthOfFileName = fileName.length();

        // here check to see if a known extension exists.
        if (indexOfLastDot > 0 && (lengthOfFileName - 4) == indexOfLastDot) {
            // substring out the extension here...
            return fileName.substring(indexOfLastDot, lengthOfFileName);
        } else {
            return "";
        }
    }

    /**
     * This method will return the <b>scheduledFilename</b> in a custom format if the <b>scheduledFilename</b>
     * contains two periods.
     * 
     * <p>If the <b>scheduledFilename</b> contains two periods then this method returns
     * a string in the following format: AppLog.DatePattern_</p>
     * 
     * <p>If the scheduledFilename does not contain periods this method will return the value of 
     * <b>scheduledFilename</b> in its original form with an underscore appended to the end of it.</p>
     * 
     * @return formatted file name based on condition (ex: AppLog.DatePattern_ or AppLog.log.DatePattern_)
     */
    private String getFormattedFilename() {
        final String[] split = scheduledFilename.split("\\.");

        final String returnFileName;
        
        if (split.length == 3 && split[1].length() == 3) {
            returnFileName = split[0] + "." + split[2] + "_";
        } else if (split.length == 2 && split[1].startsWith("log")) {
            returnFileName = split[0] + "." + split[1].replaceFirst("log", "") + "_";
        } else {
            returnFileName = scheduledFilename + "_";
        }
        return returnFileName;
    }

    /**
     * <p>This method sets and <i>opens</i> the file where the log output will go. The specified file must be writable.
     * 
     * <p>If there was already an opened file, then the previous file is closed first.</p>
     * 
     * <p><b>Do not use this method directly. To configure a FileAppender or one of its subclasses, set its properties one by one and then call
     * activateOptions.</b></p>
     * 
     * @param fileName
     *        The path to the log file.
     * @param append
     *        If true will append to fileName. Otherwise will truncate fileName.
     * @param bufferedIO - If set to true, the underlying OutputStreamWriter is wrapped by a BufferedWriter object. Setting BufferedIO
                           to true automatically sets ImmediateFlush options to false.
     * @param bufferSize - Size of BufferedWriter object's buffer; default value is 8192.
     * @throws IOException - Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions
     *                       produced by failed or interrupted I/O operations.
     */
    public synchronized void setFile(final String fileName, final boolean append, final boolean bufferedIO, final int bufferSize)
            throws IOException {
        // calling the super classes method here first.
        super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
        // if append is true then set the count of the new file.
        if (append) {
            final File f = new File(fileName);
            ((CountingQuietWriter) qw).setCount(f.length());
        }
    }

    /**
     * Set the maximum number of backup files to keep around.
     * 
     * <p>The <b>MaxBackupIndex</b> option determines how many backup files are kept before the oldest is erased. This option takes a positive
     * integer value. If set to zero, then there will be no backup files and the log file will be truncated when it reaches
     * <code>MaxFileSize</code>.</p>
     * 
     * @param maxBackups - max back number
     */
    public void setMaxBackupIndex(final int maxBackups) {
        this.maxBackupIndex = maxBackups;
    }

    /**
     * Set the maximum size that the output file is allowed to reach before being rolled over to backup files.
     * 
     * <p>This method is equivalent to {@link #setMaxFileSize} except that it is required for differentiating the setter taking a <code>long</code>
     * argument from the setter taking a <code>String</code> argument by the JavaBeans {@link java.beans.Introspector Introspector}.</p>
     * 
     * @see #setMaxFileSize(String)
     * @param maximumFileSize
     *        - the maximum file size
     */
    public void setMaximumFileSize(final long maximumFileSize) {
        this.maxFileSize = maximumFileSize;
    }

    /**
     * Get the maximum size that the output file is allowed to reach before being rolled over to backup files.
     * 
     * @return maximum file size
     */
    public long getMaximumFileSize() {
        return maxFileSize;
    }

    /**
     * Set the maximum size that the output file is allowed to reach before being rolled over to backup files.
     * <p>
     * In configuration files, the <b>MaxFileSize</b> option takes an long integer in the range 0 - 2^63. You can specify the value with the
     * suffixes "KB", "MB" or "GB" so that the integer is interpreted being expressed respectively in kilobytes, megabytes or gigabytes. For
     * example, the value "10KB" will be interpreted as 10240.
     * </p>
     * 
     * @param value
     *        - the max file size value
     */
    public void setMaxFileSize(final String value) {
        maxFileSize = OptionConverter.toFileSize(value, maxFileSize + 1);
    }

    /**
     * Sets the quiet writer being used. This method is overriden by {@link RollingFileAppender}.
     * @param writer - the object for writing to character streams
     */
    protected void setQWForFiles(final Writer writer) {
        this.qw = new CountingQuietWriter(writer, errorHandler);
    }

}

/**
 * RollingCalendar is a helper class to MultiRollingFileAppender. Given a periodicity type and the current time, it computes the start of the
 * next interval.
 */
class RollingCalendar extends GregorianCalendar {

    private static final long serialVersionUID = 3545690343068355733L;

    private int type = MultiRollingFileAppender.TOP_OF_TROUBLE;

    /**
     * RollingCalendar constructor which calls the constructor of the {@link java.util.GregorianCalendar} which
     * constructs a default {@link java.util.GregorianCalendar} using the current time in the default time zone 
     * with the default locale.
     */
    RollingCalendar() {
        super();
    }
    
    /**
     * RollingCalendar constructor which calls the constructor of the {@link java.util.GregorianCalendar} which 
     * constructs a {@link GregorianCalandar} based on the current time in the given time zone with the given locale.
     * 
     * @param tz - the given time zone.
     * @param locale - the given locale.
     */
    RollingCalendar(final TimeZone tz, final Locale locale) {
        super(tz, locale);
    }

    /**
     * Sets the type as int for check period of the rolling schedule.
     * 
     * @param type - integer type
     */
    void setType(final int type) {
        this.type = type;
    }

    /**
     * This method gets the next milliseconds to check
     * @param now - current date/time
     * @return long value
     */
    public long getNextCheckMillis(final Date now) {
        return getNextCheckDate(now).getTime();
    }

    /**
     * This method gets the next date to check based upon the type.
     * 
     * @param now current date
     * @return next check date
     */
    public Date getNextCheckDate(final Date now) {
        this.setTime(now);

        switch (type) {
            case MultiRollingFileAppender.TOP_OF_MINUTE:
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.MINUTE, 1);
                break;
            case MultiRollingFileAppender.TOP_OF_HOUR:
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.HOUR_OF_DAY, 1);
                break;
            case MultiRollingFileAppender.HALF_DAY:
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                final int hour = get(Calendar.HOUR_OF_DAY);
                if (hour < 12) {
                    this.set(Calendar.HOUR_OF_DAY, 12);
                } else {
                    this.set(Calendar.HOUR_OF_DAY, 0);
                    this.add(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            case MultiRollingFileAppender.TOP_OF_DAY:
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.DATE, 1);
                break;
            case MultiRollingFileAppender.TOP_OF_WEEK:
                this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case MultiRollingFileAppender.TOP_OF_MONTH:
                this.set(Calendar.DATE, 1);
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.MONTH, 1);
                break;
            default:
                throw new IllegalStateException("Unknown periodicity type.");
        }
        return getTime();
    }

}
