package com.atlassian.templaterenderer.velocity.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

public class CommonsLogChute implements LogChute
{
    private final Log log = LogFactory.getLog(getClass());
    
    public void init(RuntimeServices arg0) throws Exception {}

    public boolean isLevelEnabled(int level)
    {
        switch (level)
        {
            case LogChute.DEBUG_ID:
                return log.isDebugEnabled();
            case LogChute.ERROR_ID:
                return log.isErrorEnabled();
            case LogChute.INFO_ID:
                return log.isInfoEnabled();
            case LogChute.TRACE_ID:
                return log.isTraceEnabled();
            case LogChute.WARN_ID:
                return log.isWarnEnabled();
            default:
                return false;
        }
    }

    public void log(int level, String message)
    {
        switch (level)
        {
            case LogChute.DEBUG_ID:
                log.debug(message);
            case LogChute.ERROR_ID:
                log.error(message);
            case LogChute.INFO_ID:
                log.info(message);
            case LogChute.TRACE_ID:
                log.trace(message);
            case LogChute.WARN_ID:
                log.warn(message);
        }
    }

    public void log(int level, String message, Throwable cause)
    {
        switch (level)
        {
            case LogChute.DEBUG_ID:
                log.debug(message, cause);
            case LogChute.ERROR_ID:
                log.error(message, cause);
            case LogChute.INFO_ID:
                log.info(message, cause);
            case LogChute.TRACE_ID:
                log.trace(message, cause);
            case LogChute.WARN_ID:
                log.warn(message, cause);
        }
    }
}
