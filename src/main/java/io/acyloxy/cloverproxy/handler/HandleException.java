package io.acyloxy.cloverproxy.handler;

public class HandleException extends RuntimeException {
    public HandleException(String message) {
        super(message);
    }

    public HandleException(Throwable cause) {
        super(cause);
    }

    public HandleException(String message, Throwable cause) {
        super(message, cause);
    }
}
