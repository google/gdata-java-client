/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.util.common.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A wrapped logger chock-full of convenience methods.
 *
 * The introduction of varargs in Java 5 has provided two ways to further
 * simplify logging information.<p/>
 *
 * First, the level-based convenience methods such as
 * {@code Logger.foo(String msg)} can contain an additional
 * {@code Object... args} parameter that formats your message according to the
 * logger's {@link java.util.logging.Formatter}.<p/>
 *
 * Second, for those times when you don't want the
 * {@link java.util.logging.Formatter} to manage formatting your message, you
 * can use the {@code FormattingLogger.xxxfmt(String fmt, Object... params)}
 * methods. These methods leverage the Java 5
 * {@link String#format(String format, Object... args)} method. However,
 * {@code logger.infofmt("%s %s", foo, bar)} is slightly more efficient than
 * {@code logger.info(String.format("%s %s", foo, bar))} since the text is
 * only formatted when the message is sufficiently important. It's also a little
 * more readable.<p/>
 *
 * Keep in mind that this class mixes two ways to format text: one that expects
 * text to be formatted by the logger's {@link java.util.logging.Formatter}
 * and one that depends upon the new
 * {@link java.lang.String#format(java.lang.String, java.lang.Object[])} style.
 * If, in this class, method name ends with {@code fmt}, it's formatting using
 * the latter. If the class method does not end with {@code fmt}, it's using
 * the traditional formatting style.
 *
 * NOTE(future extenders): any time a new {@link #logfmt} variant is added
 * to this class a matching {@link #logpfmt} variant must also be added, and
 * vice versa.  This enables {@code
 * com.google.monitoring.runtime.instrumentation.LogFasterer} to do its job.
 *
 *   by calls to the private method doLog, which sets the resource bundle.
 *   FormattingLogger does not support that yet; this functionality may change.
 *
 * 
 */
public final class FormattingLogger {
  // For use with LogRecord.inferCaller
  private static final String LOGGER_CLASS_NAME =
      FormattingLogger.class.getName();

  /**
   * An extension of {@link LogRecord} used to support a custom version of
   * its private {@code inferCaller(String)} method.
   *
   * 
   */
  public static class Record extends LogRecord {

    private static final long serialVersionUID = 1L;

    private boolean needToInferCaller;
    private String sourceMethodName;
    private String sourceClassName;

    /**
     * Arguments passed to *fmt variants that we want to keep track of for
     * users of this object.
     */
    private final Object[] formatterArgs;
    private final String formatterFormat;

    @Override
    public String getSourceMethodName() {
      if (needToInferCaller) {
        inferCaller(LOGGER_CLASS_NAME);
      }
      return sourceMethodName;
    }

    @Override
    public String getSourceClassName() {
      if (needToInferCaller) {
        inferCaller(LOGGER_CLASS_NAME);
      }
      return sourceClassName;
    }

    public Object[] getFormatterArgs() {
      return formatterArgs;
    }

    public String getFormatterFormat() {
      return formatterFormat;
    }

    @Override
    public void setSourceClassName(String sourceClassName) {
      this.sourceClassName = sourceClassName;
      super.setSourceClassName(sourceClassName);
    }

    @Override
    public void setSourceMethodName(String sourceMethodName) {
      this.sourceMethodName = sourceMethodName;
      super.setSourceMethodName(sourceMethodName);
    }

    /**
     * @param level
     * @param msg
     */
    Record(Level level, String msg) {
      super(level, msg);
      needToInferCaller = true;
      formatterFormat = null;
      formatterArgs = null;
    }

    /**
     * Constructs a Record object given a {@link java.util.Formatter} format
     * string and arguments
     *
     */
    Record(Level level, String fmt, Object[] args) {
      super(level, String.format(fmt, args));
      needToInferCaller = true;
      formatterFormat = fmt;
      formatterArgs = args;
    }

    /**
     * Constructs a Record object when the class + method context are known.
     * @param level the severity of the log statement
     * @param msg the content of the log statement
     * @param sourceClassName the class generating the log statement
     * @param sourceMethodName the method generating the log statement
     */
    Record(Level level, String msg, String sourceClassName,
        String sourceMethodName) {
      super(level, msg);
      setSourceClassName(sourceClassName);
      setSourceMethodName(sourceMethodName);
      formatterFormat = null;
      formatterArgs = null;
    }

    /**
     * Constructs a Record object when the class + method context are known and
     * {@link java.util.Formatter} messages are used.
     */
    Record(Level level, String fmt, Object[] args, String sourceClassName,
        String sourceMethodName) {
      super(level, String.format(fmt, args));
      setSourceClassName(sourceClassName);
      setSourceMethodName(sourceMethodName);
      formatterFormat = fmt;
      formatterArgs = args;
    }

    /**
     * Like LogRecord.inferCaller, we're making a 'best effort'.
     */
    protected void inferCaller(String loggerClassName) {
      needToInferCaller = false;

      // true when searching for loggerClassName, false when not.
      boolean isSearchingForLogger = true;
      for (StackTraceElement elem : new Throwable().getStackTrace()) {
        String className = elem.getClassName();
        boolean matches = className.equals(loggerClassName);
        // Found FormattingLogger in the stack.
        if (matches && isSearchingForLogger) {
          isSearchingForLogger = false;
          continue;
        }
        // Found first call past FormattingLogger.
        if (!matches && !isSearchingForLogger) {
          setSourceClassName(className);
          setSourceMethodName(elem.getMethodName());
          return;
        }
      }
    }
  }

  private final Logger logger;

  /**
   * Returns an instance of {@code FormattingLogger}.
   */
  public static FormattingLogger getLogger(String name) {
    return new FormattingLogger(Logger.getLogger(name));
  }

  /**
   * Returns an instance of {@code FormattingLogger} using the
   * {@link Class#getCanonicalName()} as a source for the underlying logger's
   * name.
   */
  public static FormattingLogger getLogger(Class<?> cls) {
    return getLogger(cls.getCanonicalName());
  }

  /**
   * Creates a new FormattingLogger.
   *
   * This is convenience ctor that creates a named backing {@code Logger} with
   * the name of the passed-in class.
   *
   * @param cls The class whose name will be used to name the backing logger.
   */
  public FormattingLogger(Class<?> cls) {
    this(Logger.getLogger(cls.getCanonicalName()));
  }

  /**
   * Creates a new FormattingLogger.
   *
   * This is a convenience ctor that creates an anonymous backing
   * {@code Logger}.
   */
  public FormattingLogger() {
    this(Logger.getAnonymousLogger());
  }

  /**
   * Creates a new FormattingLogger by wrapping a {@code Logger}.
   *
   * @param logger The {@code Logger} to wrap.
   */
  public FormattingLogger(Logger logger) {
    if (logger == null) {
      throw new NullPointerException("logger is null");
    }
    this.logger = logger;
  }

  /**
   * Gets the {@code Logger} wrapped by this FormattingLogger.
   * @return the {@code Logger} wrapped by this FormattingLogger.
   */
  public Logger getFormattingLogger() {
    return logger;
  }

  /**
   * Similar to {@link Logger#log(Level, String, Object[])}, allows you to delay
   * formatting with {@link java.text.MessageFormat}.
   */
  public void log(Level level, String msg, Object... params) {
    if (!isLoggable(level)) {
      return;
    }
    Record record = new Record(level, msg);
    record.setParameters(params);
    nameAndLog(record);
  }

  /**
   * Similar to {@link Logger#log(Level, String, Throwable)}, sets the value
   * of {@link LogRecord#getThrown()}.
   */
  public void log(Level level, String msg, Throwable thrown) {
    if (!isLoggable(level)) {
      return;
    }
    Record record = new Record(level, msg);
    record.setThrown(thrown);
    nameAndLog(record);
  }

  /**
   * Similar to {@link Logger#log(Level, String, Throwable)}, but takes
   * variable arguments and allows you to delay formatting with
   * {@link java.text.MessageFormat}.
   * Calls {@link LogRecord#setThrown(Throwable)} if thrown is non-null.
   */
  public void log(Level level, Throwable thrown,
                  String msg, Object... params) {
    if (!isLoggable(level)) {
      return;
    }
    Record record = new Record(level, msg);
    if (thrown != null) {
      record.setThrown(thrown);
    }
    if (params != null && params.length != 0) {
      record.setParameters(params);
    }
    nameAndLog(record);
  }

  private void nameAndLog(Record record) {
    record.setLoggerName(logger.getName());
    log(record);
  }

  /**
   * Log a {@link java.util.logging.LogRecord}. The log message, level and
   * other parameters are contained in the {@code LogRecord}, but the source
   * class and method will be set if necessary.
   */
  public void log(LogRecord lr) {
    if (!(lr instanceof Record)) {
      // set the source class and method name by creating a bogus
      // record, and getting the class and method names from it.
      // This may look like a hack, but it's pretty much no worse than anything
      // else, we'll need to create a new Object, no matter what. Hey, we need
      // the stack trace, so this is peanuts compared to that.
      Record rec = new Record(Level.INFO, null);
      lr.setSourceClassName(rec.getSourceClassName());
      lr.setSourceMethodName(rec.getSourceMethodName());
    }
    logger.log(lr);
  }

  /**
   * Logs an event with a known context, similar to
   * {@link java.util.logging.Logger#logp(java.util.logging.Level, String, String, String)}.
   * However, this delays formatting of the message.
   * @param level one of the message level identifiers, e.g. SEVERE
   * @param sourceClassName the class generating this log event
   * @param sourceMethodName the method generating this log event
   * @param msg the basic message string
   * @param params the parameters for the message string
   */
  public void logp(Level level, String sourceClassName, String sourceMethodName,
      String msg, Object... params) {
    if (!isLoggable(level)) {
      return;
    }
    Record record = new Record(level, msg, sourceClassName, sourceMethodName);
    if (params != null && params.length != 0) {
      record.setParameters(params);
    }
    nameAndLog(record);
  }

  /**
   * Logs an event with a known context, similar to
   * {@link java.util.logging.Logger#logp(java.util.logging.Level, String,
   * String, String, Throwable)}.
   * However, this delays formatting of the message.
   * @param level one of the message level identifiers, e.g. SEVERE
   * @param sourceClassName the class generating this log event
   * @param sourceMethodName the method generating this log event
   * @param msg the message string
   * @param thrown the throwable which triggered this log event
   */
  public void logp(Level level, String sourceClassName, String sourceMethodName,
      String msg, Throwable thrown) {
    if (!isLoggable(level)) {
      return;
    }
    Record record = new Record(level, msg, sourceClassName, sourceMethodName);
    record.setThrown(thrown);
    nameAndLog(record);
  }

  /**
   * Logs an event with a known context, similar to
   * {@link java.util.logging.Logger#logp(java.util.logging.Level, String,
   * String, String, Throwable)}.
   * However, this delays formatting of the message.
   * @param level one of the message level identifiers, e.g. SEVERE
   * @param sourceClassName the class generating this log event
   * @param sourceMethodName the method generating this log event
   * @param thrown the throwable which triggered this log event
   * @param msg the basic message string
   * @param params the parameters for the message string
   */
  public void logp(Level level, String sourceClassName, String sourceMethodName,
      Throwable thrown, String msg, Object... params) {
    if (!isLoggable(level)) {
      return;
    }
    Record record = new Record(level, msg, sourceClassName, sourceMethodName);
    if (thrown != null) {
      record.setThrown(thrown);
    }
    if (params != null && params.length != 0) {
      record.setParameters(params);
    }
    nameAndLog(record);
  }

  /** Pass-through to {@link Logger#isLoggable(Level)} */
  public boolean isLoggable(Level level) {
    return logger.isLoggable(level);
  }

  /** Pass-through to {@link Logger#getLevel()} */
  public Level getLevel() {
    return logger.getLevel();
  }

  /** Pass-through to {@link Logger#setLevel(Level)} */
  public void setLevel(Level level) {
    logger.setLevel(level);
  }

  /**
   * Log a message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   *
   * @param level One of the message level identifiers, e.g. SEVERE
   * @param fmt The string message (or a key in the message catalog)
   * @param args array of parameters to the message
   */
  public void logfmt(Level level, String fmt, Object... args) {
    if (logger.isLoggable(level)) {
      Record record = new Record(level, fmt, args);
      nameAndLog(record);
    }
  }

  /**
   * Log a message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the {@code fmt} and {@code thrown} are converted to a string
   * using {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   *
   * @param level One of the message level identifiers, e.g. SEVERE
   * @param fmt The string message (or a key in the message catalog)
   * @param thrown the throwable which triggered this log event,
   * and parameter to the format message
   */
  public void logfmt(Level level, String fmt, Throwable thrown) {
    if (logger.isLoggable(level)) {
      Record record = new Record(level, fmt, new Object[]{thrown});
      record.setThrown(thrown);
      nameAndLog(record);
    }
  }

  /**
   * Log a message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   *
   * @param level one of the message level identifiers, e.g. SEVERE
   * @param thrown the throwable which triggered this log event
   * @param fmt the string message (or a key in the message catalog)
   * @param args array of parameters to the message
   */
  public void logfmt(Level level, Throwable thrown,
      String fmt, Object... args) {
    if (logger.isLoggable(level)) {
      Record record = new Record(level, fmt, args);
      record.setThrown(thrown);
      nameAndLog(record);
    }
  }

  /**
   * Logs an event with a known context, using explicit formatting rules.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   * @param level one of the message level identifiers, e.g. SEVERE
   * @param sourceClassName the class generating this log event
   * @param sourceMethodName the method generating this log event
   * @param fmt the string message (or a key in the message catalog)
   * @param args array of parameters to the format message
   */
  public void logpfmt(Level level, String sourceClassName,
      String sourceMethodName, String fmt, Object... args) {
    if (!isLoggable(level)) {
      return;
    }
    Record record =
        new Record(level, fmt, args, sourceClassName, sourceMethodName);
    nameAndLog(record);
  }

  /**
   * Logs an event with a known context, using explicit formatting rules.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the {@code fmt} and {@code thrown} are converted to a string
   * using {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   * @param level one of the message level identifiers, e.g. SEVERE
   * @param sourceClassName the class generating this log event
   * @param sourceMethodName the method generating this log event
   * @param fmt the string message (or a key in the message catalog)
   * @param thrown the throwable which triggered this log event,
   * and parameter to the format message
   */
  public void logpfmt(Level level, String sourceClassName,
      String sourceMethodName, String fmt, Throwable thrown) {
    if (!isLoggable(level)) {
      return;
    }
    Record record = new Record(
        level, fmt, new Object[]{thrown},
        sourceClassName, sourceMethodName);
    record.setThrown(thrown);
    nameAndLog(record);
  }

  /**
   * Logs an event with a known context, using explicit formatting rules.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   *
   * @param level one of the message level identifiers, e.g. SEVERE
   * @param sourceClassName the class generating this log event
   * @param sourceMethodName the method generating this log event
   * @param thrown the throwable which triggered this log event
   * @param fmt the string message (or a key in the message catalog)
   * @param args array of parameters to the message
   */
  public void logpfmt(Level level, String sourceClassName,
      String sourceMethodName, Throwable thrown,
      String fmt, Object... args) {
    if (logger.isLoggable(level)) {
      logp(level, sourceClassName, sourceMethodName, thrown,
           String.format(fmt, args), (Object[]) null);
    }
  }

  /**
   * Log a SEVERE message.
   * <p>
   * If the logger is currently enabled for the SEVERE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param thrown the throwable which triggered this log event,
   * and parameter to the message
   */
  public void severe(String msg, Throwable thrown) {
    log(Level.SEVERE, thrown, msg, thrown);
  }

  /**
   * Log a WARNING message.
   * <p>
   * If the logger is currently enabled for the WARNING message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param thrown the throwable which triggered this log event,
   * and parameter to the message
   */
  public void warning(String msg, Throwable thrown) {
    log(Level.WARNING, thrown, msg, thrown);
  }

  /**
   * Log an INFO message.
   * <p>
   * If the logger is currently enabled for the INFO message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param thrown the throwable which triggered this log event,
   * and parameter to the message
   */
  public void info(String msg, Throwable thrown) {
    log(Level.INFO, thrown, msg, thrown);
  }

  /**
   * Log a CONFIG message.
   * <p>
   * If the logger is currently enabled for the CONFIG message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param thrown the throwable which triggered this log event,
   * and parameter to the message
   */
  public void config(String msg, Throwable thrown) {
    log(Level.CONFIG, thrown, msg, thrown);
  }

  /**
   * Log a FINE message.
   * <p>
   * If the logger is currently enabled for the FINE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param thrown the throwable which triggered this log event,
   * and parameter to the message
   */
  public void fine(String msg, Throwable thrown) {
    log(Level.FINE, thrown, msg, thrown);
  }

  /**
   * Log a FINER message.
   * <p>
   * If the logger is currently enabled for the FINER message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param thrown the throwable which triggered this log event,
   * and parameter to the message
   */
  public void finer(String msg, Throwable thrown) {
    log(Level.FINER, thrown, msg, thrown);
  }

  /**
   * Log a FINEST message.
   * <p>
   * If the logger is currently enabled for the FINEST message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param thrown the throwable which triggered this log event,
   * and parameter to the message
   */
  public void finest(String msg, Throwable thrown) {
    log(Level.FINEST, thrown, msg, thrown);
  }

  /**
   * Log a SEVERE message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the {@code fmt} and {@code thrown} are converted to a string
   * using {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param thrown the throwable which triggered this log event,
   * and parameter to the formatter message
   * @see java.util.logging.Formatter
   * @see String#format(String, Object[])
   */
  public void severefmt(String fmt, Throwable thrown) {
    logfmt(Level.SEVERE, thrown, fmt, thrown);
  }

  /**
   * Log a WARNING message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the {@code fmt} and {@code thrown} are converted to a string
   * using {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param thrown the throwable which triggered this log event,
   * and parameter to the formatter message
   * @see java.util.logging.Formatter
   * @see String#format(String, Object[])
   */
  public void warningfmt(String fmt, Throwable thrown) {
    logfmt(Level.WARNING, thrown, fmt, thrown);
  }

  /**
   * Log an INFO message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the {@code fmt} and {@code thrown} are converted to a string
   * using {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param thrown the throwable which triggered this log event,
   * and parameter to the formatter message
   * @see java.util.logging.Formatter
   * @see String#format(String, Object[])
   */
  public void infofmt(String fmt, Throwable thrown) {
    logfmt(Level.INFO, thrown, fmt, thrown);
  }

  /**
   * Log a CONFIG message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the {@code fmt} and {@code thrown} are converted to a string
   * using {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param thrown the throwable which triggered this log event,
   * and parameter to the formatter message
   * @see java.util.logging.Formatter
   * @see String#format(java.util.Locale, String, Object[])
   */
  public void configfmt(String fmt, Throwable thrown) {
    logfmt(Level.CONFIG, thrown, fmt, thrown);
  }

  /**
   * Log a FINE message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the {@code fmt} and {@code thrown} are converted to a string
   * using {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param thrown the throwable which triggered this log event,
   * and parameter to the formatter message
   * @see java.util.logging.Formatter
   * @see String#format(String, Object[])
   */
  public void finefmt(String fmt, Throwable thrown) {
    logfmt(Level.FINE, thrown, fmt, thrown);
  }

  /**
   * Log a FINER message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the {@code fmt} and {@code thrown} are converted to a string
   * using {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param thrown the throwable which triggered this log event,
   * and parameter to the formatter message
   * @see java.util.logging.Formatter
   * @see String#format(String, Object[])
   */
  public void finerfmt(String fmt, Throwable thrown) {
    logfmt(Level.FINER, thrown, fmt, thrown);
  }

  /**
   * Log a FINEST message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the {@code fmt} and {@code thrown} are converted to a string
   * using {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param thrown the throwable which triggered this log event,
   * and parameter to the formatter message
   * @see java.util.logging.Formatter
   * @see String#format(String, Object[])
   */
  public void finestfmt(String fmt, Throwable thrown) {
    logfmt(Level.FINEST, thrown, fmt, thrown);
  }

  /**
   * Log a SEVERE message, with an array of object arguments.
   * <p>
   * If the logger is currently enabled for the SEVERE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param params array of parameters to the message
   */
  public void severe(String msg, Object... params) {
    log(Level.SEVERE, msg, params);
  }

  /**
   * Log a WARNING message.
   * <p>
   * If the logger is currently enabled for the WARNING message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param params array of parameters to the message
   */
  public void warning(String msg, Object... params) {
    log(Level.WARNING, msg, params);
  }

  /**
   * Log an INFO message.
   * <p>
   * If the logger is currently enabled for the INFO message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param params array of parameters to the message
   */
  public void info(String msg, Object... params) {
    log(Level.INFO, msg, params);
  }

  /**
   * Log a CONFIG message.
   * <p>
   * If the logger is currently enabled for the CONFIG message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param params array of parameters to the message
   */
  public void config(String msg, Object... params) {
    log(Level.CONFIG, msg, params);
  }

  /**
   * Log a FINE message.
   * <p>
   * If the logger is currently enabled for the FINE message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param params array of parameters to the message
   */
  public void fine(String msg, Object... params) {
    log(Level.FINE, msg, params);
  }

  /**
   * Log a FINER message.
   * <p>
   * If the logger is currently enabled for the FINER message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param params array of parameters to the message
   */
  public void finer(String msg, Object... params) {
    log(Level.FINER, msg, params);
  }

  /**
   * Log a FINEST message.
   * <p>
   * If the logger is currently enabled for the FINEST message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param msg The string message (or a key in the message catalog)
   * @param params array of parameters to the message
   */
  public void finest(String msg, Object... params) {
    log(Level.FINEST, msg, params);
  }

  /**
   * Log a SEVERE message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param args array of parameters to the formatter
   * @see java.util.logging.Formatter
   * @see String#format(String, Object[])
   */
  public void severefmt(String fmt, Object... args) {
    logfmt(Level.SEVERE, fmt, args);
  }

  /**
   * Log a WARNING message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param args array of parameters to the formatter
   * @see java.util.logging.Formatter
   * @see String#format(String, Object[])
   */
  public void warningfmt(String fmt, Object... args) {
    logfmt(Level.WARNING, fmt, args);
  }

  /**
   * Log an INFO message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param args array of parameters to the formatter
   * @see java.util.logging.Formatter
   * @see String#format(String, Object[])
   */
  public void infofmt(String fmt, Object... args) {
    logfmt(Level.INFO, fmt, args);
  }

  /**
   * Log a CONFIG message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param args array of parameters to the formatter
   * @see java.util.logging.Formatter
   * @see String#format(java.util.Locale, String, Object[])
   */
  public void configfmt(String fmt, Object... args) {
    logfmt(Level.CONFIG, fmt, args);
  }

  /**
   * Log a FINE message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param args array of parameters to the formatter
   * @see java.util.logging.Formatter
   * @see String#format(String, Object[])
   */
  public void finefmt(String fmt, Object... args) {
    logfmt(Level.FINE, fmt, args);
  }

  /**
   * Log a FINER message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param args array of parameters to the formatter
   * @see java.util.logging.Formatter
   * @see String#format(String, Object[])
   */
  public void finerfmt(String fmt, Object... args) {
    logfmt(Level.FINER, fmt, args);
  }

  /**
   * Log a FINEST message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and is forwarded to all the registered output Handler objects.
   *
   * @param fmt A format string
   * @param args array of parameters to the formatter
   * @see java.util.logging.Formatter
   * @see String#format(String, Object[])
   */
  public void finestfmt(String fmt, Object... args) {
    logfmt(Level.FINEST, fmt, args);
  }

  /**
   * Log a SEVERE message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   *
   * @param thrown the throwable which triggered this log event
   * @param fmt the string message (or a key in the message catalog)
   * @param args array of parameters to the message
   */
  public void severefmt(Throwable thrown, String fmt, Object... args) {
    logfmt(Level.SEVERE, thrown, fmt, args);
  }

  /**
   * Log a WARNING message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   *
   * @param thrown the throwable which triggered this log event
   * @param fmt the string message (or a key in the message catalog)
   * @param args array of parameters to the message
   */
  public void warningfmt(Throwable thrown, String fmt, Object... args) {
    logfmt(Level.WARNING, thrown, fmt, args);
  }

  /**
   * Log a INFO message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   *
   * @param thrown the throwable which triggered this log event
   * @param fmt the string message (or a key in the message catalog)
   * @param args array of parameters to the message
   */
  public void infofmt(Throwable thrown, String fmt, Object... args) {
    logfmt(Level.INFO, thrown, fmt, args);
  }

  /**
   * Log a CONFIG message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   *
   * @param thrown the throwable which triggered this log event
   * @param fmt the string message (or a key in the message catalog)
   * @param args array of parameters to the message
   */
  public void configfmt(Throwable thrown, String fmt, Object... args) {
    logfmt(Level.CONFIG, thrown, fmt, args);
  }

  /**
   * Log a FINE message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   *
   * @param thrown the throwable which triggered this log event
   * @param fmt the string message (or a key in the message catalog)
   * @param args array of parameters to the message
   */
  public void finefmt(Throwable thrown, String fmt, Object... args) {
    logfmt(Level.FINE, thrown, fmt, args);
  }

  /**
   * Log a FINER message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   *
   * @param thrown the throwable which triggered this log event
   * @param fmt the string message (or a key in the message catalog)
   * @param args array of parameters to the message
   */
  public void finerfmt(Throwable thrown, String fmt, Object... args) {
    logfmt(Level.FINER, thrown, fmt, args);
  }

  /**
   * Log a FINEST message.
   * <p>
   * If the logger currently accepts messages of the supplied level,
   * then the fmt and args are converted to a string using
   * {@link String#format(String, Object[])}
   * and forwarded to all the registered output Handler objects.
   *
   * @param thrown the throwable which triggered this log event
   * @param fmt the string message (or a key in the message catalog)
   * @param args array of parameters to the message
   */
  public void finestfmt(Throwable thrown, String fmt, Object... args) {
    logfmt(Level.FINEST, thrown, fmt, args);
  }
}
