package org.fastest.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * @Date: 2021/12/18
 */
public class FastLogger {
    private static volatile FastLogger instance = null;
    private final Logger logger;
    final boolean traceCapable;

    private FastLogger(String name){
        this.logger = LogManager.getLogger(name);
        this.traceCapable =this.isTraceCapable();
    }

    public static synchronized FastLogger getLogger(String name){
        if(Objects.isNull(instance)){
            synchronized (FastLogger.class){
                if(Objects.isNull(instance)){
                    instance = new FastLogger(name);
                }
            }
        }
        return instance;
    }

    public static synchronized FastLogger getLogger(Class<?> clazz){
        return getLogger(clazz.getName());
    }

    private boolean isTraceCapable() {
        try {
            this.logger.isTraceEnabled();
            return true;
        } catch (NoSuchMethodError var2) {
            return false;
        }
    }

    public boolean isTraceEnabled() {
        return this.traceCapable ? this.logger.isTraceEnabled() : this.logger.isDebugEnabled();
    }

    public void trace(String msg) {
        this.logger.trace(msg);
    }

    public void trace(String format, Object arg) {
        this.logger.trace(format, arg);
    }

    public void trace(String format, Object arg1, Object arg2) {
        this.logger.trace(format, arg1, arg2);
    }

    public void trace(String format, Object... arguments) {
        this.logger.trace(format, arguments);
    }

    public void trace(String msg, Throwable t) {
        this.logger.trace(msg, t);
    }

    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    public void debug(String msg) {
        this.logger.debug(msg);
    }

    public void debug(String format, Object arg) {
        this.logger.debug(format, arg);
    }

    public void debug(String format, Object arg1, Object arg2) {
        this.logger.debug(format, arg1, arg2);
    }

    public void debug(String format, Object... arguments) {
        this.logger.debug(format, arguments);
    }

    public void debug(String msg, Throwable t) {
        this.logger.debug(msg, t);
    }

    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    public void info(String msg) {
        this.logger.info(msg);
    }

    public void info(String format, Object arg) {
        this.logger.info(format, arg);
    }

    public void info(String format, Object arg1, Object arg2) {
        this.logger.info(format, arg1, arg2);
    }

    public void info(String format, Object... argArray) {
        this.logger.info(format, argArray);
    }

    public void info(String msg, Throwable t) {
        this.logger.info(msg, t);
    }

    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }

    public void warn(String msg) {
        this.logger.warn(msg);
    }

    public void warn(String format, Object arg) {
        this.logger.warn(format, arg);
    }

    public void warn(String format, Object arg1, Object arg2) {
        this.logger.warn(format, arg1, arg2);
    }

    public void warn(String format, Object... argArray) {
        this.logger.warn(format, argArray);
    }

    public void warn(String msg, Throwable t) {
        this.logger.warn(msg, t);
    }

    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }

    public void error(String msg) {
        this.logger.error(msg);
    }

    public void error(String format, Object arg) {
        this.logger.error(format, arg);
    }

    public void error(String format, Object arg1, Object arg2) {
        this.logger.error(format, arg1, arg2);
    }

    public void error(String format, Object... argArray) {
        this.logger.error(format, argArray);
    }

    public void error(String msg, Throwable t) {
        this.logger.error(msg, t);
    }
}
