package com.kernja.factoryprovider.core.log;

import com.kernja.factoryprovider.core.interfaces.IFactoryProvider;
import com.kernja.factoryprovider.core.interfaces.IInjectedService;

public class LogService implements ILogService, IInjectedService {

    //log string formats
    protected String CONST_STRING_FULL = "%s(%s): %s";
    protected String CONST_STRING_PARTIAL = "%s: %s";

    //MOST CRITICAL
    final String CONST_TYPE_FATAL = "FATAL";
    final String CONST_TYPE_ERROR = "ERROR";
    final String CONST_TYPE_WARNING = "WARN ";
    final String CONST_TYPE_INFO = "INFO ";
    final String CONST_TYPE_DEBUG = "DEBUG";
    final String CONST_TYPE_VERBOSE = "VERBO";
    //LESS CRITICAL

    //MORE CRITICAL
    final int CONST_LEVEL_FATAL = 5;
    final int CONST_LEVEL_ERROR = 4;
    final int CONST_LEVEL_WARNING = 3;
    final int CONST_LEVEL_INFO = 2;
    final int CONST_LEVEL_DEBUG = 1;
    final int CONST_LEVEL_VERBOSE = 0;
    //LESS CRITICAL

    protected int mLogLevel = 2;

    public LogService()
    {
        setLogLevel(CONST_LEVEL_INFO);
    }

    public LogService(int pLogLevel)
    {
        setLogLevel(pLogLevel);
    }

    //fatal
    @Override
    public void f(String pMessage) {
        writeToConsole(CONST_LEVEL_FATAL, generateLog(CONST_TYPE_FATAL, pMessage));
    }
    @Override
    public void f(Class pClass, String pMessage) { writeToConsole(CONST_LEVEL_FATAL, generateLog(CONST_TYPE_FATAL, pClass, pMessage)); }
    @Override
    public void f(Object pObject, String pMessage) { writeToConsole(CONST_LEVEL_FATAL, generateLog(CONST_TYPE_FATAL, pObject, pMessage));  }

    //error
    @Override
    public void e(String pMessage) {
        writeToConsole(CONST_LEVEL_ERROR, generateLog(CONST_TYPE_ERROR, pMessage));
    }
    @Override
    public void e(Class pClass, String pMessage) { writeToConsole(CONST_LEVEL_ERROR, generateLog(CONST_TYPE_ERROR, pClass, pMessage)); }
    @Override
    public void e(Object pObject, String pMessage) { writeToConsole(CONST_LEVEL_ERROR, generateLog(CONST_TYPE_ERROR, pObject, pMessage));  }

    //warning
    @Override
    public void w(String pMessage) {
        writeToConsole(CONST_LEVEL_WARNING, generateLog(CONST_TYPE_WARNING, pMessage));
    }
    @Override
    public void w(Class pClass, String pMessage) { writeToConsole(CONST_LEVEL_WARNING, generateLog(CONST_TYPE_WARNING, pClass, pMessage)); }
    @Override
    public void w(Object pObject, String pMessage) { writeToConsole(CONST_LEVEL_WARNING, generateLog(CONST_TYPE_WARNING, pObject, pMessage));  }

    //info
    @Override
    public void i(String pMessage) {
        writeToConsole(CONST_LEVEL_INFO, generateLog(CONST_TYPE_INFO, pMessage));
    }
    @Override
    public void i(Class pClass, String pMessage) { writeToConsole(CONST_LEVEL_INFO, generateLog(CONST_TYPE_INFO, pClass, pMessage)); }
    @Override
    public void i(Object pObject, String pMessage) { writeToConsole(CONST_LEVEL_INFO, generateLog(CONST_TYPE_INFO, pObject, pMessage));  }

    //debug
    @Override
    public void d(String pMessage) {
        writeToConsole(CONST_LEVEL_DEBUG, generateLog(CONST_TYPE_DEBUG, pMessage));
    }
    @Override
    public void d(Class pClass, String pMessage) { writeToConsole(CONST_LEVEL_DEBUG, generateLog(CONST_TYPE_DEBUG, pClass, pMessage)); }
    @Override
    public void d(Object pObject, String pMessage) { writeToConsole(CONST_LEVEL_DEBUG, generateLog(CONST_TYPE_DEBUG, pObject, pMessage));  }

    //verbose
    @Override
    public void v(String pMessage) {
        writeToConsole(CONST_LEVEL_VERBOSE, generateLog(CONST_TYPE_VERBOSE, pMessage));
    }
    @Override
    public void v(Class pClass, String pMessage) { writeToConsole(CONST_LEVEL_VERBOSE, generateLog(CONST_TYPE_VERBOSE, pClass, pMessage)); }
    @Override
    public void v(Object pObject, String pMessage) { writeToConsole(CONST_LEVEL_VERBOSE, generateLog(CONST_TYPE_VERBOSE, pObject, pMessage));  }

    //helpers
    @Override
    public void setLogLevel(int pLogLevel)
    {
        mLogLevel = pLogLevel;

        //bound check logging levels
        if (mLogLevel < CONST_LEVEL_VERBOSE)
        {
            mLogLevel = CONST_LEVEL_VERBOSE;
        } else if (mLogLevel > CONST_LEVEL_FATAL)
        {
            mLogLevel = CONST_LEVEL_FATAL;
        }
    }

    //standard ways to generate messages
    protected String generateLog(String pLevel, Class pClass, String pMessage)
    {
        String className = getClassName(pClass, null);

        if (className == null)
        {
            return generateLog(pLevel, pMessage);
        } else {
            return String.format(CONST_STRING_FULL, pLevel, className, pMessage);
        }
    }

    protected String generateLog(String pLevel, Object pObject, String pMessage)
    {
        if (pObject != null && pObject.getClass() != null)
        {
            return this.generateLog(pLevel, pObject.getClass(), pMessage);
        } else {
            return this.generateLog(pLevel, pMessage);
        }
    }

    protected String generateLog(String pLevel, String pMessage)
    {
        return String.format(CONST_STRING_PARTIAL, pLevel, pMessage);
    }

    //helper method to write to console
    protected boolean writeToConsole(int pLogLevel, String pMessage)
    {
        //see if we match our loglevel threshold
        if (pLogLevel >= mLogLevel)
        {
            //try to print to the console
            if (System.console() != null)
            {
                System.console().printf(pMessage);
            } else {
                System.out.println(pMessage);
            }

            //return true
            return true;
        }

        //cannot write the log, not meeting our log level threshold
        return false;
    }

    //helper method to get the full name of a class, including package
    protected <T extends Object> String getClassName(Class<T> pClass, String pDefaultValue)
    {
        if (pClass == null)
            return pDefaultValue;

        return pClass.getName();
    }

    @Override
    public void injectRequiredDependencies(IFactoryProvider pProvider) throws NoClassDefFoundError, ClassCastException {

    }
}
