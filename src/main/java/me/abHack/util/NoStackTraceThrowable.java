// 
// Decompiled by Procyon v0.5.36
// 

package me.abHack.util;

public class NoStackTraceThrowable extends RuntimeException
{
    public NoStackTraceThrowable(final String msg) {
        super(msg);
        this.setStackTrace(new StackTraceElement[0]);
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
