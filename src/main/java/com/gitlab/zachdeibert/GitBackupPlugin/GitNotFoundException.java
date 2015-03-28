package com.gitlab.zachdeibert.GitBackupPlugin;

public class GitNotFoundException extends Exception
{
    private static final long serialVersionUID = 7075262785793806939L;

    public GitNotFoundException() {
        super();
    }

    public GitNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GitNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitNotFoundException(String message) {
        super(message);
    }

    public GitNotFoundException(Throwable cause) {
        super(cause);
    }
}
