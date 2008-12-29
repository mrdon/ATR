package com.atlassian.templaterenderer;

public class RenderingException extends RuntimeException
{
    public RenderingException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public RenderingException(String message)
    {
        super(message);
    }

    public RenderingException(Throwable cause)
    {
        super(cause);
    }
}
